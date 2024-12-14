package com;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

import jakarta.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GmailService {
    private static final String APPLICATION_NAME = "HACS Communication";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
    private static GoogleAuthorizationCodeFlow flow;

    // Initialize GoogleAuthorizationCodeFlow
    public static GoogleAuthorizationCodeFlow initializeFlow() throws Exception {
        if (flow == null) {
            var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            InputStream in = GmailService.class.getResourceAsStream("/client_secret_846532980958-0olj6s95f3b8n8j3659iig1mkahgdls5.apps.googleusercontent.com.json");
            var clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                    .setAccessType("offline")
                    .build();
        }
        return flow;
    }

    // Generate Authorization URL
    public static String getAuthorizationUrl() throws Exception {
        return initializeFlow().newAuthorizationUrl()
                .setRedirectUri("http://localhost:8080/gmail/oauth2callback") // Change this for deployment
                .build();
    }

    // Exchange Authorization Code for Credential
    public static void exchangeAuthorizationCode(String code, HttpSession session) throws Exception {
        var tokenResponse = initializeFlow().newTokenRequest(code)
                .setRedirectUri("http://localhost:8080/gmail/oauth2callback")
                .execute();
        Credential credential = initializeFlow().createAndStoreCredential(tokenResponse, "user");
        
        // Store credential in session for authenticated requests
        session.setAttribute("gmailCredential", credential);
    }

    // Retrieve Gmail Service for Authenticated User
    public static Gmail getAuthenticatedGmailService(HttpSession session) throws GeneralSecurityException, Exception {
        Credential credential = (Credential) session.getAttribute("gmailCredential");
        if (credential == null) {
            throw new IllegalStateException("User is not authenticated. Please login first.");
        }

        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}