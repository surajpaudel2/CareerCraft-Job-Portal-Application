package com.suraj.careercraft.service.impl;

import com.suraj.careercraft.model.Role;
import com.suraj.careercraft.model.enums.RoleName;
import com.suraj.careercraft.repository.RoleRepository;
import com.suraj.careercraft.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RoleRepository roleRepository1) {
        this.roleRepository = roleRepository1;
    }

    public Optional<Role> findByName(RoleName roleName) {
        return roleRepository.findByName(roleName);
    }
}
