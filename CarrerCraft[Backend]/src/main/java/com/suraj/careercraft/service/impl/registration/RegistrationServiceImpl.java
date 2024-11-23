package com.suraj.careercraft.service.impl.registration;

import com.suraj.careercraft.dto.request.RegisterRequestDto;
import com.suraj.careercraft.dto.response.RegisterResponseDto;
import com.suraj.careercraft.exceptions.UserPersistenceException;
import com.suraj.careercraft.model.*;
import com.suraj.careercraft.repository.RoleRepository;
import com.suraj.careercraft.service.EmailService;
import com.suraj.careercraft.service.OtpService;
import com.suraj.careercraft.service.registration.RegistrationService;
import com.suraj.careercraft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final RoleRepository roleRepository;

    @Autowired
    public RegistrationServiceImpl(UserService userService, OtpService otpService, EmailService emailService, RoleRepository roleRepository) {
        this.userService = userService;
        this.otpService = otpService;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
    }

    @Override
    public String getValidationErrors(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder("Validation errors: ");
        bindingResult.getFieldErrors().forEach(error ->
                errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        return errorMessage.toString();
    }

    @Override
    public boolean checkUserExists(String userName, String email) {
        return userService.userExistsByEmail(email) || userService.userExistsByUsername(userName);
    }

    @Override
    public User createUser(RegisterRequestDto registerRequest) {
        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setAccountStatus(AccountStatus.PENDING);
        setRoleAsJobSeeker(user);
        user = userService.saveUser(user);
        return user;
    }

    @Override
    public void sendOtpToUser(String email) {
        String otpCode = otpService.generateAndStoreOtp(email, OtpPurpose.REGISTRATION_CONFIRMATION);
        emailService.sendOtpEmail(email, otpCode, "");
    }

    @Override
    public ResponseEntity<RegisterResponseDto> createErrorResponse(String message, HttpStatus status) {
        RegisterResponseDto response = new RegisterResponseDto(false, message);
        return new ResponseEntity<>(response, status);
    }

    @Override
    public ResponseEntity<RegisterResponseDto> createSuccessResponse(String message) {
        RegisterResponseDto response = new RegisterResponseDto(true, message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RegisterResponseDto> verifyRegistrationOtp(String otp, String email) {
        boolean isCorrect = otpService.verifyOtp(otp, email, OtpPurpose.REGISTRATION_CONFIRMATION);

        if (!isCorrect) {
            String message = "OTP verification failed. Please ensure you’ve entered the correct OTP. If the OTP has expired, request a new one and try again.";
            return createErrorResponse(message, HttpStatus.BAD_REQUEST);
        }

        User user = userService.findUserByEmail(email);
        user.setAccountStatus(AccountStatus.ACTIVE);
        userService.saveUser(user);

        String message = "Congratulations! Your account has been successfully activated. You can now log in and start exploring.";
        return createSuccessResponse(message);
    }

    @Override
    public User registerGoogleUser(String fullName, String email) {
        String tempuserName = email.split("@")[0];
        String userName = generateUserName(tempuserName);

        User user = new User();
        user.setFullName(fullName);
        user.setUsername(userName);
        user.setEmail(email);
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setAuthProvider(AuthProvider.GOOGLE);
        setRoleAsJobSeeker(user);

        return userService.saveOauthUser(user);
    }

    private String generateUserName(String baseUserName) {
        String tempuserName = baseUserName;
        while (userService.userExistsByUsername(tempuserName)) {
            String uuidString = UUID.randomUUID().toString().substring(0, 4);
            tempuserName = baseUserName + uuidString;
        }
        return tempuserName;
    }

    private void setRoleAsJobSeeker(User user) {
        Role userRole = roleRepository.findByName(RoleName.ROLE_JOB_SEEKER)
                .orElseThrow(() -> new UserPersistenceException("Problem while finding the role."));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
    }
}
