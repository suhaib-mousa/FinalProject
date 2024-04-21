package com.example.node.service;

import java.io.IOException;

public interface IDocumentService {
    int addDocument(String dbName , String collectionName, String docName) throws IOException;
    String deleteDocument(int id) throws IOException;
    String getDocument(int id) throws IOException;
    String editDocument(int id, String jsonfile) throws IOException;
    void getDocumentsFile() throws IOException;
    void storeDocumentsFile();
    String getDocumentPath(int id);
    String checkVersion(String file, int id);
    void deleteByDatabaseName(String name) throws IOException;
    void storeCounterFile(int id) throws IOException;
    int readCounterFile() throws IOException;
    String getDocuments() throws IOException;
}
