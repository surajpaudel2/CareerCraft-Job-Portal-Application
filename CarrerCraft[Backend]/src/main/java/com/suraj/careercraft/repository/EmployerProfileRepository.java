package com.suraj.careercraft.repository;

import com.suraj.careercraft.model.EmployerProfile;
import com.suraj.careercraft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Long> {
    Optional<EmployerProfile> findByUser(User user);
    @Modifying
    @Query("DELETE FROM EmployerProfile ep WHERE ep.id = :id")
    void deleteEmployerProfileById(@Param("id") long id);

}
