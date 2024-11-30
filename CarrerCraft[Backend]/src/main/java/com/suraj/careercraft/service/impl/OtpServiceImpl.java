package com.suraj.careercraft.service.impl;

import com.suraj.careercraft.exceptions.UserNotFoundException;
import com.suraj.careercraft.model.Otp;
import com.suraj.careercraft.model.enums.OtpPurpose;
import com.suraj.careercraft.model.User;
import com.suraj.careercraft.repository.OtpRepository;
import com.suraj.careercraft.repository.UserRepository;
import com.suraj.careercraft.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000;

    @Autowired
    public OtpServiceImpl(OtpRepository otpRepository, UserRepository userRepository) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String generateAndStoreOtp(String email, OtpPurpose otpPurpose) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        if (otpRepository.existsByUserIdAndOtpPurpose(user.getId(), otpPurpose)) {
            otpRepository.deleteByUserIdAndOtpPurpose(user.getId(), otpPurpose);
        }

        String otpCode;
        do {
            otpCode = generateOtp();
        } while (otpRepository.existsByOtpCode(otpCode));


        long expirationTime = System.currentTimeMillis() + OTP_VALID_DURATION;

        Otp otp = new Otp();
        otp.setOtpCode(otpCode);
        otp.setExpirationTime(expirationTime);
        otp.setUser(user);
        otp.setOtpPurpose(otpPurpose);

        otpRepository.save(otp);

        return otpCode;
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    @Override
    public boolean verifyOtp(String otpCode, String email, OtpPurpose otpPurpose) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        Optional<Otp> optionalOtp = otpRepository.findByotpCode(otpCode);

        if (optionalOtp.isEmpty()) {
            return false;
        }

        Otp otp = optionalOtp.get();
        if (System.currentTimeMillis() > otp.getExpirationTime() || otp.getOtpPurpose() != otpPurpose) {
            return false;
        }

        otpRepository.delete(otp);

        return true;
    }

}
