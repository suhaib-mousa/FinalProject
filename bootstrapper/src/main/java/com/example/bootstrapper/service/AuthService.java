package com.example.bootstrapper.service;

import com.example.bootstrapper.model.User;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService implements IAuthService{
    @Autowired
    FileService fileService;
    Map<String ,User> cachedUsers =new HashMap<>();
    String usersFilePath ="src/main/java/com/example/bootstrapper/data/users.json";
    Gson gson = new Gson();

    public synchronized User login(User user) throws IOException {
        if (cachedUsers.size()==0) {
            // Read the JSON file into a string
            String fileJson = fileService.readFile(usersFilePath);

            // Convert the JSON string back to a HashMap
            Gson gson = new Gson();
            cachedUsers = gson.fromJson(fileJson, new TypeToken<HashMap<String, User>>(){}.getType());
        }

        //check if user is valid
        if (cachedUsers.containsKey(user.getUsername())){
            User dbuser = cachedUsers.get(user.getUsername());
            if(dbuser.getUsername().equals(user.getUsername()) && dbuser.getPassword().equals(user.getPassword())){
                return dbuser;
            }
        }
        return null;
    }

    public synchronized String signup(User user) {
        try {
            String jsonFile;
            if (cachedUsers.size()==0) {
                jsonFile = fileService.readFile(usersFilePath);
                // Convert the JSON string back to a HashMap using Gson
                cachedUsers = gson.fromJson(jsonFile, new TypeToken<HashMap<String, User>>() {}.getType());
            }

            //check if user is existed
            if (cachedUsers.containsKey(user.getUsername())){
                return "this username used before";
            }

            //add user
            user.setRole("user");
            cachedUsers.put(user.getUsername(),user);

            // Write the JSON data to a file
            jsonFile = gson.toJson(cachedUsers);
            fileService.writeFile(usersFilePath,jsonFile);
            return "user added";

        } catch (IOException e) {
            return e.toString();
        }
    }
}
