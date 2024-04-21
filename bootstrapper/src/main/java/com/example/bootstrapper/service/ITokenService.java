package com.example.bootstrapper.service;

import com.example.bootstrapper.model.User;

public interface ITokenService {
    String createToken(User user);
}
