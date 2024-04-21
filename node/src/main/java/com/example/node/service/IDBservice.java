package com.example.node.service;

import java.io.IOException;

public interface IDBservice {
    String createDatabase(String name);
    String deleteDatabase(String name) throws IOException;
    String getDatabases();

}
