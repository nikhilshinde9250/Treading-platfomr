package com.trade.modal;

import com.trade.domain.VerificationType;
import jakarta.persistence.*;

@Entity
public class ForgotPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne
    private User user;

    private String otp;

    private VerificationType verificationType;

    private String sendTo;

    // No-args constructor
    public ForgotPasswordToken() {
    }

    // All-args constructor
    public ForgotPasswordToken(String id, User user, String otp, VerificationType verificationType, String sendTo) {
        this.id = id;
        this.user = user;
        this.otp = otp;
        this.verificationType = verificationType;
        this.sendTo = sendTo;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public VerificationType getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(VerificationType verificationType) {
        this.verificationType = verificationType;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }
}
