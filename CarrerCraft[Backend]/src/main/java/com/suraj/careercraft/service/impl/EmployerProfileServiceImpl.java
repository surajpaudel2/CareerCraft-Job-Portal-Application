package com.suraj.careercraft.service.impl;

import com.suraj.careercraft.dto.EmployerProfileDto;
import com.suraj.careercraft.exceptions.EmployerProfileNotFoundException;
import com.suraj.careercraft.exceptions.EmployerProfilePersistenceException;
import com.suraj.careercraft.model.EmployerProfile;
import com.suraj.careercraft.model.Job;
import com.suraj.careercraft.model.User;
import com.suraj.careercraft.repository.EmployerProfileRepository;
import com.suraj.careercraft.service.EmployerProfileService;
import com.suraj.careercraft.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployerProfileServiceImpl implements EmployerProfileService {
    private static final Logger log = LogManager.getLogger(EmployerProfileServiceImpl.class);
    private final EmployerProfileRepository employerProfileRepository;
    private final UserService userService;

    @Autowired
    public EmployerProfileServiceImpl(EmployerProfileRepository employerProfileRepository, UserService userService) {
        this.employerProfileRepository = employerProfileRepository;
        this.userService = userService;
    }

    @Override
    public EmployerProfile saveEmployerProfile(EmployerProfile employerProfile) {
        try {
            return employerProfileRepository.save(employerProfile);
        } catch (Exception e) {
            log.error("Error while saving Employer Profile with company name : {}",
                    employerProfile.getCompanyName(), e);
            throw new EmployerProfilePersistenceException("Error while saving Employer Profile", e);
        }
    }

    @Override
    public EmployerProfile getEmployerProfileById(long id) {
        return employerProfileRepository.findById(id)
                .orElseThrow(() -> new EmployerProfileNotFoundException("Employer Profile with id " + id + " not found"));
    }

    @Override
    public EmployerProfile getEmployerProfileByUsernameOrEmail(String usernameOrEmail) {
        User user = userService.findByUsernameOrEmail(usernameOrEmail);
        return employerProfileRepository.findByUser(user)
                .orElseThrow(() -> new EmployerProfileNotFoundException("Employer profile not found for user: " + user.getUsername()));

    }

    @Override
    public EmployerProfileDto generateEmployerProfileDto(EmployerProfile employerProfile) {
        String companyName = employerProfile.getCompanyName();
        String industry = employerProfile.getIndustry();
        String description = employerProfile.getDescription();
        String location = employerProfile.getLocation();
        String logoUrl = employerProfile.getLogoUrl();
        List<Job> postedJobs = employerProfile.getPostedJobs();

        return new EmployerProfileDto(companyName, industry, description, location, logoUrl, postedJobs);
    }

    @Override
    public EmployerProfileDto createEmployerProfileDto(String companyName, String industry, String description,
                                                       String location) {
        EmployerProfileDto employerProfileDto = new EmployerProfileDto();
        employerProfileDto.setCompanyName(companyName);
        employerProfileDto.setIndustry(industry);
        employerProfileDto.setDescription(description);
        employerProfileDto.setLocation(location);

        return employerProfileDto;
    }

    @Override
    @Transactional
    public boolean deleteEmployerProfileById(long id) {
        if(!employerProfileRepository.existsById(id)) {
            throw new EmployerProfileNotFoundException("Employer Profile with id " + id + " not found");
        }

        try {
            employerProfileRepository.deleteEmployerProfileById(id);
            return true;
        } catch (Exception e){
            log.error("Error while deleting Employer Profile with id {}", id, e);
            throw new EmployerProfilePersistenceException("Error while deleting Employer Profile with id " + id, e);
        }
    }

    @Override
    public void updateEmployerProfile(EmployerProfile employerProfile) {
        long id = employerProfile.getId();

        if(!employerProfileRepository.existsById(id)) {
            throw new EmployerProfileNotFoundException("Employer Profile with id " + id + " not found");
        }

        try {
            employerProfileRepository.save(employerProfile);
        } catch (Exception e) {
            log.error("Error while updating Employer Profile with id {}", employerProfile.getId(), e);
            throw new EmployerProfilePersistenceException("Error while updating Employer Profile with id " + id, e);
        }
    }

    @Override
    public EmployerProfile createEmployerProfile(long id, String companyName, String industry, String description, String location, String logoUrl, String publicId) {
        EmployerProfile employerProfile = new EmployerProfile();
        employerProfile.setId(id);
        employerProfile.setCompanyName(companyName);
        employerProfile.setIndustry(industry);
        employerProfile.setDescription(description);
        employerProfile.setLocation(location);
        employerProfile.setLogoUrl(logoUrl);
        employerProfile.setLogoPublicId(publicId);

        return employerProfile;
    }
}
