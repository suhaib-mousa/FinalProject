package atypon.store.database;

import atypon.store.HttpRequests;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class DocumentManager {
    @Value("${database}")
    private String DBname;
    public Gson gson = new Gson();
    public static void createDoc(){

    }
    public void addDocument(String nodeUrl, String token, String collection, Map<String, String> document) throws Exception {
        String databaseurl = nodeUrl+"/database/create/Blogs";
        HttpRequests.sendPostRequestWithToken(databaseurl, token,".");

        String url = nodeUrl+"/database/create/Blogs/"+collection+"/"+document.get("name").replace(" ","-");
        String response = HttpRequests.sendPostRequestWithToken(url, token,".");
        int docId = Integer.parseInt(response);

        Map<String , String> res = getDocumentByID(nodeUrl, token, docId);

        if (res.size() !=0){
            //add the new book to document and save it
            Document doc = extractDocumentInformation(res.get("document"));
            int newId = doc.getVersion() + 1;

            Map<Integer,Map<String, String>> newDoc = new HashMap<>();
            newDoc.put(newId, document);
            storeDoc(nodeUrl, token, docId, newDoc, doc.getVersion());
        }
    }
    public void editDocument(String nodeUrl, String token, int docId, Map<Integer,Map<String, String>> editedDoc) throws Exception {

        Map<String , String> res = getDocumentByID(nodeUrl, token, docId);

        if (res.size() !=0){
            //add the new book to document and save it
            Document doc = extractDocumentInformation(res.get("document"));
            int newId = doc.getVersion() + 1;
            if (res.get("data") != null){
                var result = storeDoc(nodeUrl, token, docId, editedDoc, doc.getVersion());
                if(result.startsWith("File was modified by another one.")){
                    throw new IllegalStateException("File was modified by another one.");
                }
            }
        }
    }
    public void deleteDocument(String nodeUrl, String token, int docId) throws Exception {

        Map<String , String> res = getDocumentByID(nodeUrl, token, docId);

        if (res.size() !=0){
            //add the new book to document and save it
            Document doc = extractDocumentInformation(res.get("document"));
            int newId = doc.getVersion() + 1;
            if (res.get("data") != null){
                var result = storeDoc(nodeUrl, token, docId, new HashMap<>(), doc.getVersion());
            }
        }
    }
    public String getCollectionDocs(String collectionName, String nodeUrl, String token) throws Exception {
        String url = nodeUrl + "/CollectionDocuments/" + collectionName;
        String jsonString = HttpRequests.sendGetRequestWithToken(url,token);
        if (jsonString.startsWith("you have to login")){
            return "";
        }
        JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);

        Set<String> keys = jsonObject.keySet();

        JsonObject json = new JsonObject();
        // Iterate over the keys and fetch each document by ID
        for (String key : keys) {
            // Fetch document by ID
            Map<String, String> doc = getDocumentByID(nodeUrl, token, Integer.parseInt(key));

            // Convert data JSON string to JsonObject
            JsonObject dataJson = new Gson().fromJson(doc.get("data"), JsonObject.class);
            // Convert document JSON string to JsonObject
            JsonObject documentJson = new Gson().fromJson(doc.get("document"), JsonObject.class);

            // Create a JsonObject to store both data and document
            JsonObject entryJson = new JsonObject();
            entryJson.add("data", dataJson);
            entryJson.add("document", documentJson);

            // Add entry to the main JsonObject
            json.add(key, entryJson);
        }

        return json.toString();
    }
    public String storeDoc(String nodeUrl, String token, int id,Map<Integer,Map<String, String>> doc,int version) throws Exception {
        Map<String , String> toServer = new HashMap<>();
        toServer.put("data",gson.toJson(doc));
        toServer.put("version",String.valueOf(version));
        //send the document with version to server
        String url = nodeUrl+"/database/edit/"+id;
        return HttpRequests.sendPostRequestWithToken(url, token, gson.toJson(toServer));
    }
    public Map<String,String> getDocumentByID(String nodeUrl, String token, int id) throws Exception {
        String url = nodeUrl+"/database/get/"+id;
        String res = HttpRequests.sendGetRequestWithToken(url,token);
        if (res.startsWith("you have to login")){
            return new HashMap<>();
        }
        return gson.fromJson(res, new TypeToken<HashMap<String, String>>() {}.getType());
    }
    public String getDocumentByIDJson(String nodeUrl, String token, int id) throws Exception {
        String url = nodeUrl+"/database/get/"+id;
        String res = HttpRequests.sendGetRequestWithToken(url,token);
        if (res.startsWith("you have to login")){
            return "{}";
        }
        return gson.toJson(res);
    }
    public Document extractDocumentInformation(String res) throws Exception {
        Document document = gson.fromJson(res, Document.class);
        return document;
    }
}
