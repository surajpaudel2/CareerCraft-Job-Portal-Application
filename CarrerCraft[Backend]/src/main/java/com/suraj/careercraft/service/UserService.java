package com.suraj.careercraft.service;

import com.suraj.careercraft.dto.response.UserResponseDto;
import com.suraj.careercraft.model.User;

public interface UserService {

    User findById(long id);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    boolean userExistsByUsername(String username);

    boolean userExistsByEmail(String email);

    User saveUser(User user);

    User saveOauthUser(User user);

     User findByUsernameOrEmail(String usernameOrEmail);

    User updateUser(User user);

    void deleteUser(User user);

    void deleteByEmail(String email);

    void deleteByUsername(String username);

    UserResponseDto createUserResponse(User user);
}
