package com.suraj.careercraft.service.impl;

import com.suraj.careercraft.model.EmployerProfile;
import com.suraj.careercraft.model.Job;
import com.suraj.careercraft.model.enums.JobStatus;
import com.suraj.careercraft.model.elasticsearch.JobDocument;
import com.suraj.careercraft.repository.JobSearchRepository;
import com.suraj.careercraft.service.JobIndexingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobIndexingServiceImpl implements JobIndexingService {

    private final JobSearchRepository jobSearchRepository;

    @Autowired
    public JobIndexingServiceImpl(JobSearchRepository jobSearchRepository) {
        this.jobSearchRepository = jobSearchRepository;
    }

    /**
     * Indexes a Job entity into Elasticsearch.
     *
     * @param job the Job entity to be indexed.
     */
    @Override
    public void indexJob(Job job) {
        JobDocument document = convertToJobDocument(job);
        jobSearchRepository.save(document);
    }

    /**
     * Deletes a Job document from Elasticsearch by its ID.
     *
     * @param jobId the ID of the Job document to delete.
     */
    @Override
    public void deleteJob(String jobId) {
        jobSearchRepository.deleteById(jobId);
    }

    /**
     * Converts a Job entity to a JobDocument for Elasticsearch.
     *
     * @param job the Job entity.
     * @return the JobDocument.
     */
    private JobDocument convertToJobDocument(Job job) {
        EmployerProfile employerProfile = job.getEmployer();

        return JobDocument.builder()
                .id(job.getId().toString())
                .employerId(employerProfile.getId().toString())
                .title(job.getTitle())// Add exact match field for filtering or sorting
                .description(job.getDescription())
                .location(job.getLocation()) // Add exact match field for filtering
                .requirements(job.getRequirements())
                .salary(job.getSalary() != null ? job.getSalary().doubleValue() : 0.0)
                .status(job.getStatus() != null ? job.getStatus().name() : JobStatus.ACTIVE.name())
                .postedAt(job.getPostedAt())
                .companyName(employerProfile.getCompanyName())
                .industry(employerProfile.getIndustry())
                .logoUrl(employerProfile.getLogoUrl())
                .jobTypes(job.getJobType())
                .build();
    }
}
