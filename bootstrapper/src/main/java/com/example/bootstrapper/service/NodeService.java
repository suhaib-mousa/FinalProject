package com.example.bootstrapper.service;

import com.example.bootstrapper.model.Node;
import com.example.bootstrapper.model.User;
import com.example.bootstrapper.model.UserNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class NodeService implements INodeService{
    private static UserNode userNode = UserNode.getInstance();
    private static Map<String , Node> userToNode = userNode.getUserToNode();
    //contains all users and their tokens
    private static Map<String , String> usersTokens = new HashMap<>();
    //private static int numberOfCurrentUsers =0;
    private RestTemplate restTemplate=new RestTemplate();

    @Autowired
    TokenService tokenService;

    public synchronized String assignUserToNode(User user){
        //check if user already log in
        if(userToNode.containsKey(user.getUsername())){
            return userToNode.get(user.getUsername()).getClientURL()+" "+usersTokens.get(user.getUsername());
        }
        //assign user to node
        try {
            //get the node url and user token and store it
            Node node = userNode.addUserToNode(user);
            String nodeURL= node.getUrl();
            String clientURL= userNode.getUserToNode().get(user.getUsername()).getClientURL();
            String token=tokenService.createToken(user);
            usersTokens.put(user.getUsername(),token);
            //send the token to assigned node
            restTemplate.postForObject(nodeURL+"/signuser",token,String.class);
            return clientURL+" "+token;
        }
        catch (Exception e){
            return e.getMessage();
        }
    }

    public synchronized String clearNode(User user){
        //remove user from node
        userNode.removeUserFromNode(user);
        usersTokens.remove(user.getUsername());
        return "node cleared";
    }
}