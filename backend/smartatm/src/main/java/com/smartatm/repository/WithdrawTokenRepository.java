package com.smartatm.repository;

import com.smartatm.model.WithdrawToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WithdrawTokenRepository extends JpaRepository<WithdrawToken, Long> {
    Optional<WithdrawToken> findByTokenAndUsedFalse(String token);
}


