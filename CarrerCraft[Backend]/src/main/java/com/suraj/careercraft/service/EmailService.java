package com.suraj.careercraft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender1) {
        this.mailSender = mailSender1;
    }

    public void sendOtpEmail(String toEmail, String otp, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("CareerCraft");
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Your OTP for Registration");
        simpleMailMessage.setText("Your OTP is: " + otp + "\nPlease enter this OTP to complete your registration.");

        mailSender.send(simpleMailMessage);
    }
}
