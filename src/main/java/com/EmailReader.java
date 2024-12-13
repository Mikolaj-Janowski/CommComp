package com;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import java.io.IOException;
import java.util.List;

public class EmailReader {

    public static void readEmails(Gmail service) throws IOException {
        ListMessagesResponse response = service.users().messages().list("me").setQ("is:unread").execute();
        List<Message> messages = response.getMessages();

        if (messages == null || messages.isEmpty()) {
            System.out.println("No new emails.");
        } else {
            for (Message message : messages) {
                Message fullMessage = service.users().messages().get("me", message.getId()).execute();
                System.out.println("Email snippet: " + fullMessage.getSnippet());
            }
        }
    }
}