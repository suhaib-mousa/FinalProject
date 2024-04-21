package com.example.bootstrapper.service;

import com.example.bootstrapper.model.User;

public interface INodeService {
    String assignUserToNode(User user);
    String clearNode(User user);
}
