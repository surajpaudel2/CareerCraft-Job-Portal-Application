package com.suraj.careercraft.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.suraj.careercraft.dto.request.JobSearchRequestDto;
import com.suraj.careercraft.model.elasticsearch.JobDocument;
import com.suraj.careercraft.repository.JobSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JobSearchRepositoryImpl implements JobSearchRepository {
    private static final Logger log = LoggerFactory.getLogger(JobSearchRepositoryImpl.class);
    private final ElasticsearchClient elasticsearchClient;

    private final String indexName = JobDocument.class.getAnnotation(Document.class).indexName();

    public JobSearchRepositoryImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public void save(JobDocument jobDocument) {
        try {
            elasticsearchClient.index(IndexRequest.of(i -> i.index(indexName).id(jobDocument.getId()).document(jobDocument)));
        } catch (Exception e) {
            log.error("Error saving job", e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            elasticsearchClient.delete(DeleteRequest.of(d -> d.index(indexName) // Use the extracted index name
                    .id(id)));
            log.info("Job document deleted successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting job document with ID: {}", id, e);
        }
    }

//    Search Requests :\

    @Override
    public List<JobDocument> search(JobSearchRequestDto requestDto) {
        try {
            // Build the query dynamically
            BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

            // Add fuzzy search on title, description, requirements, and company name
            if (requestDto.getTitle() != null && !requestDto.getTitle().isEmpty()) {
                boolQueryBuilder.must(Query.of(q -> q.multiMatch(m -> m.fields("title^3", "description", "requirements", "companyName").query(requestDto.getTitle()).fuzziness("AUTO"))));
            }

            // Add term filter for location
            if (requestDto.getLocation() != null && !requestDto.getLocation().isEmpty()) {
                boolQueryBuilder.filter(Query.of(q -> q.term(t -> t.field("locationKeyword").value(requestDto.getLocation()))));
            }
            // Add terms filter for workType
            if (requestDto.getWorkType() != null && !requestDto.getWorkType().isEmpty()) {
                boolQueryBuilder.filter(Query.of(q -> q.terms(t -> t.field("jobTypes").terms(tl -> tl.value(requestDto.getWorkType().stream().map(FieldValue::of).toList())))));
            }

            // Add range filter for salary
            if (requestDto.getMinSalary() != null || requestDto.getMaxSalary() != null) {
                boolQueryBuilder.filter(Query.of(q -> q.range(r -> r.number(nr -> {
                    nr.field("salary");
                    if (requestDto.getMinSalary() != null) {
                        nr.gte(requestDto.getMinSalary()); // Pass Double directly
                    }
                    if (requestDto.getMaxSalary() != null) {
                        nr.lte(requestDto.getMaxSalary()); // Pass Double directly
                    }
                    return nr;
                }))));
            }

            if (requestDto.getPostedAfter() != null) {
                boolQueryBuilder.filter(Query.of(q -> q.range(r -> r.date(dr -> {
                    dr.field("postedAt");
                    dr.gte(requestDto.getPostedAfter().format(DateTimeFormatter.ISO_DATE_TIME)); // Format LocalDateTime to ISO-8601
                    return dr;
                }))));
            }

            // Build the search request
            SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName).query(Query.of(q -> q.bool(boolQueryBuilder.build()))).from(requestDto.getPage() * requestDto.getSize()).size(requestDto.getSize()));

            // Execute the search request
            SearchResponse<JobDocument> response = elasticsearchClient.search(searchRequest, JobDocument.class);

            // Parse and return the results
            return response.hits().hits().stream().map(hit -> hit.source()).toList();

        } catch (Exception e) {
            log.error("Error searching job documents", e);
            return new ArrayList<>();
        }
    }
}

