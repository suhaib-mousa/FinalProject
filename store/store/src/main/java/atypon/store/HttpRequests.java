package atypon.store;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequests {
    public static String sendPostRequest(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // Set the request method to POST
        connection.setRequestMethod("POST");

        // Enable input/output streams for the POST request
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();

        // Check if the response code is OK or not
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
            // If response code is not OK, return the response body anyway
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        }
    }

    public static String sendPostRequestWithToken(String url, String token, String requestBody) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // Set the request method to POST
        connection.setRequestMethod("POST");

        // Set the authorization header with the token
        connection.setRequestProperty("Authorization", token);
        connection.setRequestProperty("Accept", "application/json");

        // Set content type header
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        // Encode the request body into bytes using UTF-8
        byte[] requestBodyBytes = requestBody.getBytes("UTF-8");

        // Set the Content-Length header using the byte length of the request body
        connection.setRequestProperty("Content-Length", String.valueOf(requestBodyBytes.length));

        // Enable input/output streams for the POST request
        connection.setDoOutput(true);

        // Write the request body bytes to the output stream
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBodyBytes);
        }

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                return response.toString();
            }
        } else {
            throw new Exception("POST request failed with response code: " + responseCode);
        }
    }
    public static String sendGetRequestWithToken(String url,String token) throws Exception {
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

}
