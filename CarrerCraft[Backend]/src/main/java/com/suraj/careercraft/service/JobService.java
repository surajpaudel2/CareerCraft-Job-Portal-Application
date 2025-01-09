package com.suraj.careercraft.service;

import com.suraj.careercraft.dto.request.JobRequestDto;
import com.suraj.careercraft.dto.request.JobSearchRequestDto;
import com.suraj.careercraft.model.Job;

import java.util.List;
import java.util.Map;

public interface JobService {

    List<Job> findJobsByEmployerId(Long employerId);

    Job createJob(Job job);

    void deleteJob(Long jobId);

    Map<String, Object> searchJobs(JobSearchRequestDto searchRequestDto);

    Job convertDto(JobRequestDto jobRequestDto);
}
