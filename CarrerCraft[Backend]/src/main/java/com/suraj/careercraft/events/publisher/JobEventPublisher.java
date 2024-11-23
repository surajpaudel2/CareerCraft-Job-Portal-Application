package com.suraj.careercraft.events.publisher;

import com.suraj.careercraft.events.JobEvent;
import com.suraj.careercraft.events.type.JobEventType;
import com.suraj.careercraft.model.Job;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class JobEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public JobEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public boolean publishJobEvent(Job job, JobEventType jobEventType) {
        try {
            JobEvent event = new JobEvent(job, jobEventType);
            eventPublisher.publishEvent(event);
            return true; // Successfully published the event
        } catch (Exception e) {
            // Log the error and return false to indicate failure
            Logger log = LogManager.getLogger(JobEventPublisher.class);
            log.error("Failed to publish JobEvent for Job ID: {} and Event Type: {}", job.getId(), jobEventType, e);
            return false;
        }
    }
}

