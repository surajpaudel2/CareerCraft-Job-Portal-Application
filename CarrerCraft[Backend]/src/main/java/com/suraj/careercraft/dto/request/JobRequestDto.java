package com.suraj.careercraft.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class JobRequestDto {
    private Long id;

    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be a positive number")
    private BigDecimal salary;

    @NotBlank(message = "Location is required")
    private String location;

    private List<String> requirements;

    private List<String> jobType;

    @NotNull(message = "Job status is required")
    private String status;

    @NotBlank(message = "Employer email is required")
    @Email(message = "Employer email must be valid")
    private String employerEmail;
}
