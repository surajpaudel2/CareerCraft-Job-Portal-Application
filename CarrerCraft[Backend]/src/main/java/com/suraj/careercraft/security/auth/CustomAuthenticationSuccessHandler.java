package com.suraj.careercraft.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suraj.careercraft.dto.response.LoginResponseDto;
import com.suraj.careercraft.helper.CustomUser;
import com.suraj.careercraft.dto.response.LoginResponseDto;
import com.suraj.careercraft.model.AccountStatus;
import com.suraj.careercraft.model.AuthProvider;
import com.suraj.careercraft.model.User;
import com.suraj.careercraft.security.jwt.JwtTokenUtil;
import com.suraj.careercraft.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private static final Logger log = LogManager.getLogger(CustomAuthenticationSuccessHandler.class);

    @Autowired
    public CustomAuthenticationSuccessHandler(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            CustomUser customUser = (CustomUser) authentication.getPrincipal();
            String username = customUser.getUsername();
            String jwtToken = jwtTokenUtil.generateTokenFromEmail(username);
            User user = userService.findByUsernameOrEmail(username);
            LoginResponseDto loginResponseDto = new LoginResponseDto(true, customUser.getActiveStatus(), user,
                    jwtToken);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            log.info("Form Login Success");
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(loginResponseDto));
        }

    }

//    ------------MOdified Code ----------------

//    private LoginResponseDto handleOauthLogin(Authentication authentication) {
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        String email = oAuth2User.getAttribute("email");
//
//        log.info("Oauth2 user email : {}", email);
//
//        if (!userService.userExistsByEmail(email)) {
//            String fullName = oAuth2User.getAttribute("name");
//            String tempuserName = email.split("@")[0];
//            String userName = generateUserName(tempuserName);
//
//            User user = new User();
//            user.setFullName(fullName);
//            user.setUsername(userName);
//            user.setEmail(email);
//            user.setAccountStatus(AccountStatus.ACTIVE);
//            user.setAuthProvider(findAuthProvider(authentication));
//
//            userService.saveOauthUser(user);
//        }
//
//        String jwtToken = jwtTokenUtil.generateTokenFromEmail(email);
//        return new LoginResponseDto(true, "Active", "OAuth2 Login Success", jwtToken);
//    }
//    private AuthProvider findAuthProvider(Authentication authentication) {
//        String temp = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId().toUpperCase();
//        return "FACEBOOK".equals(temp) ? AuthProvider.FACEBOOK : AuthProvider.GOOGLE;
//    }

//    private String generateUserName(String baseUserName) {
//        String tempuserName = baseUserName;
//        while (userService.userExistsByUsername(tempuserName)) {
//            String uuidString = UUID.randomUUID().toString().substring(0, 4);
//            tempuserName = baseUserName + uuidString;
//        }
//        return tempuserName;
//    }
}
