package com.suraj.careercraft.config;

import com.suraj.careercraft.model.Role;
import com.suraj.careercraft.model.enums.RoleName;
import com.suraj.careercraft.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByName(roleName)) {
                roleRepository.save(new Role(roleName));
            }
        }
    }
}
