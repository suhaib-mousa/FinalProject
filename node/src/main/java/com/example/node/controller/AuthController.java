package com.example.node.controller;

import com.example.node.model.AssignedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    @Autowired
    AssignedUser assignedUser;

    @PostMapping("/signuser")
    public void signUser(@RequestBody String token){
        //set the token of assigned user
        assignedUser.setToken(token);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String token){
        //clear user information from node
        assignedUser.clearNode(token);
    }
}
