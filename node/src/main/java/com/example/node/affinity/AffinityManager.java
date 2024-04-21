package com.example.node.affinity;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import com.example.node.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AffinityManager implements IAffinityManager {
    private static Map<Integer, Integer> docsAffinity = new HashMap<>();//to store the documents and their affinity
    private static int affinityNumber = 1;
    private final String affinityPath = "src/main/java/com/example/node/affinity/affinity.json";
    public static int nodeNumber=Integer.parseInt(System.getenv("nodeNumber"));
    public static int numberOfNodes=Integer.parseInt(System.getenv("numOfNodes"));
    private Gson gson = new Gson();

    private final FileService fileService;

    @Autowired
    public AffinityManager(FileService fileService) {
        this.fileService = fileService;
    }

    public boolean checkDocAffinity(int docID) throws IOException {
        //check if this node is the affinity of document
        if (docsAffinity.size()==0) {
            getAffinity();
        }
        if (docsAffinity.containsKey(docID)) {
            return docsAffinity.get(docID).equals(nodeNumber);
        }
        return false;
    }

    public void addDocumentAffinity(int docID) {
        //add affinity
        docsAffinity.put(docID, affinityNumber);
        affinityNumber = (affinityNumber % numberOfNodes) + 1;
        storeAffinity();
    }

    public void deleteDocumentAffinity(int docID) {
        docsAffinity.remove(docID);
        storeAffinity();
    }

    private void getAffinity() throws IOException {
        String affinity = fileService.readFile(affinityPath);
        docsAffinity = gson.fromJson(affinity, new TypeToken<HashMap<Integer, Integer>>() {
        }.getType());
    }

    private void storeAffinity() {
        //store the document affinity in file and broadcast the file
        try {
            String json = gson.toJson(docsAffinity);
            fileService.writeFile(affinityPath,json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int GetDocumentAffinity(int id) throws IOException {
        if (docsAffinity.size()==0) {
            getAffinity();
        }
        return docsAffinity.get(id);
    }

}
