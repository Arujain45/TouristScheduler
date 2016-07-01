package com.ericsson.project.tescheduler.Objects;

import android.util.Log;

import java.util.ArrayList;

public class Route {
    private ArrayList<RouteSegment> segmentToPoI;

    public Route() {
        this.segmentToPoI = new ArrayList<>();
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
        Log.w(AppData.LOG_TAG, "Printing the Segments: ");
        for(int i=0; i<segmentToPoI.size(); i++){
            Log.w(AppData.LOG_TAG, "=========Segment" + i + "=========");
            segmentToPoI.get(i).printValues();
        }
    }
}
