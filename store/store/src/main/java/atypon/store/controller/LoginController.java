package atypon.store.controller;

import atypon.store.HttpRequests;
import atypon.store.database.DocumentManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String index() {
        return "login/index";
    }
    @GetMapping("/login-to-node")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String username, @RequestParam String password) {
        try {
            // Send request to server
            String url = "http://localhost:8080/login?username=" + username + "&password=" + password;
            String res = HttpRequests.sendPostRequest(url);

            // Check if the server returns the node URL
            if (res.startsWith("http")) {
                // Extract the node URL and the token
                String[] splitted = res.split(" ");
                String token = splitted[1];
                String nodeUrl = splitted[0];

                // Return the token and node URL to the client
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("token", token);
                responseData.put("nodeUrl", nodeUrl);

                return ResponseEntity.ok(responseData);
            } else {
                // Return the error message from the server
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Error: " + res));
            }
        } catch (Exception e) {
            // Handle exceptions and return error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Error: " + e.getMessage()));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout(String nodeUrl, String token) throws Exception {
        String url = nodeUrl+"/logout";
        HttpRequests.sendPostRequestWithToken(url,token,".");
        return ResponseEntity.ok(true); // Return HTTP 200 OK with true indicating success
    }
}
