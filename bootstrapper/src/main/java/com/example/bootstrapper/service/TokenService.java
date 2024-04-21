package com.example.bootstrapper.service;

import com.example.bootstrapper.model.User;
import com.nimbusds.jose.shaded.gson.Gson;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService implements ITokenService{
    public String createToken(User user){
        //create token to send it to user
        Map<String , String> tokenMap = new HashMap<>();
        tokenMap.put("username",user.getUsername());
        tokenMap.put("role",user.getRole());
        tokenMap.put("createTime",String.valueOf(LocalDateTime.now()));

        Gson json = new Gson();
        String str = json.toJson(tokenMap);
        return str;
    }
}
