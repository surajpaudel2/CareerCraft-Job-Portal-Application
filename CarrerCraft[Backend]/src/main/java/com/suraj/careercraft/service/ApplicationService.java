package com.suraj.careercraft.service;

import com.suraj.careercraft.model.Application;

import java.util.List;

public interface ApplicationService {

    List<Application> findApplicationsByJobSeekerId(Long jobSeekerId);

    Application applyToJob(Application application);
}
