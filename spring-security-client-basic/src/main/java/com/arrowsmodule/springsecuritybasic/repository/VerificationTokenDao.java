package com.arrowsmodule.springsecuritybasic.repository;

import com.arrowsmodule.springsecuritybasic.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationTokenDao extends JpaRepository<VerificationToken,Long> {
    VerificationToken findByToken(String token);
    @Query(value = "SELECT * FROM Verification_Token WHERE user_id = :userId",nativeQuery = true)
    List<VerificationToken> findByUser(@Param("userId") Long userId);
}