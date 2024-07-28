package org.anhnt24.melodyopus.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${hostUrl}")
    private String host;

    @Value("${validResetMinutes}")
    private int minutes;

    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = host + "api/password/reset-password?token=" + token;
        String subject = "Password Reset Request";
        String content = "<html>" +
                "<body>" +
                "<h3>Password Reset Request</h3>" +
                "<p>Click the button below to reset your password. The link is valid for <b style=\"color: red;\">" + minutes + " minute</b>.</p>" +
                "<a href='" + resetUrl + "' style='background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block;'>Reset Password</a>" +
                "<p>After confirmation, we will send you a temporary password.</p>" +
                "<p>If you did not request a password reset, please ignore this email.</p>" +
                "</body>" +
                "</html>";

        try {
            sendEmail(to, subject, content);
        } catch (MessagingException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void sendTemporaryPasswordEmail(String to, String tempPassword) {
        String subject = "Your New Temporary Password";
        String content = "<html><body>"
                + "<h3>Your Temporary Password</h3>"
                + "<p>Your new temporary password is: <span style=\"background-color: yellow; padding: 5px 10px;\">" + tempPassword + "</span> </p>"
                + "<p>Please use this password to log in and change your password immediately.</p>"
                + "</body></html>";

        try {
            sendEmail(to, subject, content);
        } catch (MessagingException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private void sendEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

}
