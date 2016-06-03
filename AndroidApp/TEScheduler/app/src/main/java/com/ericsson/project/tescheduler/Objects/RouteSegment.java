package com.ericsson.project.tescheduler.Objects;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* A RouteSegment instance can represent:
 *  - a PoI: "destination" is its coordinates, "startTime" and "endTime" are the visit period
 *  - a travel segment: "destination" is where that step ends, "startTime" and "endTime"
 *                      are the time interval
 */

public class RouteSegment {
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
        double duration = ((endTime.getTime() - startTime.getTime()) / 6000);
        Log.w(AppData.LOG_MESSAGE_HEADER, Double.toString(duration));
        return Double.toString(duration);
    }

    public void printValues(){
        Log.w(AppData.LOG_MESSAGE_HEADER, "(" + destinationLat + "," + destinationLong + ")");
        Log.w(AppData.LOG_MESSAGE_HEADER, stationStart + " to " + stationEnd);
        Log.w(AppData.LOG_MESSAGE_HEADER, " --line: " + line + " towards: " + direction);
        Log.w(AppData.LOG_MESSAGE_HEADER, "Mobility: " + mobility);
        Log.w(AppData.LOG_MESSAGE_HEADER, getFormattedVisitTime());
    }
}
