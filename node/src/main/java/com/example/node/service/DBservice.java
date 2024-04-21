package com.example.node.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@Service
public class DBservice implements IDBservice{
    @Autowired
    FileService fileService;
    @Autowired
    DocumentService documentService;

    public synchronized String createDatabase(String name){
        //check if the folder is exists
        String path = "src/main/java/com/example/node/databases/"+name;
        if(Files.exists(Path.of(path))){
            return "the database a already exits";
        }
        //create folder
        fileService.createFolder(path);
        return "DB added";
    }

    public synchronized String deleteDatabase(String name) throws IOException {
        //check if the folder is exists
        String path = "src/main/java/com/example/node/databases/"+name;
        if(Files.notExists(Path.of(path))){
            return "there are no database with that name";
        }
        //delete documents information from affinity file and documents file
        documentService.deleteByDatabaseName(name);
        //delete all files and folders inside database
        File file= new File(path);
        fileService.deleteFolder(file);
        return "DB deleted";
    }

    public String getDatabases() {
        String directoryPath = "src/main/java/com/example/node/databases/";
        File directory = new File(directoryPath);
        ArrayList<String> list = new ArrayList<>();

        if (directory.exists() && directory.isDirectory()) {
            File[] subdirectories = directory.listFiles(File::isDirectory);

            if (subdirectories != null) {
                for (File subdirectory : subdirectories) {
                    list.add(subdirectory.getName());
                }
            }
        }
        return String.join(", ",list);
    }
}
