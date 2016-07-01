package com.ericsson.project.tescheduler.Objects;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class Request {
    private static ArrayList<PointOfInterest> selectedPoIs = new ArrayList<>();
    private static ArrayList<Integer> selectedPoIKeys = new ArrayList<>();
    private static double budget = -1;
    private static int numberOfTravelers = 1;
    private static Calendar tripStart = Calendar.getInstance();
    private static Calendar tripEnd = Calendar.getInstance();
    private static String mobility = "";
    private static double startLocationLat = 0;
    private static double startLocationLong = 0;

    public static void reset() {
        selectedPoIs.clear();
        budget = -1; //initial value is -1, hides the budget attribute from the map panel
        numberOfTravelers = 1;
        tripStart = Calendar.getInstance();
        tripEnd = Calendar.getInstance();
        mobility = "";
        startLocationLat = 0;
        startLocationLong = 0;
    }

    public static String[] getFormattedPoIInfo() {
        String[] poiInfo = {"",""};
        for(int i = 0; i < selectedPoIs.size() - 1; i++){
            poiInfo[0] += selectedPoIKeys.get(i) + ";";
            poiInfo[1] += selectedPoIs.get(i).getVisitTimeInMinutes() + ";";
        }
        //One final interation without the semi-column
        poiInfo[0] += selectedPoIKeys.get(selectedPoIs.size()-1);
        poiInfo[1] += selectedPoIs.get(selectedPoIs.size()-1).getVisitTimeInMinutes();

        return poiInfo;
    }

    /*public static String getFormattedStartLocation() {
        return "(" + startLocation.latitude + "," + startLocation.longitude + ")";
    }*/

    public static ArrayList<Integer> getSelectedPoIKeys() {
        return selectedPoIKeys;
    }

    public static void setSelectedPoIKeys(ArrayList<Integer> selectPoIKeys) {
        Request.selectedPoIKeys = selectPoIKeys;
    }

    public boolean isBudgetOK(){
        double remainingBudget = budget;
        for(int i = 0; i < selectedPoIs.size(); i++) {
            remainingBudget -= ( selectedPoIs.get(i).getPriceByCategory("Adult") * numberOfTravelers);
        }
        return remainingBudget >= 0;
    }

    public static ArrayList<PointOfInterest> getSelectedPoIs() {
        return selectedPoIs;
    }

    public static void setSelectedPoIs(ArrayList<PointOfInterest> selectedPoIs) {
        Request.selectedPoIs = selectedPoIs;
    }

    public static boolean addSelectedPoI(int pos){
        if(!selectedPoIs.contains(AppData.getPointOfInterestAt(pos))) {
            selectedPoIs.add(AppData.getPointOfInterestAt(pos));
            selectedPoIKeys.add(pos);
            return true;
        }
        return false;
    }

    public static double getBudget() {
        return budget;
    }

    public static double getAvailableBudget(){
        double remainingBudget = budget;
        for(PointOfInterest temp : selectedPoIs) {
            remainingBudget -= ( temp.getPriceByCategory("Adult") * numberOfTravelers);
        }
        return remainingBudget;
    }

    public static void setBudget(double budget) {
        Request.budget = budget;
    }

    public static int getNumberOfTravelers() {
        return numberOfTravelers;
    }

    public static void setNumberOfTravelers(int numberOfTravelers) {
        Request.numberOfTravelers = numberOfTravelers;
    }

    public static Calendar getTripStart() {
        return tripStart;
    }

    public static void setTripStart(Calendar tripStart) {
        Request.tripStart = tripStart;
    }

    public static Calendar getTripEnd() {
        return tripEnd;
    }

    public static void setTripEnd(Calendar tripEnd) {
        Request.tripEnd = tripEnd;
    }

    public static String getMobility() {
        return mobility;
    }

    public static void setMobility(String mobility) {
        Request.mobility = mobility;
    }

    public static double getStartLocationLat() {
        return startLocationLat;
    }

    public static double getStartLocationLong() {
        return startLocationLong;
    }

    public static void setStartLocationLat(double startLocationLat) {
        Request.startLocationLat = startLocationLat;
    }

    public static void setStartLocationLong(double startLocationLong) {
        Request.startLocationLong = startLocationLong;
    }

    public static void removeCancelledPoIs(ArrayList<PointOfInterest> cancelledPoIs){
        if(!cancelledPoIs.isEmpty()){
            for (int i = 0; i < cancelledPoIs.size(); i++){
                if(Request.getSelectedPoIs().contains(cancelledPoIs.get(i))){
                    Request.getSelectedPoIs().remove(i);
                    Request.getSelectedPoIKeys().remove(i);
                }
            }
        }
    }

    public static void printValues() {
        Log.w(AppData.LOG_TAG, "Budget: " + Double.toString(budget));
        Log.w(AppData.LOG_TAG, "Tourists: " + numberOfTravelers);
        Log.w(AppData.LOG_TAG, "Start Date: " + tripStart.getTime().toString());
        Log.w(AppData.LOG_TAG, "End Date: " + tripEnd.getTime().toString());
        Log.w(AppData.LOG_TAG, "Mobility: " + mobility);
        Log.w(AppData.LOG_TAG, "Start Location: (" + startLocationLat + "," + startLocationLong + ")");

        for(int i=0; i<selectedPoIs.size(); i++){
            Log.w(AppData.LOG_TAG, "PoI: " + selectedPoIs.get(i).getName()
                    + " (" + selectedPoIs.get(i).getVisitTimeInMinutes() + " min)");
        }
    }
}