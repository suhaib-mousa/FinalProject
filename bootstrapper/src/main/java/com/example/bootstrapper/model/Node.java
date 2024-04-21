package com.example.bootstrapper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Node {
    private String name;
    private String port;
    private int nodeNum;

    public String getUrl(){
        return "http://"+name+":"+port;
    }

    public String getClientURL(){
            return "http://localhost:808"+nodeNum;
    }
}

