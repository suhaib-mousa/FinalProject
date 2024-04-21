package com.example.demo.view;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import com.example.demo.models.Book;
import com.example.demo.models.Document;
import com.example.demo.services.HttpRequest;
import java.util.*;

public class Documents {
    private Scanner stringIN = new Scanner(System.in);
    private Scanner numIN = new Scanner(System.in);
    private Authentication auth = Authentication.getInstance();
    private HttpRequest httpRequest = new HttpRequest();
    private Gson gson = new Gson();

    public void addDocument() throws Exception {
        System.out.println("Enter the database name");
        String dbName = stringIN.nextLine();
        System.out.println("Enter the document name");
        String docName = stringIN.nextLine();
        //send request to create the document and print the response
        String url = auth.nodeUrl+"/database/create/"+dbName+"/books/"+docName;
        String res = httpRequest.sendPostRequestWithToken(url,auth.token,".");
        System.out.println(res);
        //if token not valid return to auth view to login
        if (res.startsWith("you have to login")){
            auth.authView();
        }
        System.out.println();
    }
    public void deleteDocument() throws Exception {
        System.out.println("Enter the document id");
        String docnum = stringIN.nextLine();
        //send request to delete the document and print the response
        String url = auth.nodeUrl+"/database/delete/document/"+docnum;
        String res = httpRequest.sendPostRequestWithToken(url,auth.token,".");
        System.out.println(res);
        //if token not valid return to auth view to login
        if (res.startsWith("you have to login")){
            auth.authView();
        }
        System.out.println();
    }

    public void showAllDocument() throws Exception {
        String url = auth.nodeUrl+"/documents";
        //send a request to get all documents information
        String res = httpRequest.sendGetRequestWithToken(url, auth.token);
        //if token not valid return to auth view to login
        if (res.startsWith("you have to login")){
            auth.authView();
        }
        else {
            //convert the json file to objects of documents and print them
            Map<Integer, Document> map = gson.fromJson(res, new TypeToken<HashMap<Integer, Document>>() {}.getType());
            //walk through all elements and print the objects in that database
            Iterator<Map.Entry<Integer, Document>> itr = map.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<Integer, Document> entry = itr.next();
                Document doc = entry.getValue();
                System.out.println(doc.toString());
            }
        }
        System.out.println();
    }

    public void showDocumentByID() throws Exception {
        System.out.println("Enter the document id");
        int docNum = stringIN.nextInt();
        //get the document
        Map<String , String> res = getDocumentByID(docNum);
        //if token not valid return to auth view to login
        if (res.size()==0){
            System.out.println("you have to login to access this service");
            auth.authView();
        }
        else {
            //get the books information and print them
            Map<Integer, Book> books = extractBooks(res.get("data"));

            if (books.size() > 0) {
                //walk through all elements and delete the objects in that database
                Iterator<Map.Entry<Integer, Book>> itr = books.entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry<Integer, Book> entry = itr.next();
                    Book book = entry.getValue();
                    System.out.println(book.toString());
                }
            } else {
                System.out.println("this document is empty");
            }
        }
        System.out.println();
    }

    public void addProduct() throws Exception {
        //enter book and document information
        System.out.println("Enter the document id");
        int docNum = numIN.nextInt();

        System.out.println("Enter the book title");
        String bookName = stringIN.nextLine();

        System.out.println("Enter the book author name");
        String authName = stringIN.nextLine();

        //get the document from server
        Map<String , String> res = getDocumentByID(docNum);
        //if token not valid return to auth view to login
        if (res.size()==0){
            System.out.println("you have to login to access this service");
            auth.authView();
        }
        else {
            //add the new book to document and save it
            Document doc = extractDocumentInformation(res.get("document"));
            Map<Integer, Book> books = extractBooks(res.get("data"));
            //to create a unique if for book
            int bookid = doc.getVersion() + 1;
            Book book = new Book(bookid, bookName, authName);
            books.put(doc.getVersion() + 1, book);
            System.out.println(storeDoc(docNum, books, doc.getVersion()));
        }
    }

    public void deleteProduct() throws Exception {
        //enter document and book ids
        System.out.println("Enter the document id");
        int docNum = numIN.nextInt();

        System.out.println("Enter the book id");
        int bookid = numIN.nextInt();
        //get the document
        Map<String , String> res = getDocumentByID(docNum);
        //if token not valid return to auth view to login
        if (res.size()==0){
            System.out.println("you have to login to access this service");
            auth.authView();
        }
        else {
            //extract data from response
            Document doc = extractDocumentInformation(res.get("document"));
            Map<Integer, Book> books = extractBooks(res.get("data"));
            //check if the book id is valid and remove it
            if (books.containsKey(bookid)) {
                books.remove(bookid);
                //save the changed document and print result from server
                System.out.println(storeDoc(docNum, books, doc.getVersion()));
            } else {
                System.out.println("no book with that id");
            }
        }
    }

    public void editProduct() throws Exception {
        //enter the book information and document id
        System.out.println("Enter the document id");
        int docNum = numIN.nextInt();

        System.out.println("Enter the book id");
        int bookid = numIN.nextInt();

        System.out.println("Enter the new book title");
        String bookName = stringIN.nextLine();

        System.out.println("Enter the book new author name");
        String authName = stringIN.nextLine();
        //get the document
        Map<String , String> res = getDocumentByID(docNum);
        //if token not valid return to auth view to login
        if (res.size()==0){
            System.out.println("you have to login to access this service");
            auth.authView();
        }
        else {
            //extract data from response
            Document doc = extractDocumentInformation(res.get("document"));
            Map<Integer, Book> books = extractBooks(res.get("data"));
            //edit the object and save to document and print the response from server
            if (books.containsKey(bookid)) {
                books.get(bookid).setTitle(bookName);
                books.get(bookid).setAuthor(authName);
                System.out.println(storeDoc(docNum, books, doc.getVersion()));
            } else {
                System.out.println("no book with that id");
            }
        }
    }

    public Map<String,String> getDocumentByID(int id) throws Exception {
        String url = auth.nodeUrl+"/database/get/"+id;
        String res = httpRequest.sendGetRequestWithToken(url,auth.token);
        if (res.startsWith("you have to login")){
            return new HashMap<>();
        }
        return gson.fromJson(res, new TypeToken<HashMap<String, String>>() {}.getType());
    }

    public Document extractDocumentInformation(String res) throws Exception {
        Document document = gson.fromJson(res,Document.class);
        return document;
    }

    public Map <Integer , Book> extractBooks(String res){
        if (res.length()>0) {
            return gson.fromJson(res, new TypeToken<HashMap<Integer, Book>>() {}.getType());
        }
        else return new HashMap<>();
    }

    public String storeDoc(int id,Map<Integer,Book> books,int version) throws Exception {
        Map<String , String> toServer = new HashMap<>();
        toServer.put("data",gson.toJson(books));
        toServer.put("version",String.valueOf(version));
        //send the document with version to server
        String url = auth.nodeUrl+"/database/edit/"+id;
        return httpRequest.sendPostRequestWithToken(url, auth.token, gson.toJson(toServer));
    }
}
