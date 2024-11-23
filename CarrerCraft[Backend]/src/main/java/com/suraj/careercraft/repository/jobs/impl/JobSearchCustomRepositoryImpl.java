package com.suraj.careercraft.repository.jobs.impl;

import com.suraj.careercraft.dto.request.JobSearchRequestDto;
import com.suraj.careercraft.model.elasticsearch.JobDocument;
import com.suraj.careercraft.repository.jobs.JobSearchCustomRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JobSearchCustomRepositoryImpl implements JobSearchCustomRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    public JobSearchCustomRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    /**
     * This method dynamically searches for JobDocument records in the Elasticsearch index
     * based on the user-provided filters in the JobSearchRequestDto.
     *
     * The query supports the following features:
     * 1. Full-text search for job titles, descriptions, location and requirements (using OR conditions).
     * 2. Exact match filtering for location keywords.
     * 3. Range filtering for salary (minimum and maximum salary).
     * 4. Range filtering for the posted date (jobs posted after a certain date).
     * 5. Pagination support to fetch specific pages of results.
     *
     * Steps:
     * - Dynamically build a Criteria query based on the provided filters.
     * - Apply pagination using the Pageable object.
     * - Execute the query using ElasticsearchOperations to fetch matching JobDocument records.
     * - Map the results into a list of JobDocument objects and return them.
     *
     * @param searchRequest The DTO containing dynamic search filters such as title, location, salary range, posted date, page, and size.
     * @return A list of JobDocument objects that match the specified search criteria.
     */
    @Override
    public List<JobDocument> searchJobsDynamically(JobSearchRequestDto searchRequest) {
        // Initialize Criteria for the query
        Criteria criteria = new Criteria();

        // Add title-related conditions (OR between title, description, location, and requirements)
        if (searchRequest.getTitle() != null && !searchRequest.getTitle().isEmpty()) {
            System.out.println(searchRequest.getTitle());
            String searchTitle = searchRequest.getTitle().toLowerCase();

            Criteria titleCriteria = new Criteria("title").matches(searchTitle)
                    .or("description").matches(searchTitle)
                    .or("location").matches(searchTitle)
                    .or("requirements").matches(searchTitle);

            criteria = criteria.and(titleCriteria);
        }

        // Add location filter
        if (searchRequest.getLocation() != null && !searchRequest.getLocation().isEmpty()) {
            criteria = criteria.and(new Criteria("locationKeyword").is(searchRequest.getLocation().toLowerCase()));
        }

        // Add salary range filter
        if (searchRequest.getMinSalary() != null && searchRequest.getMaxSalary() != null) {
            criteria = criteria.and(new Criteria("salary").between(searchRequest.getMinSalary(), searchRequest.getMaxSalary()));
        }

        // Add posted date filter
        if (searchRequest.getPostedAfter() != null) {
            criteria = criteria.and(new Criteria("postedAt").greaterThanEqual(searchRequest.getPostedAfter()));
        }

        // Create a Pageable object for pagination
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());

        // Build the CriteriaQuery
        Query query = new CriteriaQuery(criteria).setPageable(pageable);

        // Execute the query
        SearchHits<JobDocument> searchHits = elasticsearchOperations.search(query, JobDocument.class);

        // Map results to a list of JobDocument objects
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }


}
