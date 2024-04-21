package com.example.node.controller;

import com.example.node.affinity.AffinityManager;
import com.example.node.broadcasting.Broadcast;
import com.example.node.model.AssignedUser;
import com.example.node.service.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
public class DocumentController {
    @Autowired
    DocumentService docService;
    @Autowired
    AssignedUser assignedUser;
    @Autowired
    Broadcast broadcastInstance;
    @Autowired
    AffinityManager affinity;

    @GetMapping("/CollectionDocuments/{collectionName}")
    public String getCollectionDocuments(
            @PathVariable(value = "collectionName") String collectionName) throws IOException{
        var documents = docService.getCollectionDocuments(collectionName);
        return documents;
    }

    @PostMapping("/database/create/{dbName}/{collectionName}/{docName}")
    public int createDoc(HttpServletRequest request, @PathVariable(value = "dbName") String dbName
                            ,@PathVariable(value = "collectionName") String collectionName,
                           @PathVariable(value = "docName") String docName,
                            @RequestBody String broadcast) throws IOException, InterruptedException, IllegalAccessException {
        String token = request.getHeader("Authorization");
        //check if token is expired or not
        if(!assignedUser.isTokenValid(token) && !broadcast.equals("broadcast")){
            throw new IllegalAccessException("The token expired!");
        }
        //add document
        int res = docService.addDocument(dbName,collectionName,docName);
        //if document added without errors, and it's not broadcast request then broadcast the request
        if(!broadcast.equals("broadcast")){
            broadcastInstance.broadcastingURL(request.getRequestURI());
        }
        return res;
    }

    @PostMapping("/database/delete/document/{id}")
    public String deleteDoc(HttpServletRequest request,
                            @PathVariable(value = "id") int id,
                            @RequestBody String broadcast) throws IOException, InterruptedException {
        String token = request.getHeader("Authorization");
        //check if token is expired or not
        if(!assignedUser.isTokenValid(request.getHeader("Authorization")) && !broadcast.equals("broadcast")){
            return "you have to login to access this service";
        }
        //check if the user is admin to delete the database
        //and check if the message is broadcasted or not
        if (!assignedUser.isAdmin(token) && !broadcast.equals("broadcast")) {
            return "you are not allowed to delete document";
        }

        //delete database
        String res = docService.deleteDocument(id);
        //if document deleted without errors, and it's not broadcast request then broadcast the request
        if(res.equals("document deleted") && !broadcast.equals("broadcast")){
            broadcastInstance.broadcastingURL(request.getRequestURI());
        }
        return res;
    }

    @GetMapping("/database/get/{id}")
    public String getDoc(HttpServletRequest request, @PathVariable(value = "id") int id) throws IOException {
        //check if token is expired or not
        if(!assignedUser.isTokenValid(request.getHeader("Authorization"))){
            return "you have to login to access this service";
        }
        //get documentById
        return docService.getDocument(id);
    }

    @PostMapping("/database/edit/{id}")
    public String editDoc(HttpServletRequest request,
                          @PathVariable(value = "id") int id,
                          @RequestBody String file) throws IOException, InterruptedException {
        //check if token is expired or not
        if(!assignedUser.isTokenValid(request.getHeader("Authorization")) ){
            return "you have to login to access this service";
        }
        //check if the node is the affinity of document
        if(affinity.checkDocAffinity(id)){
            //edit the doc and broadcast the edited file
            String res = docService.editDocument(id,file);
            broadcastInstance.broadcastEdit(id,file);
            return res;
        }
        else {
            //redirect the file to his affinity node
            return broadcastInstance.RedirectToAffinity(id,file);
        }
    }

    @PostMapping("/affinity/database/edit/{id}")
    public String broadcastedDoc(@PathVariable(value = "id") int id,
                                 @RequestBody String file) throws IOException {
        //store the edited document from the broadcaster
        return docService.editDocument(id,file);
    }

    @PostMapping("/redirect/{id}")
    public String redirectedRequest(@PathVariable(value = "id") int id,
                                 @RequestBody String file) throws IOException, InterruptedException {
        //store file after send the edited file from not affinity node to affinity node then broadcast the edited file
        String res= docService.editDocument(id,file);
        broadcastInstance.broadcastEdit(id,file);
        return res;
    }

    @GetMapping("/documents")
    public String getDatabases(HttpServletRequest request) throws IOException {
        String token = request.getHeader("Authorization");
        if(!assignedUser.isTokenValid(token)){
            return "you have to login to access this service";
        }
        return docService.getDocuments();
    }
}
