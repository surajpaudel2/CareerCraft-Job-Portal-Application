package com.suraj.careercraft.dto.response;

import com.suraj.careercraft.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private boolean success;
    private String activeStatus;
    private User user;
    private String token;
}
