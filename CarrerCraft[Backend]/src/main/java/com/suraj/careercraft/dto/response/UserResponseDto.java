package com.suraj.careercraft.dto.response;

import com.suraj.careercraft.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private long id;
    private String username;
    private String email;
    private String fullName;
    private String accountStatus;
    private final Set<Role> roles = new HashSet<>();
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
