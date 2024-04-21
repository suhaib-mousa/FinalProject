package com.example.node.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Document {
    private int id;
    private String name;
    private String DBname;
    private String collectionName;
    private int version=0;

    public void addVersion(){
        version++;
    }

}
