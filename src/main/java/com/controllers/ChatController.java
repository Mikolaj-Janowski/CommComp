package com.controllers;

import com.models.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/sendMessage") // Maps to "/app/sendMessage"
    @SendTo("/topic/messages") // Broadcasts to "/topic/messages"
    public ChatMessage sendMessage(ChatMessage message) {
        return message; // Simply returns the message to all connected clients
    }
}