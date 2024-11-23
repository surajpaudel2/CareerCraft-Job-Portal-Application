package com.suraj.careercraft.service;

import com.suraj.careercraft.dto.request.ForgotPasswordRequestDto;
import com.suraj.careercraft.dto.request.ForgotPasswordRequestDto;
import com.suraj.careercraft.dto.request.OtpVerificationRequestDto;
import com.suraj.careercraft.dto.request.OtpVerificationRequestDto;
import org.springframework.http.ResponseEntity;

public interface ForgotPasswordService {

    ResponseEntity<?> initiateForgotPassword(String usernameOrEmail);

    ResponseEntity<?> verifyForgotPasswordOtp(OtpVerificationRequestDto otpVerificationRequest);

    ResponseEntity<?> changePassword(ForgotPasswordRequestDto forgotPasswordRequest);
}
