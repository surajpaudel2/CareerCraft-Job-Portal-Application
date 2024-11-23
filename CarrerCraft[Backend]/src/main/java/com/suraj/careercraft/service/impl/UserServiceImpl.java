package com.suraj.careercraft.service.impl;

import com.suraj.careercraft.dto.response.UserResponseDto;
import com.suraj.careercraft.exceptions.UserNotFoundException;
import com.suraj.careercraft.exceptions.UserPersistenceException;
import com.suraj.careercraft.model.Role;
import com.suraj.careercraft.model.User;
import com.suraj.careercraft.repository.UserRepository;
import com.suraj.careercraft.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found"));
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " does not exist"));
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " does not exist"));
    }

    @Override
    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        try {
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Error while saving user with email: {}", user.getEmail(), e);
            throw new UserPersistenceException("Error while saving user", e);
        }
    }

    @Override
    public User saveOauthUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Error while saving OAuth user with email: {}", user.getEmail(), e);
            throw new UserPersistenceException("Error while saving OAuth user", e);
        }
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.getUserByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new UserNotFoundException("User with username or email: " + usernameOrEmail + " does not exist"));
    }

    @Override
    public User updateUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Error while updating user with email: {}", user.getEmail(), e);
            throw new UserPersistenceException("Error while saving user", e);
        }
    }

    @Override
    public void deleteUser(User user) {
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            log.error("Error while deleting user with email: {}", user.getEmail(), e);
            throw new UserPersistenceException("Error while deleting user", e);
        }
    }

    @Override
    public void deleteByEmail(String email) {
        try {
            userRepository.deleteByEmail(email);
        } catch (Exception e) {
            log.error("Error while deleting user with email: {}", email, e);
            throw new UserPersistenceException("Error while deleting user", e);
        }
    }

    @Override
    public void deleteByUsername(String username) {
        try {
            userRepository.deleteByUsername(username);
        } catch (Exception e) {
            log.error("Error while deleting user with email: {}", username, e);
            throw new UserPersistenceException("Error while deleting user", e);
        }
    }

    @Override
    public UserResponseDto createUserResponse(User user) {
        Long id = user.getId();
        String fullName = user.getFullName();
        String username = user.getUsername();
        String email = user.getEmail();
        String accountStatus = user.getAccountStatus().toString();
        Set<Role> roles = user.getRoles();
        Timestamp updatedAt = user.getUpdatedAt();
        Timestamp createdAt = user.getCreatedAt();

        return new UserResponseDto(id, username, email, fullName, accountStatus, createdAt, updatedAt);
    }

}
