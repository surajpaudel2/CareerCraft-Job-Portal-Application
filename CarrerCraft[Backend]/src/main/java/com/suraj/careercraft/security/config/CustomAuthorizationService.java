package com.suraj.careercraft.security.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthorizationService {
    public boolean canAccessEmployerRegister(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_EMPLOYER"));
    }
}

