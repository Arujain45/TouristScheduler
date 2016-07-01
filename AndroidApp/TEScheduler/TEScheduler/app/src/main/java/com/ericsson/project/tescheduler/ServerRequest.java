package com.ericsson.project.tescheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerRequest {
    public static final String SERVER = "http://213.159.185.231:8000";

    // Basic method to send an HTTP request. Only called locally
    public static String postRequest(String service, String parameters) {
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();
        try{
            URL url = new URL(SERVER + service + "?" + parameters);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            if(connection.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line;
                while (null != (line = br.readLine())) {
                    response.append(line);
                }
            }
            else return null;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return response.toString();
    }
}
