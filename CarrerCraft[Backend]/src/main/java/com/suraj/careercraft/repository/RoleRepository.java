package com.suraj.careercraft.repository;

import com.suraj.careercraft.model.Role;
import com.suraj.careercraft.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
    boolean existsByName(RoleName name);
}
