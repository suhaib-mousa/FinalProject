package com.example.demo;

import com.example.demo.view.Authentication;
import com.example.demo.view.Dashboard;

public class Main {
    public static void main(String[] args) throws Exception {
        Dashboard dashboard = new Dashboard();
        Authentication auth = Authentication.getInstance();
        //start the application views
        auth.authView();
        dashboard.dashboardView();


	// write your code here
    }
}
