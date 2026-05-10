package com.auth.domain.Emails.Services;

public interface IEmailVerification {

    void sendVerificationEmail(String toEmail, String token);

    void  sendVerificationLoginEmail(String toEmail, String token);

}
