package com.trade.repository;

import com.trade.modal.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    public VerificationCode finByUserId(Long userId);
}
