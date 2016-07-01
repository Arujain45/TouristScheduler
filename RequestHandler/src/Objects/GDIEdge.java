package Objects;

import citypulse.commons.data.Coordinate;

import java.util.List;

public class GDIEdge {
    private int distance;
    private int time;
    private String route;
    private List<Coordinate> routeCoordinates;

    public GDIEdge(int distance, int time, String route){
        this.distance = distance;
        this.time = time; //in minutes
        this.route = route;
        this.routeCoordinates = null;
    }

    public GDIEdge(int distance, int time, List<Coordinate> route){
        this.distance = distance;
        this.time = time; //in minutes
        this.routeCoordinates = route;
        this.route = null;
    }

    public GDIEdge(){
        distance = -1;
        time = -1;
        route = "not found";
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
