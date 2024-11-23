package com.suraj.careercraft.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobSearchRequestDto {
    private String title;
    private String location;
    private Double minSalary;
    private Double maxSalary;
    private LocalDateTime postedAfter;

    // Pagination fields
    private Integer page = 0; // Default page
    private Integer size = 10; // Default page size
}

