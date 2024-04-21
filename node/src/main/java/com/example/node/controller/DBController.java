package com.example.node.controller;

import com.example.node.broadcasting.Broadcast;
import com.example.node.model.AssignedUser;
import com.example.node.service.DBservice;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class DBController {
    @Autowired
    DBservice dBservice;
    @Autowired
    Broadcast broadcastInstance;
    @Autowired
    AssignedUser assignedUser;

    @PostMapping("/database/create/{name}")
    public String createDB(HttpServletRequest request,
                           @PathVariable(value = "name") String name,
                           @RequestBody String broadcast) throws InterruptedException {
        String token = request.getHeader("Authorization");
        //check if user token is expired or not
        if(!assignedUser.isTokenValid(token) && !broadcast.equals("broadcast")){
            return "you have to login to access this service";
        }
        //create database
        String res = dBservice.createDatabase(name);
        //if db added without errors, and it's not broadcast request then broadcast the request
        if(res.equals("DB added") && !broadcast.equals("broadcast")){
            broadcastInstance.broadcastingURL(request.getRequestURI());
        }
        return res;
    }

    @PostMapping("/database/delete/{name}")
    public String deleteDB(HttpServletRequest request,
                           @PathVariable(value = "name") String name,
                           @RequestBody String broadcast) throws IOException, InterruptedException {
        String token = request.getHeader("Authorization");
        //check if token is expired or not
        if(!assignedUser.isTokenValid(token) && !broadcast.equals("broadcast")){
            return "you have to login to access this service";
        }
        //check if the user is admin to delete the database
        //and check if the message is a broadcast or not
        if (!assignedUser.isAdmin(token) && !broadcast.equals("broadcast")) {
            return "you are not allowed to delete database";
        }
        //delete the database
        String res = dBservice.deleteDatabase(name);
        //if db deleted without errors, and it's not broadcast request then broadcast the request
        if(res.equals("DB deleted") && !broadcast.equals("broadcast")){
            broadcastInstance.broadcastingURL(request.getRequestURI());
        }
        return res;
    }

    @GetMapping("/databases")
    public String getDatabases(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(!assignedUser.isTokenValid(token)){
            return "you have to login to access this service";
        }
        return dBservice.getDatabases();
    }
}
