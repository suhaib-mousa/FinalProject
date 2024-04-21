package com.example.node.broadcasting;

import java.io.IOException;

public interface IBroadcast {
    void broadcastingURL(String url) throws InterruptedException;
    void broadcastEdit(int id, String jsonFile) throws InterruptedException;
    String RedirectToAffinity(int docID, String file) throws IOException;
}
