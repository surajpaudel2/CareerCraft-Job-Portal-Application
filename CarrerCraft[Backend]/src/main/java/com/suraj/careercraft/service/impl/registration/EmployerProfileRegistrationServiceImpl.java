package com.suraj.careercraft.service.impl.registration;

import com.suraj.careercraft.dto.request.EmployerProfileRequestDto;
import com.suraj.careercraft.model.EmployerProfile;
import com.suraj.careercraft.model.User;
import com.suraj.careercraft.service.registration.EmployerProfileRegistrationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class EmployerProfileRegistrationServiceImpl implements EmployerProfileRegistrationService {
    private static final Logger log = LogManager.getLogger(EmployerProfileRegistrationServiceImpl.class);

    public EmployerProfileRegistrationServiceImpl() {
    }

    @Override
    public EmployerProfileRequestDto createEmployerProfileRequest(String usernameOrEmail, String companyName,
                                                                  String industry,
                                                                  String description, String location) {
        EmployerProfileRequestDto profileRequest = new EmployerProfileRequestDto();
        profileRequest.setUsernameOrEmail(usernameOrEmail);
        profileRequest.setCompanyName(companyName);
        profileRequest.setIndustry(industry);
        profileRequest.setDescription(description);
        profileRequest.setLocation(location);
        return profileRequest;
    }

    @Override
    public EmployerProfile createEmployerProfile(User user, String companyName, String industry, String description,
                                                 String location, String logoUrl, String logoPublicId) {
        EmployerProfile employerProfile = new EmployerProfile();
        employerProfile.setUser(user);
        employerProfile.setCompanyName(companyName);
        employerProfile.setIndustry(industry);
        employerProfile.setDescription(description);
        employerProfile.setLocation(location);
        employerProfile.setLogoUrl(logoUrl);
        employerProfile.setLogoPublicId(logoPublicId);
        return employerProfile;
    }
}
