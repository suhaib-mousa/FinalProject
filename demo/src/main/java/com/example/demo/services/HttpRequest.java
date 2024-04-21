package com.example.demo.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {

    public String sendGetRequestWithToken(String url,String token) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // Set the request method to GET
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", token);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            throw new Exception("GET request failed with response code: " + responseCode);
        }
    }

    public String sendPostRequest(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // Set the request method to POST
        connection.setRequestMethod("POST");

        // Enable input/output streams for the POST request
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            throw new Exception("POST request failed with response code: " + responseCode);
        }
    }

    public String sendPostRequestWithToken(String url, String token, String requestBody) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // Set the request method to POST
        connection.setRequestMethod("POST");

        // Set the authorization header with the token
        connection.setRequestProperty("Authorization", token);
        connection.setRequestProperty("Accept", "application/json");


        // Set content type and content length headers
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", String.valueOf(requestBody.length()));

        // Enable input/output streams for the POST request
        connection.setDoOutput(true);

        // Write the request body to the output stream
        OutputStream os = connection.getOutputStream();
        os.write(requestBody.getBytes());
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            throw new Exception("POST request failed with response code: " + responseCode);
        }
    }
}
