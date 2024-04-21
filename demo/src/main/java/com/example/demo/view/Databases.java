package com.example.demo.view;

import com.example.demo.services.HttpRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Databases {
    private Scanner stringIN = new Scanner(System.in);
    private Authentication auth = Authentication.getInstance();
    private HttpRequest httpRequest = new HttpRequest();
    public void addDatabase() throws Exception {
        System.out.println("Enter the database name");
        String dbName = stringIN.nextLine();
        //send request to create database
        String url = auth.nodeUrl+"/database/create/"+dbName;
        String res = httpRequest.sendPostRequestWithToken(url,auth.token,".");
        System.out.println(res);
        //if token not valid return to auth view to login
        if (res.startsWith("you have to login")){
            auth.authView();
        }
        System.out.println();
    }
    public void deleteDatabase() throws Exception {
        System.out.println("Enter the database name");
        String dbName = stringIN.nextLine();
        //send request to delete database
        String url = auth.nodeUrl+"/database/delete/"+dbName;
        String res = httpRequest.sendPostRequestWithToken(url,auth.token,".");
        System.out.println(res);
        //if token not valid return to auth view to login
        if (res.startsWith("you have to login")){
            auth.authView();
        }
        System.out.println();
    }
    public void showDatabases() throws Exception {
        String url = auth.nodeUrl+"/databases";
        String res = httpRequest.sendGetRequestWithToken(url,auth.token);
        //if token not valid return to auth view to login
        if (res.startsWith("you have to login")){
            auth.authView();
        }
        else {
            //put database names in array and print it
            String[] splitArray = res.split(", ");
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(splitArray));
            System.out.println("databases");
            for (String str : arrayList) {
                System.out.println(str);
            }
        }
        System.out.println();
    }
}
