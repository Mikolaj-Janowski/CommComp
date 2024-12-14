package com.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MockConfirmAuthController {

    @GetMapping("/api/confirmauth")
    public Map<String, Object> getAuthorizationStatus() {
        // Simulate the behavior of the theoretical C# app
        return Map.of("authorized", true); // or false for unauthorized simulation
    }
}