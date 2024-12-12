package com.suraj.careercraft.controller;

import com.suraj.careercraft.dto.request.EmployerProfileRequestDto;
import com.suraj.careercraft.dto.EmployerProfileDto;
import com.suraj.careercraft.dto.response.RegisterResponseDto;
import com.suraj.careercraft.exceptions.UserPersistenceException;
import com.suraj.careercraft.model.EmployerProfile;
import com.suraj.careercraft.model.Role;
import com.suraj.careercraft.model.enums.RoleName;
import com.suraj.careercraft.model.User;
import com.suraj.careercraft.repository.RoleRepository;
import com.suraj.careercraft.repository.UserRepository;
import com.suraj.careercraft.service.CloudinaryService;
import com.suraj.careercraft.service.EmployerProfileService;
import com.suraj.careercraft.service.UserService;
import com.suraj.careercraft.service.registration.EmployerProfileRegistrationService;
import com.suraj.careercraft.service.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/employer")
public class EmployerProfileController {
    private final CloudinaryService cloudinaryService;
    private final Validator validator;
    private final RegistrationService registrationService;
    private final UserService userService;
    private final EmployerProfileService employerProfileService;
    private final EmployerProfileRegistrationService employerProfileRegistrationService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public EmployerProfileController(CloudinaryService cloudinaryService, Validator validator, RegistrationService registrationService, UserService userService, EmployerProfileService employerProfileService, EmployerProfileRegistrationService employerProfileRegistrationService, RoleRepository roleRepository, UserRepository userRepository) {
        this.cloudinaryService = cloudinaryService;
        this.validator = validator;
        this.registrationService = registrationService;
        this.userService = userService;
        this.employerProfileService = employerProfileService;
        this.employerProfileRegistrationService = employerProfileRegistrationService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    //  --------------Registration of the Employer Profile begins here ---------------------------
    @PostMapping("/register")
    public ResponseEntity<?> registerEmployerProfile(
            @RequestParam String usernameOrEmail,
            @RequestParam String companyName,
            @RequestParam String industry,
            @RequestParam String description,
            @RequestParam String location,
            @RequestParam(required = false) MultipartFile logoFile) {

        // Create and validate EmployerProfileRequestDto
        EmployerProfileRequestDto profileRequest = employerProfileRegistrationService
                .createEmployerProfileRequest(usernameOrEmail, companyName, industry, description, location);

        BindingResult bindingResult = new BeanPropertyBindingResult(profileRequest, "profileRequest");
        validator.validate(profileRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            String messages = registrationService.getValidationErrors(bindingResult);
            return ResponseEntity.badRequest().body(messages);
        }

        // Fetch user and role (cached for efficiency if frequently used)
        User user = userService.findByUsernameOrEmail(usernameOrEmail);
        Role employerRole = roleRepository.findByName(RoleName.ROLE_EMPLOYER)
                .orElseThrow(() -> new UserPersistenceException("Problem while finding the role."));

        // Handle logo upload if provided
        String logoUrl = null, logoPublicId = null;
        if (logoFile != null && !logoFile.isEmpty()) {
            Map<String, String> map = cloudinaryService.uploadImage(logoFile);
            logoUrl = map.get("url");
            logoPublicId = map.get("publicId");
        }

        // Create and save employer profile
        EmployerProfile employerProfile = employerProfileRegistrationService
                .createEmployerProfile(user, companyName, industry, description, location, logoUrl, logoPublicId);
        EmployerProfile savedProfile = employerProfileService.saveEmployerProfile(employerProfile);

        // Check if profile saved successfully, then update user roles and save user
        if (savedProfile != null) {
            user.getRoles().add(employerRole);
            userRepository.save(user);  // This saves both the user and the added role

            RegisterResponseDto registerResponse = new RegisterResponseDto(true, "Employer Profile Registered");
            return new ResponseEntity<>(registerResponse, HttpStatus.OK);
        }

        RegisterResponseDto registerResponse = new RegisterResponseDto(false, "Employer Profile registration failed. Please try again later");
        return new ResponseEntity<>(registerResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//  --------------Registration of the Employer Profile ends here ---------------------------

//    -------------Employer Profile Updating starts -----------------------------

    @PutMapping("/update")
    public ResponseEntity<?> updateEmployerProfile(@RequestParam long id, @RequestParam String companyName,
                                                   @RequestParam String industry, @RequestParam String description,
                                                   @RequestParam String location,
                                                   @RequestParam String logoUrl,
                                                   @RequestParam(required = false) MultipartFile logoFile) {

        EmployerProfileDto employerProfileDto = employerProfileService.createEmployerProfileDto(companyName, industry,
                description, location);
        BindingResult bindingResult = new BeanPropertyBindingResult(employerProfileDto, "employerProfileDto");
        validator.validate(employerProfileDto, bindingResult);

        if (bindingResult.hasErrors()) {
            String messages = registrationService.getValidationErrors(bindingResult);
            return ResponseEntity.badRequest().body(messages);
        }

//       ----------
        /*
            1. Assume user wants to have previous multipart file -> Logo url will not be empty.
            2. Assume user wants to delete the file
            3. Assume user wants to have new logo file.

         */

        EmployerProfile employerProfile = employerProfileService.getEmployerProfileById(id);
        String publicId = employerProfile.getLogoPublicId();
        if (!logoUrl.isEmpty() || !logoUrl.isBlank()) {
//          user just wants to change the other things but the logo he wants to have it as it is.
//                        update the thing.
            EmployerProfile empProfile = employerProfileService.createEmployerProfile(id, companyName, industry,
                    description, location, logoUrl, publicId);
            employerProfileService.saveEmployerProfile(employerProfile);
            return new ResponseEntity<>(employerProfile, HttpStatus.OK);
        }

//        Since, user wants to change the logo, either he/she wants to leave it blank or want to have new one.
        /*
            1. Delete in cloud by public id.
            2. If multipart file is empty save to database.
         */
        cloudinaryService.deleteFile(publicId, "image");
        if(logoFile == null || logoFile.isEmpty()) {
//            set public id to null and url to null as well
            logoUrl = null;
            publicId = null;

            EmployerProfile empProfile = employerProfileService.createEmployerProfile(id, companyName, industry,
                    description, location, logoUrl, publicId);
            employerProfileService.saveEmployerProfile(employerProfile);
            return new ResponseEntity<>(employerProfile, HttpStatus.OK);
        }

//        Now user wants to change the existing logo file.
        Map<String, String> map = cloudinaryService.uploadImage(logoFile);
        logoUrl = map.get("url");
        publicId = map.get("publicId");

        EmployerProfile empProfile = employerProfileService.createEmployerProfile(id, companyName, industry,
                description, location, logoUrl, publicId);
        employerProfileService.saveEmployerProfile(employerProfile);
        return new ResponseEntity<>(employerProfile, HttpStatus.OK);
    }

//    -------------Employer Profile Updating ends -------------------------------

//    ------------Get Employer Profile Details starts -----------------------------

    @GetMapping("/profile")
    public ResponseEntity<EmployerProfile> getEmployerProfile(@RequestParam String usernameOrEmail) {
        EmployerProfile employerProfile = employerProfileService.getEmployerProfileByUsernameOrEmail(usernameOrEmail);
        System.out.println("Posted jobs list : -----------------------\n");
        System.out.println(employerProfile.getPostedJobs());
        return new ResponseEntity<>(employerProfile, HttpStatus.OK);
    }

//    ------------Getting Employer Profile Details ends ------------------------------

    //    ------------ Deleting Employer Profile Starts -------------------------------------
    @PostMapping("/delete")
    public ResponseEntity<?> deleteEmployerProfile(@RequestParam long id) {
        if (employerProfileService.deleteEmployerProfileById(id)) {
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    --------- Deleting Employer Profile Ends --------------------------------------

}
