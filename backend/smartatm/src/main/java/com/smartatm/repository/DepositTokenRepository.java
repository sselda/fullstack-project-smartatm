package com.smartatm.repository;

import com.smartatm.model.DepositToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepositTokenRepository extends JpaRepository<DepositToken, Long> {
    Optional<DepositToken> findByTokenAndUsedFalse(String token);
}
