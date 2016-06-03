package com.ericsson.project.tescheduler.Objects;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class Request {
    private static ArrayList<PointOfInterest> selectedPoIs = new ArrayList<>();
    private static ArrayList<Integer> selectedPoIKeys = new ArrayList<>();
    private static double budget = 0;
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
        String[] poiInfo = new String[2];
        for(int i = 0; i < selectedPoIs.size() - 1; i++){
            poiInfo[0] += selectedPoIKeys.get(i) + ";";
            poiInfo[1] += selectedPoIs.get(i).getVisitTimeInMinutes() + ";";
        }
        //One final interation without the semi-column
        poiInfo[0] += selectedPoIKeys.get(selectedPoIs.size()-1);
        poiInfo[1] += selectedPoIs.get(selectedPoIs.size()-1).getVisitTimeInMinutes() + ";";

        return poiInfo;
        /*String listLocations = "", listVisitDurations = "",
                listOpeningTimes = "", listClosingTimes = "";

        if (Request.getSelectedPoIs() != null) {
            listLocations = "[";
            listVisitDurations = "[";
            listOpeningTimes = "[";
            listClosingTimes = "[";

            String[] scheduleHolder = new String[2];
            int PoIListSize = Request.getSelectedPoIs().size();

            // The loop stops at (size-1) to avoid adding a comma ',' to the last element
            for (int i = 0; i < PoIListSize - 1 ; i++) {
                listLocations += Request.getSelectedPoIs().get(i).getFormattedLocation() + ",";
                listVisitDurations += Request.getSelectedPoIs().get(i).getFormattedLocation() + ",";
                scheduleHolder = Request.getSelectedPoIs().get(i).getSchedule()
                        .getFormattedScheduleAt(Request.tripStart, Request.tripEnd);
                listOpeningTimes +=  scheduleHolder[0] + ",";
                listClosingTimes += scheduleHolder[1] + ",";
            }

            // One more iteration for the final element
            listLocations += Request.getSelectedPoIs().get(PoIListSize-1).getFormattedLocation() + ",";
            listVisitDurations += Request.getSelectedPoIs().get(PoIListSize-1).getFormattedLocation() + ",";
            scheduleHolder = Request.getSelectedPoIs().get(PoIListSize-1).getSchedule()
                    .getFormattedScheduleAt(Request.tripStart, Request.tripEnd);
            listOpeningTimes +=  scheduleHolder[0] + ",";
            listClosingTimes += scheduleHolder[1] + ",";

            listLocations += "]";
            listVisitDurations += "]";
            listOpeningTimes += "]";
            listClosingTimes += "]";
        }
        return "{" + listLocations + "," + listVisitDurations + ","
                + listOpeningTimes + "," + listClosingTimes + "}";*/
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

    public static void addSelectedPoI(int pos){
        selectedPoIs.add(AppData.getPointOfInterestAt(pos));
        selectedPoIKeys.add(pos);
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
        Log.w(AppData.LOG_MESSAGE_HEADER, "Budget: " + Double.toString(budget));
        Log.w(AppData.LOG_MESSAGE_HEADER, "Tourists: " + numberOfTravelers);
        Log.w(AppData.LOG_MESSAGE_HEADER, "Start Date: " + tripStart.toString());
        Log.w(AppData.LOG_MESSAGE_HEADER, "End Date: " + tripEnd.toString());
        Log.w(AppData.LOG_MESSAGE_HEADER, "Mobility: " + mobility);
        //Log.w(AppData.LOG_MESSAGE_HEADER, "Start Location: " + startLocation.toString());

        for(int i=0; i<selectedPoIs.size(); i++){
            Log.w(AppData.LOG_MESSAGE_HEADER, "PoI: " + selectedPoIs.get(i).getName()
                    + " (" + selectedPoIs.get(i).getVisitTimeInMinutes() + " min)");
        }
    }
}