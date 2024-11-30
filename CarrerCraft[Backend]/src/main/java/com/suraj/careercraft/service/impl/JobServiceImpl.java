package com.suraj.careercraft.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.suraj.careercraft.dto.request.JobRequestDto;
import com.suraj.careercraft.dto.request.JobSearchRequestDto;
import com.suraj.careercraft.events.publisher.JobEventPublisher;
import com.suraj.careercraft.events.type.JobEventType;
import com.suraj.careercraft.exceptions.JobNotFoundException;
import com.suraj.careercraft.exceptions.JobPersistenceException;
import com.suraj.careercraft.model.EmployerProfile;
import com.suraj.careercraft.model.Job;
import com.suraj.careercraft.model.enums.JobStatus;
import com.suraj.careercraft.model.elasticsearch.JobDocument;
import com.suraj.careercraft.repository.JobRepository;
import com.suraj.careercraft.repository.JobSearchRepository;
import com.suraj.careercraft.service.EmployerProfileService;
import com.suraj.careercraft.service.JobService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    private static final Logger log = LogManager.getLogger(JobServiceImpl.class);
    private final JobRepository jobRepository;
    private final EmployerProfileService employerProfileService;
    private final JobEventPublisher jobEventPublisher;
    private final JobSearchRepository jobSearchRepository;
    private final ElasticsearchClient elasticsearchClient;

    public JobServiceImpl(JobRepository jobRepository, EmployerProfileService employerProfileService,
                          JobEventPublisher jobEventPublisher, JobSearchRepository jobSearchRepository,
                          ElasticsearchClient elasticsearchClient) {
        this.jobRepository = jobRepository;
        this.employerProfileService = employerProfileService;
        this.jobEventPublisher = jobEventPublisher;
        this.jobSearchRepository = jobSearchRepository;
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<Job> findJobsByEmployerId(Long employerId) {
        return jobRepository.findByEmployerId(employerId);
    }

    @Override
    public Job createJob(Job job) {
        try {
            // Save the job in MySQL
            Job savedJob = jobRepository.save(job);

            // Publish job creation event
            boolean indexedSuccessfully = jobEventPublisher.publishJobEvent(savedJob, JobEventType.CREATE);

            // If indexing fails, delete the saved job from MySQL
            if (!indexedSuccessfully) {
                log.error("Failed to index Job with ID: {} in Elasticsearch. Rolling back MySQL save.", savedJob.getId());
                jobRepository.deleteById(savedJob.getId());
                throw new JobPersistenceException("Job creation failed due to Elasticsearch indexing issue.");
            }

            return savedJob;
        } catch (Exception e) {
            log.error("Error while saving the Job with company name: {}", job.getEmployer().getCompanyName(), e);
            throw new JobPersistenceException("Error while creating Job", e);
        }
    }

    @Override
    public void deleteJob(Long jobId) {
        try {
            // Find the job in MySQL
            Job job = jobRepository.findById(jobId).orElseThrow(() -> new JobNotFoundException("Job not found"));

            // Delete the job from MySQL
            jobRepository.deleteById(jobId);
            log.info("Deleted Job with ID: {} from MySQL", jobId);

            // Publish job deletion event
            boolean deletedFromElastic = jobEventPublisher.publishJobEvent(job, JobEventType.DELETE);

            // Log warning if Elasticsearch deletion fails
            if (!deletedFromElastic) {
                log.warn("Failed to delete Job with ID: {} from Elasticsearch. Please verify manually.", jobId);
            }
        } catch (Exception e) {
            log.error("Error while deleting the Job with ID: {}", jobId, e);
            throw new JobPersistenceException("Error while deleting the Job", e);
        }
    }

//    @Override
//    public Page<JobDocument> searchJobs(JobSearchRequestDto searchRequestDto) {
//        Pageable pageable = PageRequest.of(searchRequestDto.getPage(), searchRequestDto.getSize());
//        System.out.println(searchRequestDto.getTitle() + "   " + searchRequestDto.getLocation() + "   " + searchRequestDto.getPage() + "   "  + searchRequestDto.getSize());
//        // Title + Location + Salary
//        if (searchRequestDto.getTitle() != null && !searchRequestDto.getTitle().isEmpty() &&
//                searchRequestDto.getLocation() != null && !searchRequestDto.getLocation().isEmpty() &&
//                searchRequestDto.getMinSalary() != null && searchRequestDto.getMaxSalary() != null) {
//            if (searchRequestDto.getMinSalary() > searchRequestDto.getMaxSalary()) {
//                throw new IllegalArgumentException("Min salary cannot be greater than max salary");
//            }
//            Page<JobDocument> result = jobSearchRepository.fuzzySearchByTitleFieldsLocationAndSalary(
//                    searchRequestDto.getTitle(), searchRequestDto.getLocation(),
//                    searchRequestDto.getMinSalary(), searchRequestDto.getMaxSalary(), pageable);
//            return result.isEmpty() ? Page.empty(pageable) : result;
//        }
//
//        // Title + Location
//        if (searchRequestDto.getTitle() != null && !searchRequestDto.getTitle().isEmpty() &&
//                searchRequestDto.getLocation() != null && !searchRequestDto.getLocation().isEmpty()) {
//            Page<JobDocument> result = jobSearchRepository.fuzzySearchByFieldsLocationAndStatus(
//                    searchRequestDto.getTitle(), searchRequestDto.getLocation(), JobStatus.ACTIVE, pageable);
//            return result.isEmpty() ? Page.empty(pageable) : result;
//        }
//
//        // Title + Salary
//        if (searchRequestDto.getTitle() != null && !searchRequestDto.getTitle().isEmpty() &&
//                searchRequestDto.getMinSalary() != null && searchRequestDto.getMaxSalary() != null) {
//            if (searchRequestDto.getMinSalary() > searchRequestDto.getMaxSalary()) {
//                throw new IllegalArgumentException("Min salary cannot be greater than max salary");
//            }
//            Page<JobDocument> result = jobSearchRepository.fuzzySearchByTitleFieldsAndSalary(
//                    searchRequestDto.getTitle(), searchRequestDto.getMinSalary(),
//                    searchRequestDto.getMaxSalary(), pageable);
//            return result.isEmpty() ? Page.empty(pageable) : result;
//        }
//
//        // Location + Salary
//        if (searchRequestDto.getLocation() != null && !searchRequestDto.getLocation().isEmpty() &&
//                searchRequestDto.getMinSalary() != null && searchRequestDto.getMaxSalary() != null) {
//            if (searchRequestDto.getMinSalary() > searchRequestDto.getMaxSalary()) {
//                throw new IllegalArgumentException("Min salary cannot be greater than max salary");
//            }
//            Page<JobDocument> result = jobSearchRepository.fuzzySearchByLocationAndSalary(
//                    searchRequestDto.getLocation(), searchRequestDto.getMinSalary(),
//                    searchRequestDto.getMaxSalary(), pageable);
//            return result.isEmpty() ? Page.empty(pageable) : result;
//        }
//
//        // Title Only
//        if (searchRequestDto.getTitle() != null && !searchRequestDto.getTitle().isEmpty()) {
//            Page<JobDocument> result = jobSearchRepository.fuzzySearchByFieldsAndStatus(
//                    searchRequestDto.getTitle(), JobStatus.ACTIVE, pageable);
//            return result.isEmpty() ? Page.empty(pageable) : result;
//        }
//
//        // Location Only
//        if (searchRequestDto.getLocation() != null && !searchRequestDto.getLocation().isEmpty()) {
//            Page<JobDocument> result = jobSearchRepository.fuzzySearchByLocation(
//                    searchRequestDto.getLocation(), pageable);
//            return result.isEmpty() ? Page.empty(pageable) : result;
//        }
//
//        // Salary Only
//        if (searchRequestDto.getMinSalary() != null && searchRequestDto.getMaxSalary() != null) {
//            if (searchRequestDto.getMinSalary() > searchRequestDto.getMaxSalary()) {
//                throw new IllegalArgumentException("Min salary cannot be greater than max salary");
//            }
//            Page<JobDocument> result = jobSearchRepository.findBySalaryBetween(
//                    searchRequestDto.getMinSalary(), searchRequestDto.getMaxSalary(), pageable);
//            return result.isEmpty() ? Page.empty(pageable) : result;
//        }
//
//        // Default case - Return all jobs (pagination applied)
//        Page<JobDocument> result = jobSearchRepository.findAll(pageable);
//        return result.isEmpty() ? Page.empty(pageable) : result;
//    }

    @Override
    public Page<JobDocument> searchJobs(JobSearchRequestDto searchRequestDto) {
        Pageable pageable = PageRequest.of(searchRequestDto.getPage(), searchRequestDto.getSize());

        try {
            // Build the Bool Query dynamically
            BoolQuery.Builder boolQuery = new BoolQuery.Builder();

            // Add Title Search
            if (isNotEmpty(searchRequestDto.getTitle())) {
                boolQuery.should(Query.of(q -> q
                        .fuzzy(f -> f
                                .field("title")
                                .value(searchRequestDto.getTitle())
                        )
                ));
            }

            // Add Location Filter
            if (isNotEmpty(searchRequestDto.getLocation())) {
                boolQuery.filter(Query.of(q -> q
                        .term(t -> t
                                .field("location.keyword")
                                .value(searchRequestDto.getLocation())
                        )
                ));
            }

//            // Add Work Type Filter
//            if (isNotEmpty(searchRequestDto.getWorkType())) {
//                boolQuery.filter(Query.of(q -> q
//                        .term(t -> t
//                                .field("workType.keyword")
//                                .value(searchRequestDto.getWorkType())
//                        )
//                ));
//            }

            // Add Salary Range Filter
            if (searchRequestDto.getMinSalary() != null && searchRequestDto.getMaxSalary() != null) {
                if (searchRequestDto.getMinSalary() > searchRequestDto.getMaxSalary()) {
                    throw new IllegalArgumentException("Min salary cannot be greater than max salary");
                }
                boolQuery.filter(Query.of(q -> q
                        .range(r -> r
                                .field("salary")
                                .gte(JsonData.of(searchRequestDto.getMinSalary()))
                                .lte(JsonData.of(searchRequestDto.getMaxSalary()))
                        )
                ));

            }

            // Add Posted After Date Filter
            if (searchRequestDto.getPostedAfter() != null) {
                boolQuery.filter(Query.of(q -> q
                        .range(r -> r
                                .field("postedAt")
                                .gte(JsonData.fromJson(searchRequestDto.getPostedAfter().format(DateTimeFormatter.ISO_DATE_TIME)))
                        )
                ));
            }

            // Build the Search Request
            SearchRequest searchRequest = SearchRequest.of(sr -> sr
                    .index("jobs") // Replace with your Elasticsearch index name
                    .query(Query.of(q -> q.bool(boolQuery.build())))
                    .from((int) pageable.getOffset())
                    .size(pageable.getPageSize())
            );

            // Execute the Search
            SearchResponse<JobDocument> searchResponse = elasticsearchClient.search(searchRequest, JobDocument.class);

            // Map Hits to Page
            List<JobDocument> jobDocuments = searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            return new PageImpl<>(jobDocuments, pageable, searchResponse.hits().total().value());

        } catch (IOException e) {
            e.printStackTrace();
            return Page.empty(pageable);
        }
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }


    @Override
    public Job convertDto(JobRequestDto jobRequestDto) {
        String email = jobRequestDto.getEmployerEmail();
        EmployerProfile employerProfile = employerProfileService.getEmployerProfileByUsernameOrEmail(email);

        Job job = new Job();
        job.setId(jobRequestDto.getId());
        job.setTitle(jobRequestDto.getTitle());
        job.setDescription(jobRequestDto.getDescription());
        job.setSalary(jobRequestDto.getSalary());
        job.setLocation(jobRequestDto.getLocation());
        job.setRequirements(jobRequestDto.getRequirements());
        job.setStatus(getJobStatus(jobRequestDto.getStatus()));
        job.setEmployer(employerProfile);

        return job;
    }

    private JobStatus getJobStatus(String strStatus) {
        JobStatus jobStatus = null;
        if (strStatus.equalsIgnoreCase("ACTIVE")) {
            jobStatus = JobStatus.ACTIVE;
        } else if (strStatus.equalsIgnoreCase("PENDING")) {
            jobStatus = JobStatus.PENDING;
        } else if (strStatus.equalsIgnoreCase("CLOSED")) {
            jobStatus = JobStatus.CLOSED;
        }
        return jobStatus;
    }
}
