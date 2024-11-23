package com.suraj.careercraft.service;

import com.suraj.careercraft.model.OtpPurpose;

public interface OtpService {
    public String generateAndStoreOtp(String email, OtpPurpose purpose);
    public boolean verifyOtp(String otp, String email, OtpPurpose otpPurpose);
}

