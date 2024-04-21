package com.example.bootstrapper.service;

import java.io.IOException;

public interface IFileService {
    void writeFile(String path , String jsonfile) throws IOException;
    String readFile(String path) throws IOException;
}
