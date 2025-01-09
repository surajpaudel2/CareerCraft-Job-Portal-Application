package com.suraj.careercraft.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.suraj.careercraft.dto.request.JobSearchRequestDto;
import com.suraj.careercraft.model.elasticsearch.JobDocument;
import com.suraj.careercraft.repository.JobSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Map<String, Object> search(JobSearchRequestDto requestDto) {
        try {
            // Build the query dynamically
            BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

            // Fuzzy search on title, description, requirements, and company name
            if (requestDto.getTitle() != null && !requestDto.getTitle().isEmpty()) {
                boolQueryBuilder.must(Query.of(q -> q.multiMatch(m -> m
                        .fields("title^3", "description", "requirements", "companyName")
                        .query(requestDto.getTitle())
                        .fuzziness("AUTO"))));
            }

            // Term filter for location
            if (requestDto.getLocation() != null && !requestDto.getLocation().isEmpty()) {
                boolQueryBuilder.filter(Query.of(q -> q.match(m -> m
                        .field("location")
                        .query(requestDto.getLocation()))));
            }

            // Terms filter for workType
            if (requestDto.getWorkType() != null && !requestDto.getWorkType().isEmpty()) {
                List<String> workTypeValues = requestDto.getWorkType().stream()
                        .map(String::toLowerCase)
                        .toList();

                boolQueryBuilder.filter(Query.of(q -> q.terms(t -> t
                        .field("jobTypes.keyword")
                        .terms(tl -> tl.value(workTypeValues.stream()
                                .map(FieldValue::of)
                                .toList())))));
            }

            // Range filter for salary
            if (requestDto.getMinSalary() != null || requestDto.getMaxSalary() != null) {
                boolQueryBuilder.filter(Query.of(q -> q.range(r -> r.number(nr -> {
                    nr.field("salary");
                    if (requestDto.getMinSalary() != null) nr.gte(requestDto.getMinSalary());
                    if (requestDto.getMaxSalary() != null) nr.lte(requestDto.getMaxSalary());
                    return nr;
                }))));
            }

            // Date range filter for postedAt
            if (requestDto.getPostedAfter() != null) {
                boolQueryBuilder.filter(Query.of(q -> q.range(r -> r.date(dr -> {
                    dr.field("postedAt");
                    dr.gte(requestDto.getPostedAfter().format(DateTimeFormatter.ISO_DATE_TIME));
                    return dr;
                }))));
            }

            // Build the BoolQuery only once
            BoolQuery boolQuery = boolQueryBuilder.build();

            // Pagination parameters
            int page = requestDto.getPage() != null ? requestDto.getPage() : 0;
            int size = requestDto.getSize() != null ? requestDto.getSize() : 10;
            int from = page * size;

            // Step 1: Count total elements from the current page onward, capped at 50
            SearchRequest countRequest = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(Query.of(q -> q.bool(boolQuery)))
                    .terminateAfter((long)1) // Cap the total count at 50 from the current offset
                    .size(0)); // No documents fetched, just count

            SearchResponse<Void> countResponse = elasticsearchClient.search(countRequest, Void.class);
            int totalCount = (int) countResponse.hits().total().value();

            // Calculate remaining elements to be shown
            int remainingCount = Math.min(totalCount - from, 50);

            // Step 2: Fetch paginated results
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(Query.of(q -> q.bool(boolQuery)))
                    .from(from)
                    .size(size));

            SearchResponse<JobDocument> response = elasticsearchClient.search(searchRequest, JobDocument.class);

            List<JobDocument> jobs = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            // Step 3: Build the response in a HashMap
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("jobs", jobs); // List of jobs for the current page
            responseMap.put("remainingCount", remainingCount); // Remaining elements capped at 50

            return responseMap;

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }


}

