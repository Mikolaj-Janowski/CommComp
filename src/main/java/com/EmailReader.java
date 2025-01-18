package com;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmailReader {

    public static List<Email> readAppEmails(Gmail service) throws IOException {
        // Query to fetch emails with the specified app prefix
        String query = "from:me subject:HACS:"; // Adjust as necessary
        ListMessagesResponse response = service.users().messages().list("me").setQ(query).execute();
        List<Message> messages = response.getMessages();
        List<Email> emailDetails = new ArrayList<>();

        if (messages != null && !messages.isEmpty()) {
            for (Message message : messages) {
                Message fullMessage = service.users().messages().get("me", message.getId()).execute();

                // Extract subject
                String subject = fullMessage.getPayload().getHeaders().stream()
                        .filter(header -> "Subject".equalsIgnoreCase(header.getName()))
                        .map(MessagePartHeader::getValue)
                        .findFirst()
                        .orElse("(No Subject)");

                // Extract body snippet
                String snippet = fullMessage.getSnippet();

                // Add the email details as an Email object
                emailDetails.add(new Email(subject, snippet));
            }
        }

        return emailDetails;
    }

    // Inner class to represent an email
    public static class Email {
        private String subject;
        private String body;

        public Email(String subject, String body) {
            this.subject = subject;
            this.body = body;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }
    }
}
