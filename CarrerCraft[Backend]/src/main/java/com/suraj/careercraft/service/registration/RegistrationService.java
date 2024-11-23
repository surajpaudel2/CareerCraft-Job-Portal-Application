package com.suraj.careercraft.service.registration;

import com.suraj.careercraft.dto.request.RegisterRequestDto;
import com.suraj.careercraft.dto.request.RegisterRequestDto;
import com.suraj.careercraft.dto.response.RegisterResponseDto;
import com.suraj.careercraft.dto.response.RegisterResponseDto;
import com.suraj.careercraft.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface RegistrationService {

    String getValidationErrors(BindingResult bindingResult);

    boolean checkUserExists(String userName, String email);

    User createUser(RegisterRequestDto registerRequest);

    void sendOtpToUser(String email);

    ResponseEntity<RegisterResponseDto> createErrorResponse(String message, HttpStatus status);

    ResponseEntity<RegisterResponseDto> createSuccessResponse(String message);

    ResponseEntity<RegisterResponseDto> verifyRegistrationOtp(String otp, String email);

    User registerGoogleUser(String fullName, String email);
}
