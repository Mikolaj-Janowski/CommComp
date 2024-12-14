package com.controllers;

import com.GmailService;
import com.EmailSender;
import com.EmailReader;
import com.EmailHistory;
import com.google.api.services.gmail.Gmail;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;

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
    public ResponseEntity<String> handleCallback(
            @RequestParam() String code,
            HttpSession session) {
        try {
            GmailService.exchangeAuthorizationCode(code, session);
            return ResponseEntity.ok("Authorization successful!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Authorization failed: " + e.getMessage());
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
    public ResponseEntity<String> readEmails(HttpSession session) {
        try {
            Gmail gmail = GmailService.getAuthenticatedGmailService(session);
            EmailReader.readEmails(gmail);
            return ResponseEntity.ok("Unread emails fetched. Check logs for details.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to fetch emails: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<String> getEmailHistory(
            @RequestParam String historyId,
            HttpSession session) {
        try {
            Gmail gmail = GmailService.getAuthenticatedGmailService(session);
            EmailHistory.getEmailHistory(gmail, historyId);
            return ResponseEntity.ok("Email history fetched. Check logs for details.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to fetch email history: " + e.getMessage());
        }
    }
}