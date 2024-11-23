package com.suraj.careercraft.helper;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUser extends User {
    @ToString.Exclude
    private final String activeStatus;
    private final String email;

    public CustomUser(String username, String email, String password,
                      Collection<? extends GrantedAuthority> authorities, String activeStatus) {
        super(username, password, authorities);
        this.activeStatus = activeStatus;
        this.email = email;
    }

}
