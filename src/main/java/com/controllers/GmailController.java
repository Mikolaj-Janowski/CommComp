package com.controllers;

import com.GmailService;
import com.EmailSender;
import com.EmailReader;
import com.EmailHistory;

import com.google.api.services.gmail.Gmail;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/gmail")
public class GmailController {

    @GetMapping("/send")
    public ResponseEntity<String> sendEmail(
            @RequestParam String recipient,
            @RequestParam String subject,
            @RequestParam String body) {
        try {
            Gmail service = GmailService.getGmailService();
            EmailSender.sendEmail(service, recipient, subject, body);
            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }

    @GetMapping("/read")
    public ResponseEntity<String> readEmails() {
        try {
            Gmail service = GmailService.getGmailService();
            EmailReader.readEmails(service);
            return ResponseEntity.ok("Unread emails fetched. Check logs for details.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to fetch emails: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<String> getEmailHistory(@RequestParam String historyId) {
        try {
            Gmail service = GmailService.getGmailService();
            EmailHistory.getEmailHistory(service, historyId);
            return ResponseEntity.ok("Email history fetched. Check logs for details.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to fetch email history: " + e.getMessage());
        }
    }
}
