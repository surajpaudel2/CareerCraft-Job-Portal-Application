package com.suraj.careercraft.service.registration;

import com.suraj.careercraft.dto.request.EmployerProfileRequestDto;
import com.suraj.careercraft.dto.request.EmployerProfileRequestDto;
import com.suraj.careercraft.model.EmployerProfile;
import com.suraj.careercraft.model.User;

public interface EmployerProfileRegistrationService {
    EmployerProfileRequestDto createEmployerProfileRequest(String usernameOrEmail, String companyName, String industry,
                                                           String description, String location);

    EmployerProfile createEmployerProfile(User user, String companyName, String industry, String description,
                                          String location, String logoUrl, String logoPublicId);

}
