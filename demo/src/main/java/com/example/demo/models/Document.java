package com.example.demo.models;

import lombok.Getter;

@Getter
public class Document {
    private int id;
    private String name;
    private String DBname;
    private String collectionName;
    private int version;

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", DBname='" + DBname + '\'' +
                ", collectionName='" + collectionName + '\'' +
                ", version=" + version +
                '}';
    }
}
