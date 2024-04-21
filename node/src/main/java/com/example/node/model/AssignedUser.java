package com.example.node.model;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter
public class AssignedUser {
    private Map<String  , Token> tokenToUser = new HashMap<>();
    private RestTemplate restTemplate = new RestTemplate();

    public void clearNode(String token){
        //make bootstrapping node clear current node to be user by another user
        String URL= "http://bootstrapper:8080/signout";
        restTemplate.postForObject(URL, tokenToUser.get(token).getUser(),String.class);
        tokenToUser.remove(token);
    }

    public synchronized void setToken(String tokenString){
        //extract data from token and set expireDate of token
        Gson gson = new Gson();
        Map<String,String> tokenMap = gson.fromJson(tokenString, new TypeToken<HashMap<String, String>>() {}.getType());
        User user = new User(tokenMap.get("username"),"",tokenMap.get("role"));
        LocalDateTime createTime= LocalDateTime.parse(tokenMap.get("createTime"));
        LocalDateTime expireTime=createTime.plus(3, ChronoUnit.HOURS);
        Token token = new Token(tokenString,user,expireTime,expireTime);
        tokenToUser.put(tokenString,token);
    }

    public boolean isTokenValid(String reqToken){
        //check if the token expired or not
        if (tokenToUser.containsKey(reqToken)) {
            if (tokenToUser.containsKey(reqToken) && LocalDateTime.now().isBefore(tokenToUser.get(reqToken).getExpireTime())) {
                return true;
            }
            //clear user information
            clearNode(reqToken);
        }
        return false;
    }

    public boolean isAdmin(String token){
        if (tokenToUser.containsKey(token)){
            if (tokenToUser.get(token).getUser().getRole().equals("admin")){
                return true;
            }
        }
        return false;
    }
}
