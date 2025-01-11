package com.trade.modal;

import com.trade.domain.VerificationType;
import jakarta.persistence.*;

@Entity
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String otp;

    @OneToOne
    private User user;

    private String email;

    private String mobile;

    private VerificationType verificationType;

    // No-args constructor
    public VerificationCode() {
    }

    // All-args constructor
    public VerificationCode(Long id, String otp, User user, String email, String mobile, VerificationType verificationType) {
        this.id = id;
        this.otp = otp;
        this.user = user;
        this.email = email;
        this.mobile = mobile;
        this.verificationType = verificationType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public VerificationType getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(VerificationType verificationType) {
        this.verificationType = verificationType;
    }
}
