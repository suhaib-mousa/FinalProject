package com.example.bootstrapper.service;

import com.example.bootstrapper.model.User;
import java.io.IOException;

public interface IAuthService {
    User login(User user) throws IOException;
    String signup(User user);
}
