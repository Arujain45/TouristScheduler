package com.ericsson.project.tescheduler.Objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppData {
    private static Map<Integer, PointOfInterest> pointsOfInterest;
    private static ArrayList<Marker> poiMarkers;
    private static ArrayList<Polygon> poiPolygons;
    private static Route resultRoute = new Route();

    public static final float STOCKHOLM_MAP_MIN_ZOOM = 11.0f;
    public static final LatLng STOCKHOLM_CENTRAL_COORDINATES = new LatLng(59.3299775, 18.0576622);
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_EXCEPTION = "cccc, MMMM dd, yyyy";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String LOG_MESSAGE_HEADER = "Wydad ==> ";

    public static int[] getRecommendedVisitTimes() {
        int[] recommendedVisitTimes = new int[pointsOfInterest.size()];
        for(int i=0; i < pointsOfInterest.size(); i++){
            recommendedVisitTimes[i] = pointsOfInterest.get(i).getVisitTimeInMinutes();
        }
        return recommendedVisitTimes;
    }

    public static int getRecommendedVisitTimesAt(int pos){
        return pointsOfInterest.get(pos).getVisitTimeInMinutes();
    }

    public static void init(){
        pointsOfInterest = new HashMap<>();
        poiMarkers = new ArrayList<>();
        poiPolygons = new ArrayList<>();
        generatePoIs();
    }

    public static Map<Integer, PointOfInterest> getPointsOfInterest() {
        return pointsOfInterest;
    }

    public static void setPointsOfInterest(Map<Integer, PointOfInterest> pointsOfInterest) {
        AppData.pointsOfInterest = pointsOfInterest;
    }

    public static PointOfInterest getPointOfInterestAt(int i) {
        return pointsOfInterest.get(i);
    }

    public static Route getResultRoute() {
        return resultRoute;
    }

    public static void setResultRoute(Route resultRoute) {
        AppData.resultRoute = resultRoute;
    }

    private static void generatePoIs() {
        ArrayList<Double> areaSlottetLat = new ArrayList<>();
        ArrayList<Double> areaSlottetLong = new ArrayList<>();
        ArrayList<Double> areaVasamuseetLat = new ArrayList<>();
        ArrayList<Double> areaVasamuseetLong = new ArrayList<>();
        ArrayList<Double> areaFreePoILat = new ArrayList<>();
        ArrayList<Double> areaFreePoILong = new ArrayList<>();
        ArrayList<Price> priceList = new ArrayList<>();
        ArrayList<Price> freePriceList = new ArrayList<>();
        priceList.add(new Price("Adult", 150));
        priceList.add(new Price("Kid", 80));
        priceList.add(new Price("Student", 100));
        priceList.add(new Price("Senior", 100));
        priceList.add(new Price("Group", 90));
        freePriceList.add(new Price("Adult", 0));

        DaySchedule[] daySchedule = new DaySchedule[7];
        for(int i=0; i<7; i++){
            daySchedule[i] = new DaySchedule(new Date(32400000 + i*60000), new Date(64800000 + i*60000));
        }
        Schedule schedule = new Schedule(daySchedule);

        Calendar cal = Calendar.getInstance();
        schedule.addToExceptionSchedule(new ExceptionSchedule(cal, new Date(28800000), new Date(54000000)));
        schedule.addToExceptionSchedule(new ExceptionSchedule(cal, new Date(35000000), new Date(60000000)));
        schedule.addToExceptionSchedule(new ExceptionSchedule(cal, new Date(50000000), new Date(80000000)));

        String dummyDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed" +
                " do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim" +
                " veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo" +
                " consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum" +
                " dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident," +
                " sunt in culpa qui officia deserunt mollit anim id est laborum.";

        areaVasamuseetLat.add(59.328672);
        areaVasamuseetLat.add(59.327777);
        areaVasamuseetLat.add(59.327377);
        areaVasamuseetLat.add(59.327919);
        areaVasamuseetLat.add(59.328672);

        areaVasamuseetLong.add(18.091573);
        areaVasamuseetLong.add(18.090672);
        areaVasamuseetLong.add(18.091842);
        areaVasamuseetLong.add(18.092094);
        areaVasamuseetLong.add(18.091573);

        areaSlottetLat.add(59.327890);
        areaSlottetLat.add(59.326746);
        areaSlottetLat.add(59.325947);
        areaSlottetLat.add(59.326073);
        areaSlottetLat.add(59.325958);
        areaSlottetLat.add(59.326850);
        areaSlottetLat.add(59.327890);

        areaSlottetLong.add(18.072719);
        areaSlottetLong.add(18.069007);
        areaSlottetLong.add(18.069951);
        areaSlottetLong.add(18.070981);
        areaSlottetLong.add(18.071077);
        areaSlottetLong.add(18.074017);
        areaSlottetLong.add(18.072719);

        areaFreePoILat.add(59.330209);
        areaFreePoILat.add(59.328906);
        areaFreePoILat.add(59.328020);
        areaFreePoILat.add(59.328775);
        areaFreePoILat.add(59.330209);

        areaFreePoILong.add(18.078848);
        areaFreePoILong.add(18.076853);
        areaFreePoILong.add(18.078162);
        areaFreePoILong.add(18.079921);
        areaFreePoILong.add(18.078848);

        PointOfInterest poi = new PointOfInterest(59.328075, 18.091445, areaVasamuseetLat, areaVasamuseetLong
                , priceList,120 , schedule, "Vasamuseet", "History", "www.vasamuseet.se", "+468-519 548 00"
                , "Galärvarvsvägen 14, 115 21 Stockholm", dummyDescription, dummyDescription);
        PointOfInterest poi2 = new PointOfInterest(59.326878, 18.071946, areaSlottetLat, areaSlottetLong
                , priceList, 60, schedule, "Stockholm Slottet", "Art", "www.kungahuset.se", "+468-402 61 30"
                , "Kungliga Slottet S-111 30 Stockholm", dummyDescription, dummyDescription);
        PointOfInterest poi3 = new PointOfInterest(59.329016, 18.078548, areaFreePoILat, areaFreePoILong
                , freePriceList, 60 , schedule, "Free PI", "Sightseeing", "www.anotherPOI.se", "+468-519 548 00"
                , "Galï¿½rvarvsvï¿½gen 14, 115 21 Stockholm", dummyDescription, dummyDescription);
        AppData.getPointsOfInterest().put(0,poi);
        AppData.getPointsOfInterest().put(1,poi2);
        AppData.getPointsOfInterest().put(2,poi3);
    }

    public static ArrayList<Marker> getPoiMarkers() {
        return poiMarkers;
    }

    public static void setPoiMarkers(ArrayList<Marker> poiMarkers) {
        AppData.poiMarkers = poiMarkers;
    }

    public static ArrayList<Polygon> getPoiPolygons() {
        return poiPolygons;
    }

    public static void setPoiPolygons(ArrayList<Polygon> poiPolygons) {
        AppData.poiPolygons = poiPolygons;
    }

    public static void addPoIMarker(Marker marker, Polygon polygon) {
        poiMarkers.add(marker);
        poiPolygons.add(polygon);
    }

    public static Marker getPoiMarkerAt(int i) {
        return poiMarkers.get(i);
    }

    public static Polygon getPoiPolygonAt(int i) {
        return poiPolygons.get(i);
    }

    public static int findPoIIndexByMarker(Marker marker) {
        for (int i = 0; i < poiMarkers.size(); i++){
            if(poiMarkers.get(i).getPosition().equals(marker.getPosition())) {
                return i;
            }
        }
        return -1;
    }

    public static int getVisitTimeFromPoI(PointOfInterest poi) {
        for(int i = 0; i < pointsOfInterest.size(); i++){
            if(pointsOfInterest.get(i) == poi){
                return pointsOfInterest.get(i).getVisitTimeInMinutes();
            }
        }
        return -1;
    }
}
