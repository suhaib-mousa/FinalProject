package com.example.node.service;

import java.io.File;
import java.io.IOException;

public interface IFileService {
    void writeFile(String path , String jsonFile) throws IOException;
    String readFile(String path) throws IOException;
    void deleteFolder(File file);
    void createFolder(String path);
    void deleteDocument(String path);
    void createDocument(String path) throws IOException;
}
