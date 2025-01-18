package com.controllers;

import com.GmailService;
import com.EmailSender;
import com.EmailReader;
import com.google.api.services.gmail.Gmail;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/gmail")
public class GmailController {

    @GetMapping("/authorize")
    public ResponseEntity<String> authorizeUser(HttpServletRequest request) {
        try {
            // Generate the OAuth URL to redirect user to Google's consent screen
            String authUrl = GmailService.getAuthorizationUrl();
            return ResponseEntity.status(302).location(URI.create(authUrl)).build(); // Redirect user
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to initiate authorization: " + e.getMessage());
        }
    }

    @GetMapping("/oauth2callback")
    public ResponseEntity<Void> handleCallback(
            @RequestParam() String code,
            HttpSession session) {
        try {
            GmailService.exchangeAuthorizationCode(code, session);
            URI redirectUri = URI.create("/mailing.html"); // Redirect to mailing.html
            return ResponseEntity.status(302).location(redirectUri).build(); 
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build(); // Return error if authorization fails
        }
    }

    @GetMapping("/send")
    public ResponseEntity<String> sendEmail(
            HttpSession session,
            @RequestParam String recipient,
            @RequestParam String subject,
            @RequestParam String body) {
        try {
            Gmail gmail = GmailService.getAuthenticatedGmailService(session);
            EmailSender.sendEmail(gmail, recipient, subject, body);
            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }

    @GetMapping("/read")
    public ResponseEntity<List<EmailReader.Email>> readEmails(HttpSession session) {
        try {
            Gmail gmail = GmailService.getAuthenticatedGmailService(session);
            List<EmailReader.Email> emails = EmailReader.readAppEmails(gmail);
            return ResponseEntity.ok(emails); // Return JSON response
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/userEmail")
    public ResponseEntity<String> getUserEmail(HttpSession session) {
        try {
            Gmail gmail = GmailService.getAuthenticatedGmailService(session);
            String userEmail = gmail.users().getProfile("me").execute().getEmailAddress();
            return ResponseEntity.ok(userEmail);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to retrieve user email: " + e.getMessage());
        }
    }

}