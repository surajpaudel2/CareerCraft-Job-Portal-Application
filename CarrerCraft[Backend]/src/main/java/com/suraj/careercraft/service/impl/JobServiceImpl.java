package com.suraj.careercraft.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.suraj.careercraft.model.enums.JobType;
import com.suraj.careercraft.repository.JobRepository;
import com.suraj.careercraft.repository.JobSearchRepository;
import com.suraj.careercraft.service.EmployerProfileService;
import com.suraj.careercraft.service.JobService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    private static final Logger log = LogManager.getLogger(JobServiceImpl.class);
    private final JobRepository jobRepository;
    private final EmployerProfileService employerProfileService;
    private final JobEventPublisher jobEventPublisher;
    private final JobSearchRepository jobSearchRepository;

    public JobServiceImpl(JobRepository jobRepository, EmployerProfileService employerProfileService,
                          JobEventPublisher jobEventPublisher, JobSearchRepository jobSearchRepository
                          ) {
        this.jobRepository = jobRepository;
        this.employerProfileService = employerProfileService;
        this.jobEventPublisher = jobEventPublisher;
        this.jobSearchRepository = jobSearchRepository;
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

    @Override
    public List<JobDocument> searchJobs(JobSearchRequestDto request) {
        return jobSearchRepository.search(request);
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
        job.setJobType(getJobTypes(jobRequestDto.getJobType()));
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

    private List<JobType> getJobTypes(List<String> jobTypes) {
        List<JobType> newJobTypes = new ArrayList<>();
        for (String jobType : jobTypes) {
            newJobTypes.add(JobType.fromString(jobType));
        }
        return newJobTypes;
    }
}
