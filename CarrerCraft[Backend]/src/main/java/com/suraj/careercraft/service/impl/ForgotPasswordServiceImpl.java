package com.suraj.careercraft.service.impl;

import com.suraj.careercraft.dto.request.ForgotPasswordRequestDto;
import com.suraj.careercraft.dto.request.OtpVerificationRequestDto;
import com.suraj.careercraft.dto.response.RegisterResponseDto;
import com.suraj.careercraft.model.enums.OtpPurpose;
import com.suraj.careercraft.model.User;
import com.suraj.careercraft.service.EmailService;
import com.suraj.careercraft.service.ForgotPasswordService;
import com.suraj.careercraft.service.OtpService;
import com.suraj.careercraft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;

    @Autowired
    public ForgotPasswordServiceImpl(UserService userService, OtpService otpService, EmailService emailService) {
        this.userService = userService;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @Override
    public ResponseEntity<?> initiateForgotPassword(String usernameOrEmail) {
        String otpMessage = "If an account with this email exists, we’ve sent password reset instructions to your email. Please check your inbox.";

        if (!userService.userExistsByUsername(usernameOrEmail) && !userService.userExistsByEmail(usernameOrEmail)) {
            return new ResponseEntity<>(otpMessage, HttpStatus.OK);
        }

        User user = userService.findByUsernameOrEmail(usernameOrEmail);
        String email = user.getEmail();
        String otpCode = otpService.generateAndStoreOtp(email, OtpPurpose.FORGOT_PASSWORD_CONFIRMATION);
        emailService.sendOtpEmail(email, otpCode, "");

        return new ResponseEntity<>(otpMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> verifyForgotPasswordOtp(OtpVerificationRequestDto otpVerificationRequest) {
        String otp = otpVerificationRequest.getOtp();
        String email = otpVerificationRequest.getEmail();

        boolean isCorrect = otpService.verifyOtp(otp, email, OtpPurpose.FORGOT_PASSWORD_CONFIRMATION);
        if (!isCorrect) {
            String message = "OTP verification failed. Please ensure you’ve entered the correct OTP. If the OTP has expired, request a new one and try again.";
            return new ResponseEntity<>(new RegisterResponseDto(false, message), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Otp Verified Successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> changePassword(ForgotPasswordRequestDto forgotPasswordRequest) {
        String password = forgotPasswordRequest.getPassword();
        String confirmPassword = forgotPasswordRequest.getConfirmPassword();

        if (!password.equals(confirmPassword)) {
            String message = "The passwords you entered do not match. Please make sure both fields contain the same password.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        String usernameOrEmail = forgotPasswordRequest.getUsernameOrEmail();
        User user = userService.findByUsernameOrEmail(usernameOrEmail);
        user.setPassword(password);
        userService.saveUser(user);

        return new ResponseEntity<>("Password changed successfully.", HttpStatus.OK);
    }
}
