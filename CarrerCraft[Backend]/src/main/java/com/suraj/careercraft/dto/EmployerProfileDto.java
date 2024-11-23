package com.suraj.careercraft.dto;

import com.suraj.careercraft.model.Job;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerProfileDto {
    @NotBlank(message = "Company name cannot be empty")
    @Size(max = 100, message = "Company name should not exceed 100 characters")
    private String companyName;

    @NotBlank(message = "Industry cannot be empty")
    @Size(max = 50, message = "Industry should not exceed 50 characters")
    private String industry;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 1000, message = "Description should not exceed 100 characters")
    private String description;

    @NotBlank(message = "Location cannot be empty")
    @Size(max = 100, message = "Location should not exceed 100 characters")
    private String location;

    @Size(max = 200, message = "Logo URL should not exceed 200 characters")
    private String logoUrl;

    private List<Job> postedJobs;
}
