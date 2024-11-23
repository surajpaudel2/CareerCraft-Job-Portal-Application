package com.suraj.careercraft.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.suraj.careercraft.dto.request.ForgotPasswordRequestDto;
import com.suraj.careercraft.dto.request.JobSearchRequestDto;
import com.suraj.careercraft.dto.request.OtpVerificationRequestDto;
import com.suraj.careercraft.dto.response.JwtResponseDto;
import com.suraj.careercraft.dto.response.LoginResponseDto;
import com.suraj.careercraft.dto.response.RegisterResponseDto;
import com.suraj.careercraft.model.*;
import com.suraj.careercraft.dto.request.RegisterRequestDto;
import com.suraj.careercraft.model.elasticsearch.JobDocument;
import com.suraj.careercraft.security.jwt.JwtTokenUtil;
import com.suraj.careercraft.service.*;
import com.suraj.careercraft.service.impl.JobServiceImpl;
import com.suraj.careercraft.service.registration.RegistrationService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger log = LogManager.getLogger(AuthController.class);
    private final RegistrationService registrationService;
    private final ForgotPasswordService forgotPasswordService;
    private final String CLIENT_ID = "516077764705-ueuep86aim4a2f20595f1p1jqc9pt689.apps.googleusercontent.com";
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final JobServiceImpl jobServiceImpl;

    @Autowired
    public AuthController(RegistrationService registrationService, ForgotPasswordService forgotPasswordService, JwtTokenUtil jwtTokenUtil, UserService userService, JobServiceImpl jobServiceImpl) {
        this.registrationService = registrationService;
        this.forgotPasswordService = forgotPasswordService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.jobServiceImpl = jobServiceImpl;
    }

//    ---------------------- OAuth Login Starts From Here-------------------------

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            User user = null;
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                user = null;
                if (!userService.userExistsByEmail(email)) {
//                    Register user if he/she is not registered
                    user = registrationService.registerGoogleUser(name, email);
                } else {
                    user = userService.findUserByEmail(email);
                }

                String jwtToken = jwtTokenUtil.generateTokenFromEmail(email);

                LoginResponseDto loginResponseDto = new LoginResponseDto(true, user.getAccountStatus().name(), user,
                        jwtToken);
                return ResponseEntity.ok(loginResponseDto);

            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token verification failed.");
            }
        } catch (Exception e) {
            log.error("Google User Failed to login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token verification failed.");
        }
    }

    //    ------------Registration Process Starts Here ----------------
    @PostMapping("/signup")
    public ResponseEntity<RegisterResponseDto> registerLocalUser(@Valid @RequestBody RegisterRequestDto registerRequest,
                                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = registrationService.getValidationErrors(bindingResult);
            return registrationService.createErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
        }

        // Check if user already exists
        String userName = registerRequest.getUsername();
        String email = registerRequest.getEmail();
        if (registrationService.checkUserExists(userName, email)) {
            String message = "Username or Email already exists, please try with different credentials.";
            return registrationService.createErrorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Create and save user
        User user = registrationService.createUser(registerRequest);
        if (user == null) {
            return registrationService.createErrorResponse("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Send OTP
        registrationService.sendOtpToUser(email);

        // Success response
        String successMessage = "User registered successfully. Please check your email to activate your account by entering the OTP sent to you.";
        return registrationService.createSuccessResponse(successMessage);
    }

    //-----------------OTP Verification Starts Here-------------------------------
    @PostMapping("/verify/reg/otp")
    public ResponseEntity<RegisterResponseDto> verifyRegistrationOtp(@RequestBody OtpVerificationRequestDto otpVerificationRequest) {
        String otp = otpVerificationRequest.getOtp();
        String email = otpVerificationRequest.getEmail();
        return registrationService.verifyRegistrationOtp(otp, email);
    }

//-----------------OTP Verification Ends Here-------------------------------

    //    ---------------Registration Process Ends Here ---------------------


// -----------------Forgot Password Implementation Starts Here-------------------------------

    @PostMapping("/forgotPass")
    public ResponseEntity<?> forgotPassword(@RequestParam String usernameOrEmail) {
        return forgotPasswordService.initiateForgotPassword(usernameOrEmail);
    }

    @PostMapping("/verify/pass/otp")
    public ResponseEntity<?> verifyPasswordOtp(@RequestBody OtpVerificationRequestDto otpVerificationRequestDto) {
        return forgotPasswordService.verifyForgotPasswordOtp(otpVerificationRequestDto);
    }

    @PostMapping("/changePass")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequest,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors: ");
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
            );
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        return forgotPasswordService.changePassword(forgotPasswordRequest);
    }

// -----------------Forgot Password Implementation Starts Here-------------------------------


//    ---------------Refresh JwtToken Code Starts here -------------------------

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Authorization").replace("Bearer ", "");

        if (jwtTokenUtil.isTokenExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token has expired");
        }

        String email = jwtTokenUtil.getClaimFromToken(refreshToken, Claims::getSubject);
        User user = userService.findByUsernameOrEmail(email); // Fetch user from the database

        String newAccessToken = jwtTokenUtil.generateTokenFromEmail(email);
        return ResponseEntity.ok(new JwtResponseDto(newAccessToken, refreshToken)); // Return the new tokens
    }


//    Remove at last just for testing


    @PostMapping("/search")
    public ResponseEntity<?> searchJob(@RequestBody JobSearchRequestDto searchRequestDto) {
        System.out.println("hey");
//        System.out.println();
        List<JobDocument> list = jobServiceImpl.searchJobs(searchRequestDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/hey")
    public void sayHi() {
        System.out.println("Say Hi");
    }
}


