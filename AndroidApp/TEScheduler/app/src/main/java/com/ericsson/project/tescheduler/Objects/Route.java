package com.ericsson.project.tescheduler.Objects;

import android.util.Log;

import java.util.ArrayList;

public class Route {
    private ArrayList<Double> trajectoryLat;
    private ArrayList<Double> trajectoryLong;
    private ArrayList<RouteSegment> segmentToPoI;

    public Route() {
        this.trajectoryLat = new ArrayList<>();
        this.trajectoryLong = new ArrayList<>();
        this.segmentToPoI = new ArrayList<>();
    }

    public ArrayList<Double> getTrajectoryLat() {
        return trajectoryLat;
    }

    public ArrayList<Double> getTrajectoryLong() {
        return trajectoryLong;
    }

    public void setTrajectoryLat(ArrayList<Double> trajectoryLat) {
        this.trajectoryLat = trajectoryLat;
    }

    public void setTrajectoryLong(ArrayList<Double> trajectoryLong) {
        this.trajectoryLong = trajectoryLong;
    }

    public ArrayList<RouteSegment> getSegmentToPoI() {
        return segmentToPoI;
    }

    public RouteSegment getSegmentAt(int index){
        return segmentToPoI.get(index);
    }

    public void setSegmentToPoI(ArrayList<RouteSegment> segmentToPoI) {
        this.segmentToPoI = segmentToPoI;
    }

    public void printValues(){
        Log.w(AppData.LOG_MESSAGE_HEADER, "Printing the ROAD: ");
        for(int i=0 ; i<trajectoryLat.size() ; i++){
            Log.w(AppData.LOG_MESSAGE_HEADER, "(" + trajectoryLat.get(i)
                    + "," + trajectoryLong.get(i) + ")");
        }
        Log.w(AppData.LOG_MESSAGE_HEADER, "Printing the Segments: ");
        for(int i=0; i<segmentToPoI.size(); i++){
            Log.w(AppData.LOG_MESSAGE_HEADER, "=========Segment" + i + "=========");
            segmentToPoI.get(i).printValues();
        }
    }
}
