package com.suraj.careercraft.service.impl;

import com.suraj.careercraft.model.Application;
import com.suraj.careercraft.repository.ApplicationRepository;
import com.suraj.careercraft.service.ApplicationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<Application> findApplicationsByJobSeekerId(Long jobSeekerId) {
        return applicationRepository.findByJobSeekerId(jobSeekerId);
    }

    public Application applyToJob(Application application) {
        return applicationRepository.save(application);
    }
}
