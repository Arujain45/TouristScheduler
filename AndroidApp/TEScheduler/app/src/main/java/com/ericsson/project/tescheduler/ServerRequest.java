package com.ericsson.project.tescheduler;

import android.util.Log;

import com.ericsson.project.tescheduler.Objects.AppData;
import com.ericsson.project.tescheduler.Objects.PointOfInterest;
import com.ericsson.project.tescheduler.Objects.Request;
import com.ericsson.project.tescheduler.Objects.Route;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ServerRequest {
    public static final String SERVER_HOST = "http://129.192.22.189";
    public static final String SERVER_PORT = ":22";

    public static final String SERVICE_GET_POI_DB = "/get_all_poi";
    public static final String SERVICE_GET_SCHEDULE = "/generate_schedule";

    // Basic method to post an HTTP request. Only called locally
    public static String postRequest(String service, String parameters){
        HttpURLConnection connection = null;
        String response = "";

        try{
            URL url = new URL(SERVER_HOST + SERVER_PORT + service);
            byte[] postData = parameters.getBytes(StandardCharsets.UTF_8);
            int contentLength = postData.length;

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "charset=UTF-8");
            connection.setRequestProperty("Content-Length", Integer.toString(contentLength));
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(postData);

            if (connection.getResponseCode() != 200){
                return "FAILED - Server returned: \"" + connection.getResponseCode() + ": " +
                        connection.getResponseMessage() + "\".";
            }
            else {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    response += "\n" + line;
                }
                bufferedReader.close();
                dataOutputStream.close();
                connection.disconnect();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "FAILED - Error occured: \"" + e.toString() + "\".";
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return response;
    }

    //Sends request to load all PoIs from DB, returns PoIs just added to DB
    public static String postGetPoIFromDB(String lastUpdate){
        String service =  SERVICE_GET_POI_DB;
        String parameters = "last_update=" + lastUpdate;

        String response = postRequest(service, parameters);
        if(response.startsWith("FAILED")){
            return response;
        }
        else {
            return String.valueOf(processResponseGetPoIFromDB(response));
        }
    }

    private static boolean processResponseGetPoIFromDB(String response) {
        try {
            Gson gson = new Gson();
            AppData.setResultRoute((Route) gson.fromJson(response, Route.class));
        } catch (ClassCastException e){
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Sends request to load all PoIs from DB, returns PoIs just added to DB
    public static String postGenerateSchedule(){
        String service =  SERVICE_GET_SCHEDULE;
        String[] poiInfo = Request.getFormattedPoIInfo();
        String parameters = "start_location_latitude=" + Request.getStartLocationLat()
                + "&start_location_longitude=" + Request.getStartLocationLong()
                + "&trip_start=" + String.valueOf(Request.getTripStart().getTimeInMillis())
                + "&trip_end=" + String.valueOf(Request.getTripEnd().getTimeInMillis())
                + "&budget=" + Request.getAvailableBudget()
                + "&travelers=" + Request.getNumberOfTravelers()
                + "&mobility=" + Request.getMobility()
                + "&poilist=" + poiInfo[0]
                + "&poi_visitduration=" + poiInfo[1];

        Log.w(AppData.LOG_MESSAGE_HEADER, parameters);

        String response = postRequest(service, parameters);
        if(response.startsWith("FAILED")){
            return response;
        }
        else {
            return String.valueOf(processResponseGenerateSchedule(response));
        }
    }

    private static boolean processResponseGenerateSchedule(String response) {
        try {
            Gson gson = new Gson();
            AppData.setPointsOfInterest((Map<Integer,PointOfInterest>) gson.fromJson(response, Map.class));
        } catch (ClassCastException e){
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
