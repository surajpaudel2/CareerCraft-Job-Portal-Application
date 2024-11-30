package com.suraj.careercraft.service.impl;

import com.suraj.careercraft.helper.CustomUser;
import com.suraj.careercraft.model.enums.AccountStatus;
import com.suraj.careercraft.model.enums.AuthProvider;
import com.suraj.careercraft.model.User;
import com.suraj.careercraft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(usernameOrEmail)
                .or(() -> userRepository.findByUsername(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email or username: " + usernameOrEmail));


        String password = user.getAuthProvider() == AuthProvider.LOCAL ? user.getPassword() : "";
        String activeStatus = user.getAccountStatus() == AccountStatus.ACTIVE ? "ACTIVE" : "INACTIVE";

        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());



        return new CustomUser(user.getUsername(), user.getEmail(), password, authorities, activeStatus);
    }
}
