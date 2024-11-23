package com.suraj.careercraft.service;

import com.suraj.careercraft.dto.EmployerProfileDto;
import com.suraj.careercraft.model.EmployerProfile;

public interface EmployerProfileService {
    EmployerProfile saveEmployerProfile(EmployerProfile employerProfile);

    EmployerProfile getEmployerProfileById(long id);

    EmployerProfile getEmployerProfileByUsernameOrEmail(String usernameOrEmail);

    EmployerProfileDto generateEmployerProfileDto(EmployerProfile employerProfile);

    EmployerProfileDto createEmployerProfileDto(String companyName, String industry, String description,
                                          String location);

    boolean deleteEmployerProfileById(long id);

    void updateEmployerProfile(EmployerProfile employerProfile);

    EmployerProfile createEmployerProfile(long id, String companyName, String industry,
                                          String description, String location, String logoUrl, String publicId);
}
