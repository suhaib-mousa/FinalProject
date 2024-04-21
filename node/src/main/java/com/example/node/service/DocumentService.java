package com.example.node.service;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import com.example.node.affinity.AffinityManager;
import com.example.node.broadcasting.Broadcast;
import com.example.node.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class DocumentService implements IDocumentService{

    @Autowired
    AffinityManager affinity;
    @Autowired
    Broadcast broadcast;
    @Autowired
    FileService fileService;

    //contains the document id and it's all information
    private Map<Integer , Document> docs = new HashMap<>();
    //cached documents
    private final Map<Integer , String> cache = new HashMap<>();
    //file contains all documents information
    private final String documentsPath ="src/main/java/com/example/node/databases/Documents.json";
    private final String docCounterPath = "src/main/java/com/example/node/databases/DocCounter.txt";

    public synchronized int addDocument(String dbName , String collectionName, String docName) throws IOException {
        //get the file that contains the details of all documents
        if (docs.size()==0){
            getDocumentsFile();
        }
        //check if the file is exists
        String path = "src/main/java/com/example/node/databases/"+dbName;
        if(Files.notExists(Path.of(path))){
            fileService.createFolder(path);
        }
        //create the collection if it's not exist
        path+="/"+collectionName;
        if(Files.notExists(Path.of(path))){
            fileService.createFolder(path);
        }
        //check if document exist or not
        path+="/"+docName+".json";
        if(Files.exists(Path.of(path))){
            throw new FileSystemException("The file exists already!");
        }
        //give a id for a document and store the document information in document file

        int docID = readCounterFile();
        Document newDoc = new Document(docID,docName,dbName,collectionName,0);
        //store the new document in documents details file
        docs.put(docID,newDoc);
        storeDocumentsFile();
        //create the document and set the affinity node of document
        fileService.createDocument(path);
        affinity.addDocumentAffinity(docID);
        //store the next id in file to use it later

        storeCounterFile(docID + 1);
        return docID;
    }

    public synchronized String deleteDocument(int id) throws IOException {
        //get the file that contains the details of all documents
        if (docs.size()==0){
            getDocumentsFile();
        }
        //check if the key is exists
        if (!docs.containsKey(id)){
            return "no document with that id";
        }
        //get the details of doc and delete the doc then store the file after Edit
        String path = getDocumentPath(id);
        fileService.deleteDocument(path);
        affinity.deleteDocumentAffinity(id);
        docs.remove(id);
        storeDocumentsFile();
        //remove document from cache
        if (cache.containsKey(id)){
            cache.remove(id);
        }
        //broadcast the file after edit
        return "document deleted";
    }

    public String getDocument(int id) throws IOException {
        //get a document by id
        if (docs.size()==0){
            getDocumentsFile();
        }
        if (!docs.containsKey(id)){
            return "no document with that id";
        }
        //if the file in cache just send the file
        String path = getDocumentPath(id);
        String file;
        if (cache.containsKey(id)){
            file= cache.get(id);
        }
        else {
            //if it's not cached read the file and put it in cache and send the file
            file = fileService.readFile(path);
            cache.put(id, file);
        }
        Document doc = docs.get(id);
        Map <String , String > map = new HashMap<>();
        Gson gson = new Gson();
        map.put("data",file);
        map.put("document", gson.toJson(doc));
        return gson.toJson(map);
    }

    public synchronized String editDocument(int id, String jsonfile) throws IOException {
        //get the file that contains the details of all documents
        if (docs.size()==0){
            getDocumentsFile();
        }
        //check if id is exists
        if (!docs.containsKey(id)){
            return "no document with that id";
        }

        //check if the original document version is equal the edited document version
        String AfterCheckVersion= checkVersion(jsonfile,id);
        if(AfterCheckVersion.equals("error")){
            return "File was modified by another one.enter the data another time.";
        }
        //get the doc
        Document doc=docs.get(id);
        //increase the version
        doc.addVersion();
        String path = getDocumentPath(id);
        //if the file is cached then delete remove the cache
        cache.remove(id);
        //edit the file and store it
        docs.put(id,doc);
        storeDocumentsFile();

        fileService.writeFile(path,AfterCheckVersion);
        return "file modified";
    }

    public void getDocumentsFile() throws IOException {
        //read the file that contains all document information
        String docsFile = fileService.readFile(documentsPath);
        // Convert the JSON string back to a HashMap using Gson
        Gson gson = new Gson();
        docs = gson.fromJson(docsFile, new TypeToken<HashMap<Integer, Document>>() {}.getType());
    }

    public String getCollectionDocuments(String collectionName) throws IOException{
        //read the file that contains all document information
        String docsFile = fileService.readFile(documentsPath);
        // Convert the JSON string back to a HashMap using Gson
        Gson gson = new Gson();
        docs = gson.fromJson(docsFile, new TypeToken<HashMap<Integer, Document>>() {}.getType());

        // Filter doc on collectionName and return it as string
        Map<Integer, Document> filteredDocs = new HashMap<>();
        for (Map.Entry<Integer, Document> entry : docs.entrySet()) {
            Document document = entry.getValue();
            if (document.getCollectionName().equals(collectionName)) {
                filteredDocs.put(entry.getKey(), document);
            }
        }

        return gson.toJson(filteredDocs);
    }

    public synchronized void storeDocumentsFile() {
        //store the file that contains all document information
        try {
            Gson gson = new Gson();
            String json = gson.toJson(docs);
            fileService.writeFile(documentsPath,json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDocumentPath(int id){
        Document doc = docs.get(id);
        String docPath = doc.getDBname()+"/"+doc.getCollectionName()+"/"+doc.getName();
        return  "src/main/java/com/example/node/databases/"+docPath+".json";
    }

    public String checkVersion(String file, int id){
        //optimistic locking
        Gson gson = new Gson();
        Map<String, String> map= gson.fromJson(file, new TypeToken<HashMap<String, String>>() {}.getType());
        if (map.containsKey("version")){
            if (Integer.parseInt(map.get("version"))==docs.get(id).getVersion()){
                return map.get("data");
            }
        }
        return "error";
    }
    
    public synchronized void deleteByDatabaseName(String name) throws IOException {
        //get the file that contains the details of all documents
        if (docs.size()==0){
            getDocumentsFile();
        }
        //walk through all elements and delete the objects in that database
        Iterator<Map.Entry<Integer, Document>> itr = docs.entrySet().iterator();
        Queue <Integer> queue = new LinkedList<>();

        while (itr.hasNext()){
            Map.Entry<Integer, Document> entry = itr.next();
            Document doc = entry.getValue();
            if (doc.getDBname().equals(name)){
                queue.add(entry.getKey());
            }
        }
        while (queue.size()>0){
            deleteDocument(queue.remove());
        }
    }

    public synchronized void storeCounterFile(int id) throws IOException {
        fileService.writeFile(docCounterPath, String.valueOf(id));
    }

    public int readCounterFile() throws IOException {
        return Integer.parseInt(fileService.readFile(docCounterPath));
    }

    public String getDocuments() throws IOException {
        return fileService.readFile(documentsPath);
    }
}
