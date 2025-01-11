package com.trade.request;

import com.trade.domain.VerificationType;

public class ForgotPasswordRequest {
    private String sendTo;
    private VerificationType verificationType;

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public VerificationType getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(VerificationType verificationType) {
        this.verificationType = verificationType;
    }
}
