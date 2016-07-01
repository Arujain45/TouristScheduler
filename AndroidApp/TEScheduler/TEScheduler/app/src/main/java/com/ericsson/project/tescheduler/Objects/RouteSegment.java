package com.ericsson.project.tescheduler.Objects;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import citypulse.commons.data.Coordinate;

/* A RouteSegment instance can represent:
 *  - a PoI: "destination" is its coordinates, "startTime" and "endTime" are the visit period
 *  - a travel segment: "destination" is where that step ends, "startTime" and "endTime"
 *                      are the time interval
 */

public class RouteSegment {
    private List<Coordinate> trajectory;
    private double destinationLat;
    private double destinationLong;
    private Date startTime;
    private Date endTime;
    private String mobility;
    private String line; //line number if public transportation, null otherwise
    private String stationStart;
    private String stationEnd;
    private String direction; //last station name
    private boolean poiSegment;

    public RouteSegment(){
        trajectory = new ArrayList<>();
        destinationLat = -1;
        destinationLong = -1;
        startTime = new Date();
        endTime = new Date();
        mobility = "";
        line = "";
        stationStart = "";
        stationEnd = "";
        direction = "";
        poiSegment = false;
    }

    public List<Coordinate> getTrajectory(){
        return trajectory;
    }

    public void setTrajectory(ArrayList<Coordinate> trajectory){
        this.trajectory = trajectory;
    }

    public void addToTrajectory(Coordinate coordinates){
        this.trajectory.add(coordinates);
    }

    public double getDestinationLat() {
        return destinationLat;
    }

    public double getDestinationLong() {
        return destinationLong;
    }

    public void setDestinationLat(double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public void setDestinationLong(double destinationLong) {
        this.destinationLong = destinationLong;
    }

    public String getMobility() {
        return mobility;
    }

    public void setMobility(String mobility) {
        this.mobility = mobility;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getStationStart() {
        return stationStart;
    }

    public void setStationStart(String stationStart) {
        this.stationStart = stationStart;
    }

    public String getStationEnd() {
        return stationEnd;
    }

    public void setStationEnd(String stationEnd) {
        this.stationEnd = stationEnd;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isPoISegment() {
        return poiSegment;
    }

    public void setPoISegment(boolean poiSegment) {
        this.poiSegment = poiSegment;
    }

    public String getFormattedVisitTime() {
        return formatTime(startTime) + "-" + formatTime(endTime);
    }

    private String formatTime(Date time){
        final String DATE_FORMAT = "HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return dateFormat.format(time.getTime());
    }

    public String getFormattedDuration() {
        double duration = ((endTime.getTime() - startTime.getTime()) / 60000);
        return "(" + String.format(Locale.getDefault(), "%d",(int) Math.ceil(duration)) + "min)";
    }

    public void printValues(){
        Log.w(AppData.LOG_TAG, "(" + destinationLat + "," + destinationLong + ")");
        Log.w(AppData.LOG_TAG, stationStart + " to " + stationEnd);
        Log.w(AppData.LOG_TAG, " --line: " + line + " towards: " + direction);
        Log.w(AppData.LOG_TAG, "Mobility: " + mobility);
        Log.w(AppData.LOG_TAG, getFormattedVisitTime());
        Log.w(AppData.LOG_TAG, "Printing trajectory coordinates: ");
        for(int i = 0; i < trajectory.size(); i++){
            Log.w(AppData.LOG_TAG, trajectory.get(i).toString());
        }
    }
}
