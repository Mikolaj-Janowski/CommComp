package com.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class ProxyController {

    private final RestTemplate restTemplate;

    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/api/confirmauth-proxy")
    public Map<String, Object> proxyAuthorization() {
        // Mocking a C# app by pointing to another Java endpoint
        String mockCSharpAuthUrl = "http://localhost:8080/api/confirmauth";

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                mockCSharpAuthUrl,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        return response.getBody(); // Forward the response from the mock endpoint
    }
}