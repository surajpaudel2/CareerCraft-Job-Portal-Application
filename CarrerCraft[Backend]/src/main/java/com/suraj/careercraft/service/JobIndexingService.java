package com.suraj.careercraft.service;

import com.suraj.careercraft.model.Job;

public interface JobIndexingService {
    void indexJob(Job job);

    void deleteJob(String jobId);
}
