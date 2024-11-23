package com.suraj.careercraft.repository;

import com.suraj.careercraft.model.Otp;
import com.suraj.careercraft.model.OtpPurpose;
import com.suraj.careercraft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    boolean existsByOtpCode(String otpCode);

    Optional<Otp> findFirstByUserOrderByExpirationTimeDesc(User user);

    Optional<Otp> findByotpCode(String otpCode);

    @Transactional(readOnly = true)
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Otp o WHERE o.user.id = :userId AND o.otpPurpose = :otpPurpose")
    boolean existsByUserIdAndOtpPurpose(@Param("userId") Long userId, @Param("otpPurpose") OtpPurpose otpPurpose);

    @Transactional
    @Modifying
    @Query("DELETE FROM Otp o WHERE o.user.id = :userId AND o.otpPurpose = :otpPurpose")
    void deleteByUserIdAndOtpPurpose(Long userId, OtpPurpose otpPurpose);
}
