package com.trade.service;

import com.trade.domain.VerificationType;
import com.trade.modal.User;
import com.trade.modal.VerificationCode;

public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode  getVerificationCodeByUser(Long userId);



    void deleteVerificationCodeById(VerificationCode verificationCode);
}
