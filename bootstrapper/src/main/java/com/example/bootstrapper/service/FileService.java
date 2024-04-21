package com.example.bootstrapper.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FileService implements IFileService{
    public void writeFile(String path , String jsonfile) throws IOException {
        File file = new File(path);
        FileWriter writer = new FileWriter(file);
        writer.write(jsonfile);
        writer.close();
    }

    public String readFile(String path) throws IOException {
        File file = new File(path);
        // Read the JSON file into a string
        FileReader reader = new FileReader(file);
        StringBuilder content = new StringBuilder();
        int character;
        while ((character = reader.read()) != -1) {
            content.append((char) character);
        }
        reader.close();
        return content.toString();
    }
}
