package com;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListHistoryResponse;
import com.google.api.services.gmail.model.History;

import java.io.IOException;
import java.util.List;
import java.math.BigInteger;

public class EmailHistory {

    public static void getEmailHistory(Gmail service, String startHistoryId) throws IOException {
        BigInteger historyId = new BigInteger(startHistoryId); // Convert String to BigInteger
        ListHistoryResponse response = service.users().history().list("me")
                .setStartHistoryId(historyId) // Use BigInteger here
                .execute();
        List<History> histories = response.getHistory();
    
        if (histories == null || histories.isEmpty()) {
            System.out.println("No history available.");
        } else {
            for (History history : histories) {
                System.out.println("History ID: " + history.getId());
            }
        }
    }
}