package Objects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class Request {
	private static ArrayList<PointOfInterest> selectedPoIs = new ArrayList<>();
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

	public static String getFormattedStartLocation() {
		return "(" + startLocationLat + "," + startLocationLong + ")";
	}

	public boolean isBudgetOK(){
		double remainingBudget = budget;
		for(PointOfInterest temp : selectedPoIs) {
			remainingBudget -= ( temp.getPriceByCategory("Adult") * numberOfTravelers);
		}
		return remainingBudget >= 0;
	}

	public static ArrayList<PointOfInterest> getSelectedPoIs() {
		return selectedPoIs;
	}
	
	public static PointOfInterest getPoIAt(int pos){
		return selectedPoIs.get(pos);
	}

	public static PointOfInterest getPoIfromObject(PointOfInterest poi){
		return selectedPoIs.get(selectedPoIs.indexOf(poi));
	}

	public static void setSelectedPoIs(ArrayList<PointOfInterest> selectedPoIs) {
		Request.selectedPoIs = selectedPoIs;
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
				}
			}
		}
	}

	public static void printValues() {
		System.out.println("Printing Request values...");
		System.out.println("Budget: " + Double.toString(budget));
		System.out.println("Tourists: " + numberOfTravelers);
		System.out.println("Start Date: " + tripStart.getTime());
		System.out.println("End Date: " + tripEnd.getTime());
		System.out.println("Mobility: " + mobility);
		System.out.println("Start Location: (" + startLocationLat + "," + startLocationLong + ")");

		for (PointOfInterest selectedPoI : selectedPoIs) {
			System.out.println("PoI: " + selectedPoI.getName()
					+ " (" + selectedPoI.getVisitTimeInMinutes() + " min)");
		}
	}

	public static ArrayList<PointOfInterest> sortPoIByVisitTime() {
		ArrayList<PointOfInterest> sortedPoIs = new ArrayList<>(selectedPoIs);
		Collections.sort(sortedPoIs, new Comparator<PointOfInterest>() {
			@Override public int compare(PointOfInterest poi1, PointOfInterest poi2) {
				return poi1.getVisitTimeStart().compareTo(poi2.getVisitTimeStart()); // Ascending
			}
		});
		return sortedPoIs;
	}
}