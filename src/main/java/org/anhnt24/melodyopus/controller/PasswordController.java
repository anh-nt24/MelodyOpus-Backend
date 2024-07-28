package org.anhnt24.melodyopus.controller;

import org.anhnt24.melodyopus.dto.ForgotPasswordDTO;
import org.anhnt24.melodyopus.service.PasswordService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/password")
public class PasswordController {
    @Autowired
    private PasswordService passwordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO dto) {
        try {
            passwordService.initiatePasswordReset(dto.getEmail());
            return ResponseEntity.ok("Password reset email sent. Please check your email.");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> showResetPasswordPage(
            @RequestParam("token") String token
    ) {
        try {
            passwordService.resetPassword(token);
            String htmlResponse = "<html><body><script>window.close();</script></body></html>";
            return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(htmlResponse);
        } catch (ServiceException e) {
            String errorMessage = e.getMessage();
            String htmlResponse = "<html><body><p>" + errorMessage + "</p><script>setTimeout(function(){ window.close(); }, 3000);</script></body></html>";
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_HTML).body(htmlResponse);
        }
    }
}
