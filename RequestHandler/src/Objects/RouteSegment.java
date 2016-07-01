package Objects;

import citypulse.commons.data.Coordinate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public RouteSegment(List<Coordinate> trajectory){
        this.trajectory = trajectory;
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

    public List<Coordinate> getTrajectory() {
        return trajectory;
    }

    public void setTrajectory(ArrayList<Coordinate> trajectory) {
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

    public void setStartTimeFromString(String startTime) {
        this.startTime = parseDate(startTime);
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setEndTimeFromString(String endTime) {
        this.endTime = parseDate(endTime);
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
        return Double.toString(duration);
    }
    
    public Date parseDate(String date){
		SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
		try{
			return parser.parse(date);
		} catch(ParseException e){
			e.printStackTrace();
			return null;
		}
	}
    
    public void printValues(){
		System.out.println("(" + destinationLat + "," + destinationLong + ")");
		System.out.println(stationStart + " to " + stationEnd);
		System.out.println(" --line: " + line + " towards: " + direction);
		System.out.println("Mobility: " + mobility);
		System.out.println(getFormattedVisitTime());
        System.out.println("Printing trajectory coordinates: ");
        for(int i = 0; i < trajectory.size(); i++){
            System.out.println(trajectory.get(i).toString());
        }
    }
}
