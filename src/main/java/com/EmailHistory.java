package com;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListHistoryResponse;
import com.google.api.services.gmail.model.History;

import java.io.IOException;
import java.util.List;

public class EmailHistory {

    public static void getEmailHistory(Gmail service, String startHistoryId) throws IOException {
        ListHistoryResponse response = service.users().history().list("me")
                .setStartHistoryId(startHistoryId)
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