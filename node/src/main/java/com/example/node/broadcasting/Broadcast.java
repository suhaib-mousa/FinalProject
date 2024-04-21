package com.example.node.broadcasting;

import com.example.node.affinity.AffinityManager;
import com.example.node.service.FileService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;

@Getter
@Setter
@Component
public class Broadcast implements IBroadcast{
    private RestTemplate restTemplate = new RestTemplate();
    public static int nodeNumber=Integer.parseInt(System.getenv("nodeNumber"));
    public static int numberOfNodes=Integer.parseInt(System.getenv("numOfNodes"));
    private final String affinityFilePath = "src/main/java/com/example/node/affinity/affinity.json";
    private String docsFilePath = "src/main/java/com/example/node/databases/Documents.json";

    @Autowired
    private FileService fileService;
    @Autowired
    private AffinityManager affinity;

    public void broadcastingURL(String url) throws InterruptedException {
        //broadcast the request url to all nodes
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 1; i <= numberOfNodes; i++) {
            if (i!=nodeNumber){
                String sendedUrl = "http://node"+i+":8080"+url;
                threads.add(new Thread(new Request(sendedUrl,"broadcast")));
            }
        }
        for (Thread th : threads) {
            th.start();
        }
        for (Thread th : threads) {
            th.join();
        }
    }

    public void broadcastEdit(int id, String jsonFile) throws InterruptedException {
        ArrayList<Thread> threads = new ArrayList<>();
        //used to broadcasting edited document
        for (int i = 1; i <= numberOfNodes; i++) {
            if (i!=nodeNumber){
                String sendedUrl = "http://node"+i+":8080/affinity/database/edit/"+id;
                threads.add(new Thread(new Request(sendedUrl,jsonFile)));
            }
        }
        for (Thread th : threads) {
            th.start();
        }
        for (Thread th : threads) {
            th.join();
        }
    }

    public String RedirectToAffinity(int docID, String file) throws IOException {
        //redirect the edited file to his affinity node
        int affinityNum = affinity.GetDocumentAffinity(docID);
        String fullURL = "http://node"+affinityNum+":8080/redirect/"+docID;
        return restTemplate.postForObject(fullURL,file,String.class);
    }
}

class Request implements Runnable{
    private RestTemplate restTemplate = new RestTemplate();
    private String url;
    private String file;

    public Request(String url , String file){
        this.url=url;
        this.file=file;
    }

    @Override
    public void run() {
        restTemplate.postForObject(url,file,String.class);
    }
}
