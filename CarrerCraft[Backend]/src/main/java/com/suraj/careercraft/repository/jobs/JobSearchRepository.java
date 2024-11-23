package com.suraj.careercraft.repository.jobs;

import com.suraj.careercraft.model.elasticsearch.JobDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface JobSearchRepository extends ElasticsearchRepository<JobDocument, String> {

    // Basic search by title
    List<JobDocument> findByTitleContaining(String title);

    // Search by location (exact match)
    List<JobDocument> findByLocation(String location);

    // Full-text search across title and description with pagination
    List<JobDocument> findByTitleContainingOrDescriptionContainingOrRequirementsContaining(String title,
                                                                                           String description,
                                                                                           String requirements);

    // Search by salary range
    List<JobDocument> findBySalaryBetween(Double minSalary, Double maxSalary);

    // Search by status
    List<JobDocument> findByStatus(String status);

    // Combined search: title + location + salary range
    @Query("{\"bool\": {\"must\": [{\"match\": {\"title\": \"?0\"}}, {\"term\": {\"location.keyword\": \"?1\"}}, {\"range\": {\"salary\": {\"gte\": ?2, \"lte\": ?3}}}]}}")
    List<JobDocument> searchByTitleLocationAndSalary(String title, String location, Double minSalary, Double maxSalary);

    // Fuzzy search for misspelled terms (e.g., "developr" -> "developer")
    @Query("{\"match\": {\"title\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    List<JobDocument> fuzzySearchByTitle(String title);

    // Geo-distance query for jobs near a given location
    @Query("{\"bool\": {\"filter\": {\"geo_distance\": {\"distance\": \"?2\", \"location_point\": {\"lat\": ?0, \"lon\": ?1}}}}}")
    List<JobDocument> searchByProximity(Double lat, Double lon, String distance);

    // Multi-field search with boosting
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title^3\", \"description\", \"requirements\"]}}")
    List<JobDocument> multiFieldSearchWithBoost(String query);

    // Advanced search with flexible filtering
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title\", \"description\", \"requirements\"]}}], \"filter\": [{\"term\": {\"status\": \"?1\"}}, {\"range\": {\"salary\": {\"gte\": ?2, \"lte\": ?3}}}]}}")
    List<JobDocument> advancedSearchWithFilters(String query, String status, Double minSalary, Double maxSalary);
}
