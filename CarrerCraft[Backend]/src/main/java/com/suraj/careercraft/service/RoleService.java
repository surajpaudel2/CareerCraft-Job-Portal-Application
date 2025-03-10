package com.suraj.careercraft.service;

import com.suraj.careercraft.model.Role;
import com.suraj.careercraft.model.enums.RoleName;

import java.util.Optional;

public interface RoleService {
    public Optional<Role> findByName(RoleName roleName);
}
