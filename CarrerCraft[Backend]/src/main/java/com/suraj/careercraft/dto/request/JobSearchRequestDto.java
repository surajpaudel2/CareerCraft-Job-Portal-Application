package com.suraj.careercraft.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobSearchRequestDto {
    private String title;
    private String location;
    private List<String> workType;
    private Double minSalary;
    private Double maxSalary;
    private LocalDateTime postedAfter;

    // Pagination fields
    private Integer page = 0; // Default page
    private Integer size = 10; // Default page size
}

