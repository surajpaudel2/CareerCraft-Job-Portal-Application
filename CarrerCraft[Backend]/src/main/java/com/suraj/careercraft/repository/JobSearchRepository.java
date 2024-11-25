package com.suraj.careercraft.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.suraj.careercraft.model.elasticsearch.JobDocument;
import com.suraj.careercraft.model.JobStatus;

public interface JobSearchRepository extends ElasticsearchRepository<JobDocument, String> {

    /**
     * Fuzzy search by title, description, or requirements and filter by job status.
     */
    @Query("""
    {
      "bool": {
        "should": [
          { "fuzzy": { "title": { "value": "?0" } } },
          { "fuzzy": { "description": { "value": "?0" } } },
          { "fuzzy": { "requirements": { "value": "?0" } } }
        ],
        "filter": [
          { "term": { "status": "?1" } }
        ]
      }
    }
    """)
    Page<JobDocument> fuzzySearchByFieldsAndStatus(String searchText, JobStatus jobStatus, Pageable pageable);

    /**
     * Fuzzy search by title, description, or requirements, filter by location and job status.
     */
    @Query("""
    {
      "bool": {
        "should": [
          { "fuzzy": { "title": { "value": "?0" } } },
          { "fuzzy": { "description": { "value": "?0" } } },
          { "fuzzy": { "requirements": { "value": "?0" } } }
        ],
        "filter": [
          { "term": { "location.keyword": "?1" } },
          { "term": { "status": "?2" } }
        ]
      }
    }
    """)
    Page<JobDocument> fuzzySearchByFieldsLocationAndStatus(String searchText, String location, JobStatus jobStatus, Pageable pageable);

    /**
     * Find jobs within a salary range.
     */
    Page<JobDocument> findBySalaryBetween(Double minSalary, Double maxSalary, Pageable pageable);
}
