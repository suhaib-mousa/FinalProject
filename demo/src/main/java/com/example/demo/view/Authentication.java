package com.example.demo.view;

import com.example.demo.services.HttpRequest;

import java.util.Scanner;
public class Authentication {
    public static String token="";
    public static String nodeUrl="";
    private Scanner numIN = new Scanner(System.in);
    private Scanner stringIN = new Scanner(System.in);
    private HttpRequest httpRequest = new HttpRequest();

    private static Authentication auth;

    private Authentication(){}

    public static Authentication getInstance(){
        if (auth==null){
            auth=new Authentication();
        }
        return auth;
    }

    public void authView() throws Exception {
        System.out.println("-------------------------------------");
        // call login or signup function
        System.out.println("Login or Signup");
        token="";
        while (token.equals("")){
            System.out.println("1-Login");
            System.out.println("2-Signup");
            int choice = numIN.nextInt();

            if (choice==1){
                login();
            }
            else if (choice==2){
                signup();
            }
            else{
                System.out.println("invalid input , please inter 1 or 2");
            }
        }
    }

    private void login() throws Exception {
        while (true) {
            //enter user information
            System.out.println("Enter your username and password to login");
            System.out.print("username : ");String username = stringIN.nextLine();
            System.out.print("password : ");String password = stringIN.nextLine();
            //send request to server
            String url ="http://localhost:8080/login?username="+username+"&password="+password;
            String res = httpRequest.sendPostRequest(url);
            //check of the server return the node url or not
            if (res.startsWith("http")){
                //extract the node url and the token
                String[] splited = res.split(" ");
                token=splited[1];
                nodeUrl=splited[0];
                System.out.println(nodeUrl);
            }
            //print the problem that happened
            else {
                System.out.println(res);
            }
            System.out.println();
            break;
        }
    }

    private void signup() throws Exception {
        while (true){
            //enter user information
            System.out.println("Enter your username and password to signup");
            System.out.print("username : ");String username = stringIN.nextLine();
            System.out.print("password : ");String password = stringIN.nextLine();
            //send request and print the response
            String url ="http://localhost:8080/signup?username="+username+"&password="+password;
            String res = httpRequest.sendPostRequest(url);
            System.out.println(res);
            System.out.println();
            break;
        }
    }
    public void logout() throws Exception {
        String url = nodeUrl+"/logout";
        httpRequest.sendPostRequestWithToken(url,token,".");
        authView();
    }

}


