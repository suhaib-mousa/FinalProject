package com.example.node.affinity;

import java.io.IOException;

public interface IAffinityManager {
    boolean checkDocAffinity(int docID) throws IOException;
    void addDocumentAffinity(int docID);
    void deleteDocumentAffinity(int docID);
    int GetDocumentAffinity(int id) throws IOException;
}
