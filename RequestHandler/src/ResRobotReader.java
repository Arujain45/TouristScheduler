import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import citypulse.commons.data.Coordinate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Objects.Route;
import Objects.RouteSegment;

import javax.xml.crypto.Data;

public class ResRobotReader{
	
	private static final String RESROBOTAPI_URL = "https://api.resrobot.se/trip.json";
	private static final String RESROBOTAPI_KEY = "1d5e3077-705f-4bd5-9f3b-2d034ff98992";
	
	public static void getRoute(double originLatitude, double originLongitude, 
			double destLatitude, double destLongitude, Calendar dateTime ){
		
		//MANDATORY PARAMETERS
		String originLat = "&originCoordLat=" + originLatitude; // Vasa museum 59.328672 , 18.091573
		String originLng = "&originCoordLong=" + originLongitude;
		String destLat = "&destCoordLat=" + destLatitude; // Stockholm Slott 59.327890 , 18.072719
		String destLng = "&destCoordLong=" + destLongitude;
		String date = "&date=" + dateTime.get(Calendar.YEAR) + "-"
							   + (dateTime.get(Calendar.MONTH)+1) + "-"
							   + dateTime.get(Calendar.DAY_OF_MONTH); // FORMAT: yyyy-MM-dd
		String time = "&time=" + dateTime.get(Calendar.HOUR_OF_DAY) + "-"
							   + dateTime.get(Calendar.MINUTE); // FORMAT: HH:mm
		
		//EXTRA PARAMETERS
		String language = "&lang=en";
		String originWalk = "&originWalk=1,0,1000,100";
		String destWalk = "&destWalk=1,0,1000,100";
		String numberOfResults = "&numF=1";
		
		/* originWalk and destWalk:
		 * - 1: boolean, turns on/off the option to walk before/after a result trip
		 * - 0: minimum distance that can be walked
		 * - 500: max distance that can be walked (minimize for elders, increase for youth etc)
		 * - 100: % of the default walking speed, which is 5km/h
		 */
		
		try {
            URL url = new URL(RESROBOTAPI_URL + "?key=" + RESROBOTAPI_KEY + language 
            		+ originLat + originLng + destLat + destLng + date + time
            		+ originWalk + destWalk + numberOfResults);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "charset=UTF-8"); //ISO-8859-1 for swedish letters
            
            try {
                BufferedReader bufferedReader = new BufferedReader(
                		new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                processJSONResponse(stringBuilder.toString());
                if(DataStore.route == null){
                	System.out.println("ERROR: the route response was not read correctly...");
                }
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            System.out.println("ERROR" + e.getMessage() + " <==> " + e);
        }
		
	}
	
	private static void processJSONResponse(String input) {
		JSONParser parser = new JSONParser();
		 
        try {
        	JSONObject jsonObject = (JSONObject) parser.parse(input);
            /*JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(
                    "/Users/eilygar/Desktop/APItestResult.json"));*/

            RouteSegment segment;
            JSONArray tripList = (JSONArray) jsonObject.get("Trip");
            JSONArray legList;
            JSONObject trip, leg, temp;
            
            for(int i = 0; i < tripList.size(); i++){
            	trip = (JSONObject) tripList.get(i);
            	trip = (JSONObject) trip.get("LegList");
            	legList = (JSONArray) trip.get("Leg");
            	for(int j = 0; j < legList.size(); j++){
            		segment = new RouteSegment();
            		System.out.println("LEG " + j + ":");
            		leg = (JSONObject) legList.get(j);
            		
            		if(leg.containsKey("direction")){
            			segment.setDirection(leg.get("direction").toString());
            		}
            		
            		segment.setMobility(leg.get("type").toString().toLowerCase());
            		
            		//Add segment info about the origin
            		temp = (JSONObject) leg.get("Origin");
            		if(i == 0 && j == 0){
            			segment.getTrajectory()
								.add(new Coordinate((double) temp.get("lon"), (double) temp.get("lat")));
            		}
            		segment.setStationStart(temp.get("name").toString());
            		segment.setStartTimeFromString(temp.get("time").toString());
            		
            		//Add segment info about the destination
            		temp = (JSONObject) leg.get("Destination");
            		segment.setDestinationLat((double) temp.get("lat"));
            		segment.setDestinationLong((double) temp.get("lon"));
            		segment.setStationEnd(temp.get("name").toString());
            		segment.setEndTimeFromString(temp.get("time").toString());
            		
            		if(leg.containsKey("Product")){
            			temp = (JSONObject) leg.get("Product");
            			segment.setMobility(temp.get("catOutL").toString().toLowerCase());
            			segment.setLine(temp.get("num").toString());
            		}
            		
            		if(leg.containsKey("Stops")){
            			temp = (JSONObject) leg.get("Stops");
            			JSONArray stops = (JSONArray) temp.get("Stop");
            			// k loop has range [1,size-1) to avoid duplicating the first and last values in route 
            			for(int k = 1; k < stops.size(); k++){
            				temp = (JSONObject) stops.get(k);
							segment.getTrajectory()
									.add(new Coordinate((double) temp.get("lon"), (double) temp.get("lat")));
            			}
            		} else{
						segment.getTrajectory()
								.add(new Coordinate((double) temp.get("lon"), (double) temp.get("lat")));
            		}

					DataStore.route.appendRouteSegment(segment);
            	}
            }
            DataStore.route.getSegmentAt(DataStore.route.getSegmentToPoI().size()-1).setPoISegment(true);
            DataStore.route.printValues();

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}