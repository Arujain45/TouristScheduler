package com.ericsson.project.tescheduler.Objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppData {
    private static Map<Integer, PointOfInterest> pointsOfInterest;
    private static ArrayList<Marker> poiMarkers;
    private static ArrayList<Polygon> poiPolygons;
    private static Route resultRoute = new Route();
    public static double budgetAvailable;
    public static double budgetExpenses;
    private static ArrayList<String> addresses;

    public static LatLng currentPosition;

    public static boolean dataInitialized = false;

    public static final float STOCKHOLM_MAP_MIN_ZOOM = 11.0f;
    public static final LatLng STOCKHOLM_CENTRAL_COORDINATES = new LatLng(59.3299775, 18.0576622);
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_EXCEPTION = "cccc, MMMM dd, yyyy";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String LOG_TAG = "Wydad ==> ";

    public static void init(){
        pointsOfInterest = new HashMap<>();
        poiMarkers = new ArrayList<>();
        poiPolygons = new ArrayList<>();
        budgetAvailable = 0;
        budgetExpenses = 0;
        addresses = new ArrayList<>();
        currentPosition = new LatLng(2.8, 2.8);
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

    public static ArrayList<String> getAddresses() {
        return addresses;
    }

    public static void setAddresses() {
        for(int i = 0; i < pointsOfInterest.size(); i++){
            addresses.add(pointsOfInterest.get(i).getName());
        }
    }

    public static LatLng getCurrentPosition() {
        return currentPosition;
    }

    public static void setCurrentPosition(double latitude, double longitude) {
        AppData.currentPosition = new LatLng(latitude, longitude);
    }

    public static ArrayList<Marker> getPoiMarkers() {
        return poiMarkers;
    }

    public static ArrayList<Polygon> getPoiPolygons() {
        return poiPolygons;
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

    public static PointOfInterest findPoIbyName(String query) {
        for(int i = 0; i < pointsOfInterest.size(); i++){
            if(query.toLowerCase().equals(pointsOfInterest.get(i).getName().toLowerCase()))
                return pointsOfInterest.get(i);
        }
        return null;
    }

    /*public static void generateResultRoute(){
        int i = 0;
        resultRoute.getSegmentToPoI().add(new RouteSegment());
        resultRoute.getSegmentAt(i).setDestinationLat(59.3299775);
        resultRoute.getSegmentAt(i).setDestinationLong(18.0576622);
        resultRoute.getSegmentAt(i).setStartTime(new Date(39600000));
        resultRoute.getSegmentAt(i).setEndTime(new Date(39600000));
        resultRoute.getSegmentAt(i).setDirection("Stockholm Centralstation");
        resultRoute.getSegmentAt(i).setPoISegment(true);

        i++;
        resultRoute.getSegmentToPoI().add(new RouteSegment());
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.058517, 59.330287));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.060641, 59.328076));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.065212, 59.325548));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.067599, 59.323174));
        resultRoute.getSegmentAt(i).setDestinationLat(59.323174);
        resultRoute.getSegmentAt(i).setDestinationLong(18.067599);
        resultRoute.getSegmentAt(i).setStartTime(new Date(39600000));
        resultRoute.getSegmentAt(i).setEndTime(new Date(39780000));
        resultRoute.getSegmentAt(i).setMobility("Public Transportation");
        resultRoute.getSegmentAt(i).setLine("T17");
        resultRoute.getSegmentAt(i).setStationStart("Stockholm T-Centralen");
        resultRoute.getSegmentAt(i).setStationEnd("Gamla Stan");
        resultRoute.getSegmentAt(i).setDirection("TerminusTest");
        resultRoute.getSegmentAt(i).setPoISegment(false);

        i++;
        resultRoute.getSegmentToPoI().add(new RouteSegment());
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.067599, 59.323174));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.069579, 59.323988));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.069032, 59.324332));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.069955, 59.324748));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.069171, 59.325769));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.069901, 59.325881));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.069643, 59.326226));
        resultRoute.getSegmentAt(i).setDestinationLat(59.326226);
        resultRoute.getSegmentAt(i).setDestinationLong(18.069643);
        resultRoute.getSegmentAt(i).setStartTime(new Date(39780000));
        resultRoute.getSegmentAt(i).setEndTime(new Date(40200000));
        resultRoute.getSegmentAt(i).setMobility("Walk");
        resultRoute.getSegmentAt(i).setStationEnd("Stockholm Royal Palace");
        resultRoute.getSegmentAt(i).setPoISegment(false);

        i++;
        resultRoute.getSegmentToPoI().add(new RouteSegment());
        resultRoute.getSegmentAt(i).setDestinationLat(59.323165);
        resultRoute.getSegmentAt(i).setDestinationLong(18.067604);
        resultRoute.getSegmentAt(i).setStartTime(new Date(40200000));
        resultRoute.getSegmentAt(i).setEndTime(new Date(43800000));
        resultRoute.getSegmentAt(i).setDirection("Stockholm Royal Palace");
        resultRoute.getSegmentAt(i).setPoISegment(true);

        i++;
        resultRoute.getSegmentToPoI().add(new RouteSegment());
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.069643, 59.326226));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.069273, 59.326967));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.072965, 59.328094));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.073352, 59.32990));
        resultRoute.getSegmentAt(i).setDestinationLat(59.32990);
        resultRoute.getSegmentAt(i).setDestinationLong(18.073352);
        resultRoute.getSegmentAt(i).setStartTime(new Date(43800000));
        resultRoute.getSegmentAt(i).setEndTime(new Date(44280000)); //+8min
        resultRoute.getSegmentAt(i).setMobility("Walk");
        resultRoute.getSegmentAt(i).setStationEnd("Karl XII:s torg");
        resultRoute.getSegmentAt(i).setPoISegment(false);

        i++;
        resultRoute.getSegmentToPoI().add(new RouteSegment());
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.073352, 59.329905));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.071613, 59.332833));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.075357, 59.333002));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.081677, 59.331410));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.093285, 59.331684));
        resultRoute.getSegmentAt(i).setDestinationLat(59.331684);
        resultRoute.getSegmentAt(i).setDestinationLong(18.093285);
        resultRoute.getSegmentAt(i).setStartTime(new Date(44280000));
        resultRoute.getSegmentAt(i).setEndTime(new Date(44640000)); //+6min
        resultRoute.getSegmentAt(i).setMobility("Public Transportation");
        resultRoute.getSegmentAt(i).setLine("B17");
        resultRoute.getSegmentAt(i).setStationStart("Karl XII:s torg");
        resultRoute.getSegmentAt(i).setStationEnd("Djurgårdsbron");
        resultRoute.getSegmentAt(i).setDirection("Ropsten T-bana");
        resultRoute.getSegmentAt(i).setPoISegment(false);

        i++;
        resultRoute.getSegmentToPoI().add(new RouteSegment());
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.093285, 59.331684));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.094156, 59.330419));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.093341, 59.330310));
        resultRoute.getSegmentAt(i).addToTrajectory(new Coordinate(18.092193, 59.328597));
        resultRoute.getSegmentAt(i).setDestinationLat(59.32990);
        resultRoute.getSegmentAt(i).setDestinationLong(18.073352);
        resultRoute.getSegmentAt(i).setStartTime(new Date(44640000));
        resultRoute.getSegmentAt(i).setEndTime(new Date(44880000)); //+4min
        resultRoute.getSegmentAt(i).setMobility("Walk");
        resultRoute.getSegmentAt(i).setStationEnd("Vasa museum");
        resultRoute.getSegmentAt(i).setPoISegment(false);

        i++;
        resultRoute.getSegmentToPoI().add(new RouteSegment());
        resultRoute.getSegmentAt(i).setDestinationLat(59.328075);
        resultRoute.getSegmentAt(i).setDestinationLong(18.091445);
        resultRoute.getSegmentAt(i).setStartTime(new Date(44880000));
        resultRoute.getSegmentAt(i).setEndTime(new Date(52080000));
        resultRoute.getSegmentAt(i).setDirection("Vasa museum");
        resultRoute.getSegmentAt(i).setPoISegment(true);
    }

    private static void generatePoIs() {
        ArrayList<Double> areaVasamuseetLat = new ArrayList<>();
        ArrayList<Double> areaVasamuseetLong = new ArrayList<>();

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

        ArrayList<Price> priceListVasa = new ArrayList<>();
        priceListVasa.add(new Price("Adult", 150));
        priceListVasa.add(new Price("Kid", 80));
        priceListVasa.add(new Price("Student", 100));
        priceListVasa.add(new Price("Senior", 100));
        priceListVasa.add(new Price("Group", 90));

        DaySchedule[] daySchedule = new DaySchedule[7];
        for(int i=0; i<7; i++){
            daySchedule[i] = new DaySchedule(new Date(32400000), new Date(64800000)); //9AM - 6PM
        }
        Schedule scheduleVasa = new Schedule(daySchedule);

        Calendar calVasa = Calendar.getInstance();
        Calendar calVasa1 = Calendar.getInstance();
        Calendar calVasa2 = Calendar.getInstance();
        calVasa.set(2016, Calendar.MARCH, 27);
        calVasa1.set(2016, Calendar.JUNE, 6);
        calVasa2.set(2016, Calendar.DECEMBER, 25);
        scheduleVasa.addToExceptionSchedule(new ExceptionSchedule(calVasa, new Date(28800000), new Date(54000000)));
        scheduleVasa.addToExceptionSchedule(new ExceptionSchedule(calVasa1, new Date(35000000), new Date(60000000)));
        scheduleVasa.addToExceptionSchedule(new ExceptionSchedule(calVasa2, new Date(50000000), new Date(80000000)));

        String descriptionVasa = "Maritime museum with 17th-Century ship. "
                + "It is the only fully preserved ship of its kind in the world.";

        PointOfInterest poi = new PointOfInterest(0, 59.328075, 18.091445, areaVasamuseetLat, areaVasamuseetLong
                , priceListVasa,120 , scheduleVasa, "Vasa museum", "History", "www.vasamuseet.se", "+468-519 548 00"
                , "Galärvarvsvägen 14, 115 21 Stockholm", descriptionVasa, descriptionVasa);

        ArrayList<Double> areaSlottetLat = new ArrayList<>();
        ArrayList<Double> areaSlottetLong = new ArrayList<>();

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

        ArrayList<Price> priceListSlottet= new ArrayList<>();
        priceListSlottet.add(new Price("Adult", 90));
        priceListSlottet.add(new Price("Kid", 30));

        DaySchedule[] dayScheduleSlottet = new DaySchedule[7];
        for(int i=0; i<7; i++){
            dayScheduleSlottet[i] = new DaySchedule(new Date(39600000), new Date(54000000)); // 11AM to 3PM
        }
        Schedule scheduleSlottet = new Schedule(dayScheduleSlottet);

        String descriptionSlottet = "The Royal Palace of Stockholm is His Majesty The King's official residence" +
                " and is also the setting for most of the monarchy's official receptions.";

        PointOfInterest poi2 = new PointOfInterest(1, 59.326878, 18.071946, areaSlottetLat, areaSlottetLong
                , priceListSlottet, 60, scheduleSlottet, "Stockholm Royal Palace", "Art", "www.kungahuset.se"
                , "+468-402 61 30", "Kungliga Slottet S-111 30 Stockholm", descriptionSlottet, descriptionSlottet);

        ArrayList<Double> areaHistoriskaPoILat = new ArrayList<>();
        ArrayList<Double> areaHistoriskaPoILong = new ArrayList<>();

        areaHistoriskaPoILat.add(59.334931);
        areaHistoriskaPoILat.add(59.335259);
        areaHistoriskaPoILat.add(59.334797);
        areaHistoriskaPoILat.add(59.334463);

        areaHistoriskaPoILong.add(18.091144);
        areaHistoriskaPoILong.add(18.089513);
        areaHistoriskaPoILong.add(18.089159);
        areaHistoriskaPoILong.add(18.090725);

        ArrayList<Price> priceListHistoriska = new ArrayList<>();
        priceListHistoriska.add(new Price("Adult", 100));

        DaySchedule[] dayScheduleHistoriska = new DaySchedule[7];
        for(int i=0; i<7; i++){
            dayScheduleHistoriska[i] = new DaySchedule(new Date(39600000), new Date(64800000)); // 11AM to 6PM
        }
        Schedule scheduleHistoriska = new Schedule(dayScheduleHistoriska);

        String descriptionHistoriska = "The Swedish History Museum contains the world’s largest Viking exhibits, " +
                "Sweden’s gold and silver treasures, medieval art, and unique finds from the Battle of Gotland 1361.";

        PointOfInterest poi3 = new PointOfInterest(2, 59.3347, 18.0899, areaHistoriskaPoILat, areaHistoriskaPoILong
                , priceListHistoriska, 60 , scheduleHistoriska, "Historiska Museum", "History", "www.historiska.se"
                , "+468-519 556 00", "Narvavägen 13-17,114 84 Stockholm", descriptionHistoriska, descriptionHistoriska);

        AppData.getPointsOfInterest().put(0,poi);
        AppData.getPointsOfInterest().put(1,poi2);
        AppData.getPointsOfInterest().put(2,poi3);
    }*/
}
