package com.suraj.careercraft.controller;

import com.suraj.careercraft.dto.request.UserRequestDto;
import com.suraj.careercraft.dto.response.UserResponseDto;
import com.suraj.careercraft.model.User;
import com.suraj.careercraft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestParam String usernameOrEmail) {
        User user = userService.findByUsernameOrEmail(usernameOrEmail);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserRequestDto userRequestDto) {
       long id = userRequestDto.getId();
       User user = userService.findById(id);

       String fullName = userRequestDto.getFullName();
       String username = userRequestDto.getUsername();
       String email = userRequestDto.getEmail();

       user.setFullName(fullName);
       user.setUsername(username);
       user.setEmail(email);
       userService.updateUser(user);

        UserResponseDto userResponse = userService.createUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody UserRequestDto userRequestDto) {
        long id = userRequestDto.getId();
        User user = userService.findById(id);
        userService.deleteUser(user);
        UserResponseDto userResponse = userService.createUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }
}

