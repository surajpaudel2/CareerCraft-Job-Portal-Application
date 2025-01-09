package com.suraj.careercraft.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JobType {
    FULL_TIME("full time"),
    PART_TIME("part time"),
    TEMPORARY("temporary"),
    CONTRACT("contract"),
    INTERNSHIP("internship"),
    FREELANCE("freelance"),
    SEASONAL("seasonal"),
    VOLUNTEER("volunteer"),
    PER_DIEM("per diem"),
    ON_CALL("on call"),
    REMOTE("remote"),
    HYBRID("hybrid"),
    GIG_WORK("gig work"),
    COMMISSION_BASED("commission based"),
    APPRENTICESHIP("apprenticeship"),
    SHIFT_WORK("shift work");

    private final String displayName;

    JobType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static JobType fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("JobType value cannot be null or empty");
        }

        // Match against enum constants
        for (JobType jobType : values()) {
            if (jobType.name().equalsIgnoreCase(value.trim()) || // Check constant (e.g., PART_TIME)
                    jobType.displayName.equalsIgnoreCase(value.trim())) { // Check display name (e.g., part-time)
                return jobType;
            }
        }

        throw new IllegalArgumentException("Invalid JobType: " + value);
    }
}
