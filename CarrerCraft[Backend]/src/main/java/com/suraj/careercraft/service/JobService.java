package com.suraj.careercraft.service;

import com.suraj.careercraft.dto.request.JobRequestDto;
import com.suraj.careercraft.dto.request.JobSearchRequestDto;
import com.suraj.careercraft.model.Job;
import com.suraj.careercraft.model.elasticsearch.JobDocument;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobService {

    List<Job> findJobsByEmployerId(Long employerId);

    Job createJob(Job job);

    void deleteJob(Long jobId);

    Page<JobDocument> searchJobs(JobSearchRequestDto searchRequestDto);

    Job convertDto(JobRequestDto jobRequestDto);
}
