package com.suraj.careercraft.events;

import com.suraj.careercraft.events.type.JobEventType;
import com.suraj.careercraft.model.Job;

public class JobEvent {
    private final Job job;
    private final JobEventType eventType; // CREATE, UPDATE, DELETE

    public JobEvent(Job job, JobEventType eventType) {
        this.job = job;
        this.eventType = eventType;
    }

    public Job getJob() {
        return job;
    }

    public JobEventType getEventType() {
        return eventType;
    }
}

