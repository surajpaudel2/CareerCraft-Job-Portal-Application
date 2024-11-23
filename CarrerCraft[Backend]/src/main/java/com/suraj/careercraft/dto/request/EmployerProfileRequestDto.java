package com.suraj.careercraft.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployerProfileRequestDto {

    @NotBlank(message = "Username or Email is required")
    private String usernameOrEmail;

    @NotBlank(message = "Company name is required")
    @Size(max = 100, message = "Company name should not exceed 100 characters")
    private String companyName;

    @NotBlank(message = "Industry is required")
    private String industry;

    @Size(max = 1000, message = "Description should not exceed 1000 characters")
    private String description;

    @NotBlank(message = "Location is required")
    @Size(max = 100, message = "Location should not exceed 100 characters")
    private String location;
}
