package com.trade.repository;

import com.trade.modal.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken, String> {
    ForgotPasswordToken findByUserId(Long userId);
}
