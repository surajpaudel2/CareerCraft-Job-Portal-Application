package com.suraj.careercraft.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.suraj.careercraft.model.elasticsearch.JobDocument;
import com.suraj.careercraft.model.JobStatus;

public interface JobSearchRepository extends ElasticsearchRepository<JobDocument, String> {

    /**
     * title only
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
     * Location only
     * Fuzzy search by location.
     */
    @Query("""
    {
      "bool": {
        "should": [
          { "fuzzy": { "location.keyword": { "value": "?0" } } }
        ]
      }
    }
    """)
    Page<JobDocument> fuzzySearchByLocation(String location, Pageable pageable);


    /**
     * title + job location
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
     * by salary only
     * Find jobs within a salary range.
     */
    Page<JobDocument> findBySalaryBetween(Double minSalary, Double maxSalary, Pageable pageable);


    /**
     * title + location + salary
     * Fuzzy search by title fields, location, and salary range.
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
          { "range": { "salary": { "gte": "?2", "lte": "?3" } } }
        ]
      }
    }
    """)
    Page<JobDocument> fuzzySearchByTitleFieldsLocationAndSalary(String searchText, String location, Double minSalary, Double maxSalary, Pageable pageable);


    /**
     * title + salary
     * Fuzzy search by title fields and salary range.
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
          { "range": { "salary": { "gte": "?1", "lte": "?2" } } }
        ]
      }
    }
    """)
    Page<JobDocument> fuzzySearchByTitleFieldsAndSalary(String searchText, Double minSalary, Double maxSalary, Pageable pageable);

    /**
     * location + salary
     * Fuzzy search by location and salary range.
     */
    @Query("""
    {
      "bool": {
        "should": [
          { "fuzzy": { "location.keyword": { "value": "?0" } } }
        ],
        "filter": [
          { "range": { "salary": { "gte": "?1", "lte": "?2" } } }
        ]
      }
    }
    """)
    Page<JobDocument> fuzzySearchByLocationAndSalary(String location, Double minSalary, Double maxSalary, Pageable pageable);
}
