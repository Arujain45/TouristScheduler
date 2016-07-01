import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Objects.DaySchedule;
import Objects.PointOfInterest;

class EverySportReader{
	
	private static final int DURATION_FOOTBALL = 7200000;
	private static final int DURATION_BANDY = 7200000;
	private static final int DURATION_ICEHOCKEY = 8100000;
	private static final int DURATION_BASKETBALL = 8100000;
	
	static ArrayList<PointOfInterest> getEvents(String startDate, String endDate){

		//MANDATORY PARAMETERS
		String fromDate = "&fromDate=" + startDate;
		String sort = "&sort=startDate:asc";
		String region = "&homeTeam.municipality=Stockholm";
	
		//Extra parameters
		String status = "&status=upcoming"; //not started, upcoming etc
		String toDate = "&toDate=" + endDate;
		
        ArrayList<PointOfInterest> eventList = processJSONResponse();
        if(eventList == null){
        	System.out.println("ERROR: the event response was not read correctly...");
        }
		
		/*try {
            URL url = new URL(EVERYSPORTAPI_URL + "apikey=" + EVERYSPORTAPI_KEY 
            		+ fromDate + sort + region + status + toDate);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
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
                ArrayList<PointOfInterest> eventList = JSONReader.processJSONResponse(stringBuilder.toString());
                if(eventList == null){
                	System.out.println("ERROR: the event response was not read correctly...");
                }
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            System.out.println("ERROR" + e.getMessage() + " <==> " + e);
        }*/
		return eventList;
	}
	
	private static ArrayList<PointOfInterest> processJSONResponse() {
		JSONParser parser = new JSONParser();
		ArrayList<PointOfInterest> eventList= new ArrayList<>(); 
		
        try {
        	//JSONObject jsonObject = (JSONObject) parser.parse(input);
            JSONObject jsonObject = (JSONObject) parser.parse(
            		new InputStreamReader(new FileInputStream(DataStore.EVENT_HANDLER_EVERYSPORT_INPUTFILE),"utf-8"));
 
            JSONArray eventListJSON = (JSONArray) jsonObject.get("events");
            JSONObject event, homeTeam, awayTeam, temp;
            
            for(int i = 0; i < eventListJSON.size(); i++){
            	PointOfInterest poi = new PointOfInterest();
            	Date startTime;
            	poi.setType("Sports Event");
            	
            	event = (JSONObject) eventListJSON.get(i);
            	if(event.get("links") != null){
	            	temp = (JSONObject) ((JSONArray) event.get("links")).get(0);
	            	if(!temp.isEmpty())
	            		poi.setWebsite(temp.get("href").toString());
            	}
            	startTime = parseDate(event.get("startDate").toString());
            	
            	String eventName = "";
            	String eventDescription = "";
            	
            	homeTeam = (JSONObject) event.get("homeTeam");
            	awayTeam = (JSONObject) event.get("visitingTeam");
            	temp = (JSONObject) event.get("league");
            	String sportName = ((JSONObject) temp.get("sport")).get("name").toString();
            	
            	eventName += sportName + ": "
            			+ homeTeam.get("shortName").toString()
            			+ " vs. "
            			+ awayTeam.get("shortName").toString();
            	poi.setName(eventName);
            	
            	configureEventTime(poi, sportName, startTime);
            	
            	String teamClass = temp.get("teamClass").toString().toLowerCase();
            	teamClass = Character.toUpperCase(teamClass.charAt(0)) + teamClass.substring(1);
            	eventDescription += sportName + " (" + teamClass + "): "
            			+ temp.get("name").toString() + "\n"
            			+ homeTeam.get("name").toString() 
            			+ " vs. " + awayTeam.get("name").toString() + "\n";
            	
            	temp = (JSONObject) ((JSONObject) event.get("facts")).get("arena");
            	
            	String venue = temp.get("name").toString();
            	findCoordinates(poi, venue);
            	poi.setAddress(venue);
            	poi.setDescriptionShort("Venue: " + venue);
            	eventDescription += "Venue: " + venue + "\n"
            			+ "Provided by EverySport.com";
            	poi.setDescriptionLong(eventDescription);
            	
            	//poi.printValues();
            	eventList.add(poi);
            }
 
            return eventList;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

	private static void findCoordinates(PointOfInterest poi, String venue) {
		switch(venue.toLowerCase()){
		case "friends arena":
			poi.setLocationLat(59.3727);
			poi.setLocationLong(18.0002);
			break;
		case "tele2 arena":
			poi.setLocationLat(59.2913);
			poi.setLocationLong(18.0840);
			break;
		case "globen":
			poi.setLocationLat(59.2936);
			poi.setLocationLong(18.0836);
			break;
		case "hovet":
			poi.setLocationLat(59.2949);
			poi.setLocationLong(18.0818);
			break;
		case "sp�nga ip":
			poi.setLocationLat(59.3887);
			poi.setLocationLong(17.9036);
			break;
		case "zinkensdamms ip":
			poi.setLocationLat(59.3165);
			poi.setLocationLong(18.0508);
			break;
		case "bergshamra ip":
			poi.setLocationLat(59.3852);
			poi.setLocationLong(18.0262);
			break;
		case "gubb�ngens idrottsplats":
			poi.setLocationLat(59.2626);
			poi.setLocationLong(18.0683);
			break;
		case "t�ljehallen":
			poi.setLocationLat(59.1993);
			poi.setLocationLong(17.6368);
			break;
		case "skytteholms ip":
			poi.setLocationLat(59.3599);
			poi.setLocationLong(17.9931);
			break;
		case "s�dert�lje fotbollsarena":
			poi.setLocationLat(59.1876);
			poi.setLocationLong(17.5704);
			break;
		case "stockholms stadion":
			poi.setLocationLat(59.3453);
			poi.setLocationLong(18.0790);
			break;
		case "kanalplan":
			poi.setLocationLat(59.3063818);
			poi.setLocationLong(18.0850425);
			break;
		default:
			poi.setLocationLat(1.0);
			poi.setLocationLong(1.0);
		}
	}

	private static void configureEventTime(PointOfInterest poi, String sportName, Date startTime) {
		Date endTime;
		switch(sportName.toLowerCase()){
			case "fotboll":
				poi.setVisitTimeInMinutes(DURATION_FOOTBALL / DataStore.MILLISECONDS_TO_MINUTES);
				endTime = new Date(startTime.getTime() + DURATION_FOOTBALL);
				break;
			case "ishockey":
				poi.setVisitTimeInMinutes(DURATION_ICEHOCKEY / DataStore.MILLISECONDS_TO_MINUTES);
				endTime = new Date(startTime.getTime() + DURATION_ICEHOCKEY);
				break;
			case "bandy":
				poi.setVisitTimeInMinutes(DURATION_BANDY / DataStore.MILLISECONDS_TO_MINUTES);
				endTime = new Date(startTime.getTime() + DURATION_BANDY);
				break;
			case "basketball":
				poi.setVisitTimeInMinutes(DURATION_BASKETBALL / DataStore.MILLISECONDS_TO_MINUTES);
				endTime = new Date(startTime.getTime() + DURATION_BASKETBALL);
				break;
			default:
				poi.setVisitTimeInMinutes(DURATION_FOOTBALL / DataStore.MILLISECONDS_TO_MINUTES);
				endTime = new Date(startTime.getTime() + DURATION_FOOTBALL);
				break;
		}
		poi.getSchedule().setDayScheduleAt(new DaySchedule(startTime, endTime), 0);
	}

	private static Date parseDate(String date){
		SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
		try{
			return parser.parse(date);
		} catch(ParseException e){
			e.printStackTrace();
			return null;
		}
	}
}