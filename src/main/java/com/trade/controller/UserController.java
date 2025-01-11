package com.trade.controller;

import com.trade.request.ForgotPasswordRequest;
import com.trade.domain.VerificationType;
import com.trade.modal.ForgotPasswordToken;
import com.trade.modal.User;
import com.trade.modal.VerificationCode;
import com.trade.request.ResetPasswordRequest;
import com.trade.response.ApiResponse;
import com.trade.response.AuthResponse;
import com.trade.service.EmailService;
import com.trade.service.ForgotPasswordService;
import com.trade.service.UserService;
import com.trade.service.VerificationCodeService;
import com.trade.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;
    @Autowired
    private VerificationCodeService verificationCodeService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(
            @RequestHeader("Authorization") String jwt
            , @PathVariable VerificationType verificationType) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        if (verificationCode == null) {
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }
        if (verificationType.equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }
        return new ResponseEntity<>("verification otp send successfully", HttpStatus.OK);
    }

    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @PathVariable String otp,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL) ?
                verificationCode.getEmail() : verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);

        if (isVerified) {
            User updatedUser = userService.enabledTwoFactorAuthentication(
                    verificationCode.getVerificationType(), sendTo, user);
            verificationCodeService.deleteVerificationCodeById(verificationCode);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("Wrong otp");
    }

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(
            @RequestBody ForgotPasswordRequest request) throws Exception {
        User user = userService.findUserByEmail(request.getSendTo());
        String otp = OtpUtils.generateOTP();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());
        if (token == null) {
            token = forgotPasswordService.createToke(user, id, otp, request.getVerificationType(), request.getSendTo());
        }
        if (request.getVerificationType().equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(), token.getOtp());
        }

        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("password reset otp sent successfully.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String id,
            @RequestBody ResetPasswordRequest request,
            @RequestHeader("Authorization") String jwt) throws Exception {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);
        boolean isVerified = forgotPasswordToken.getOtp().equals(request.getOtp());
        if (isVerified) {
            userService.updatePassword(forgotPasswordToken.getUser(), request.getPassword());
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("password update Successfully.");
            return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
        }
        throw new Exception("wrong otp");
    }
}
