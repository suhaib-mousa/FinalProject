package com.example.node.service;

import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.Files;

@Service
public class FileService implements IFileService{
    public void writeFile(String path , String jsonFile) throws IOException {
        File file = new File(path);
        FileWriter writer = new FileWriter(file);
        writer.write(jsonFile);
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

    public void deleteFolder(File file){
        //delete a database
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteFolder(f);
                }
            }
        }
        file.delete();
    }

    public void createFolder(String path){
        File file = new File(path);
        file.mkdir();
    }

    public void deleteDocument(String path){
        File file = new File(path);
        file.delete();
    }

    public void createDocument(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
    }
}
