package com.trade.service;

import com.trade.domain.VerificationType;
import com.trade.modal.ForgotPasswordToken;
import com.trade.modal.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToke(User user,
                                   String id, String otp,
                                   VerificationType verificationType,
                                   String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);
}
