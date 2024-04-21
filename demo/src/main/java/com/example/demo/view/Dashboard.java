package com.example.demo.view;

import java.util.Scanner;

public class Dashboard {
    private Scanner numIN = new Scanner(System.in);
    private Databases databases=new Databases();
    private Documents documents=new Documents();
    private Authentication authentication = Authentication.getInstance();


    public void dashboardView() throws Exception {
        while (true) {

            //print the available choices and call the functions depend on user choice
            System.out.println("-------------------------------------");
            System.out.println("choose a option");
            System.out.println("1-Add database");
            System.out.println("2-Delete database");
            System.out.println("3-Show all databases");
            System.out.println("4-Add document");
            System.out.println("5-Delete document");
            System.out.println("6-Show all documents");
            System.out.println("7-Show documents by id");
            System.out.println("8-Add book");
            System.out.println("9-Edit book");
            System.out.println("10-Delete book");
            System.out.println("11-Logout");

            int choice = numIN.nextInt();
            switch (choice) {
                case 1:
                    databases.addDatabase();
                    break;
                case 2:
                    databases.deleteDatabase();
                    break;
                case 3:
                    databases.showDatabases();
                    break;
                case 4:
                    documents.addDocument();
                    break;
                case 5:
                    documents.deleteDocument();
                    break;
                case 6:
                    documents.showAllDocument();
                    break;
                case 7:
                    documents.showDocumentByID();
                    break;
                case 8:
                    documents.addProduct();
                    break;
                case 9:
                    documents.editProduct();
                    break;
                case 10:
                    documents.deleteProduct();
                    break;
                case 11:
                    authentication.logout();
            }
        }
    }
}
