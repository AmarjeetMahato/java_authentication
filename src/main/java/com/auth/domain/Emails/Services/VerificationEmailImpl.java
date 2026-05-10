package com.auth.domain.Emails.Services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationEmailImpl implements  IEmailVerification {

    private  final JavaMailSender javaMailSender;

    @Value("${application.frontend.url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendVerificationEmail(String toEmail, String token) {
        try {

            String verificationLink =
                    frontendUrl +
                            "/verify-email?token=" + token;

            MimeMessage mimeMessage =
                    javaMailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(senderEmail);

            helper.setTo(toEmail);

            helper.setSubject("Verify Your Email");

            helper.setText(buildVerificationEmailTemplate(
                    verificationLink
            ), true);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException | MailException ex) {

            throw new RuntimeException(
                    "Failed to send verification email"
            );
        }

    }

    @Override
    public void sendVerificationLoginEmail(String toEmail, String token) {

    }


    /**
     * Verification Email HTML Template
     */
    private String buildVerificationEmailTemplate(String verificationLink) {

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                  <title>Verify Your Email</title>
                  <style>
                    @import url('https://fonts.googleapis.com/css2?family=Playfair+Display:wght@600&family=DM+Sans:wght@300;400;500&display=swap');
                  </style>
                </head>
                <body style="
                  margin: 0;
                  padding: 0;
                  background-color: #0d0f14;
                  font-family: 'DM Sans', sans-serif;
                  -webkit-font-smoothing: antialiased;
                ">
                
                  <!-- Outer wrapper -->
                  <table width="100%" cellpadding="0" cellspacing="0" border="0"
                         style="background-color: #0d0f14; padding: 48px 16px;">
                    <tr>
                      <td align="center">
                
                        <!-- Card -->
                        <table width="600" cellpadding="0" cellspacing="0" border="0"
                               style="
                                 max-width: 600px;
                                 width: 100%;
                                 background: linear-gradient(160deg, #13151c 0%, #0f1118 100%);
                                 border-radius: 20px;
                                 border: 1px solid rgba(255,255,255,0.06);
                                 overflow: hidden;
                                 box-shadow: 0 40px 80px rgba(0,0,0,0.6);
                               ">
                
                          <!-- Top accent bar -->
                          <tr>
                            <td style="
                              background: linear-gradient(90deg, #6366f1 0%, #a78bfa 50%, #38bdf8 100%);
                              height: 4px;
                              font-size: 0;
                              line-height: 0;
                            ">&nbsp;</td>
                          </tr>
                
                          <!-- Header section -->
                          <tr>
                            <td style="padding: 52px 52px 0 52px; text-align: center;">
                
                              <!-- Icon badge -->
                              <div style="
                                display: inline-block;
                                background: rgba(99,102,241,0.12);
                                border: 1px solid rgba(99,102,241,0.3);
                                border-radius: 50%;
                                width: 72px;
                                height: 72px;
                                line-height: 72px;
                                text-align: center;
                                margin-bottom: 28px;
                              ">
                                <span style="font-size: 32px; line-height: 72px;">✉</span>
                              </div>
                
                              <h1 style="
                                font-family: 'Playfair Display', Georgia, serif;
                                font-size: 32px;
                                font-weight: 600;
                                color: #f1f5f9;
                                margin: 0 0 12px 0;
                                letter-spacing: -0.5px;
                                line-height: 1.2;
                              ">Verify your email address</h1>
                
                              <p style="
                                font-size: 15px;
                                color: #64748b;
                                font-weight: 300;
                                margin: 0;
                                line-height: 1.7;
                                letter-spacing: 0.1px;
                              ">You're almost there. One tap to confirm<br/>and you're all set.</p>
                
                            </td>
                          </tr>
                
                          <!-- Divider -->
                          <tr>
                            <td style="padding: 36px 52px 0 52px;">
                              <div style="
                                height: 1px;
                                background: linear-gradient(90deg, transparent, rgba(255,255,255,0.06), transparent);
                              "></div>
                            </td>
                          </tr>
                
                          <!-- Body text -->
                          <tr>
                            <td style="padding: 36px 52px 0 52px;">
                              <p style="
                                font-size: 15px;
                                color: #94a3b8;
                                margin: 0;
                                line-height: 1.8;
                                font-weight: 300;
                              ">
                                Hi there,<br/><br/>
                                Thank you for creating an account. To complete your registration and
                                activate all features, please verify your email address by clicking
                                the button below.
                              </p>
                            </td>
                          </tr>
                
                          <!-- CTA Button -->
                          <tr>
                            <td style="padding: 36px 52px 0 52px; text-align: center;">
                              <table cellpadding="0" cellspacing="0" border="0" style="margin: auto;">
                                <tr>
                                  <td style="
                                    background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                                    border-radius: 12px;
                                    box-shadow: 0 8px 32px rgba(99,102,241,0.4), 0 2px 8px rgba(0,0,0,0.3);
                                  ">
                                    <a href="%s" style="
                                      display: inline-block;
                                      padding: 16px 44px;
                                      font-family: 'DM Sans', sans-serif;
                                      font-size: 15px;
                                      font-weight: 500;
                                      color: #ffffff;
                                      text-decoration: none;
                                      letter-spacing: 0.3px;
                                      border-radius: 12px;
                                    ">Verify My Email &rarr;</a>
                                  </td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                
                          <!-- Fallback link -->
                          <tr>
                            <td style="padding: 24px 52px 0 52px; text-align: center;">
                              <p style="
                                font-size: 12px;
                                color: #475569;
                                margin: 0;
                                line-height: 1.7;
                              ">
                                Button not working? Copy and paste this link into your browser:<br/>
                                <span style="
                                  color: #6366f1;
                                  word-break: break-all;
                                  font-size: 11px;
                                ">%s</span>
                              </p>
                            </td>
                          </tr>
                
                          <!-- Divider -->
                          <tr>
                            <td style="padding: 36px 52px 0 52px;">
                              <div style="
                                height: 1px;
                                background: linear-gradient(90deg, transparent, rgba(255,255,255,0.06), transparent);
                              "></div>
                            </td>
                          </tr>
                
                          <!-- Expiry warning -->
                          <tr>
                            <td style="padding: 28px 52px 0 52px;">
                              <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr>
                                  <td style="
                                    background: rgba(251,191,36,0.07);
                                    border: 1px solid rgba(251,191,36,0.18);
                                    border-radius: 10px;
                                    padding: 14px 18px;
                                  ">
                                    <p style="
                                      margin: 0;
                                      font-size: 13px;
                                      color: #fbbf24;
                                      font-weight: 400;
                                      letter-spacing: 0.1px;
                                    ">⏳ &nbsp;This link expires in <strong>15 minutes</strong>. If it expires, you can request a new one from the login page.</p>
                                  </td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                
                          <!-- Ignore notice -->
                          <tr>
                            <td style="padding: 24px 52px 0 52px;">
                              <p style="
                                font-size: 13px;
                                color: #475569;
                                margin: 0;
                                line-height: 1.7;
                                font-weight: 300;
                              ">
                                If you didn't create an account, you can safely ignore this email.
                                No action is required.
                              </p>
                            </td>
                          </tr>
                
                          <!-- Footer -->
                          <tr>
                            <td style="padding: 40px 52px 48px 52px; text-align: center;">
                              <p style="
                                font-size: 12px;
                                color: #334155;
                                margin: 0;
                                line-height: 1.8;
                              ">
                                Sent with care by <strong style="color: #475569;">YourApp</strong><br/>
                                123 Product Lane, San Francisco, CA 94103<br/>
                                <a href="#" style="color: #6366f1; text-decoration: none;">Unsubscribe</a>
                                &nbsp;&middot;&nbsp;
                                <a href="#" style="color: #6366f1; text-decoration: none;">Privacy Policy</a>
                              </p>
                            </td>
                          </tr>
                
                        </table>
                        <!-- /Card -->
                
                      </td>
                    </tr>
                  </table>
                
                </body>
                </html>
                """;


    }


}
