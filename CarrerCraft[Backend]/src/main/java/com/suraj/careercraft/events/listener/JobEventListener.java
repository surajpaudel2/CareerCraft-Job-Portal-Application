package com.suraj.careercraft.events.listener;

import com.suraj.careercraft.events.JobEvent;
import com.suraj.careercraft.model.Job;
import com.suraj.careercraft.service.JobIndexingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class JobEventListener {
    private final JobIndexingService jobIndexingService;

    @Autowired
    public JobEventListener(JobIndexingService jobIndexingService) {
        this.jobIndexingService = jobIndexingService;
    }

    @EventListener
    public void handleJobEvent(JobEvent event) {
        Job job = event.getJob();
        switch (event.getEventType()) {
            case CREATE:
            case UPDATE:
                jobIndexingService.indexJob(job);
                break;
            case DELETE:
                jobIndexingService.deleteJob(job.getId().toString());
                break;
        }
    }
}
