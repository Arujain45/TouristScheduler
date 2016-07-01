import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Objects.*;
import eu.citypulse.uaso.gdiclient.CpGdiInterface;
import eu.citypulse.uaso.gdiclient.GdiConfig;

class DataStore {
	static final int PORT = 8000;
	static final String SERVER_IP_ADDRESS = "213.159.185.231";
	//static final String SERVER_IP_ADDRESS = "127.0.0.1";
	static final String CONSTRAINT_SOLVER_CMD = "SchedSolver/distanceScheduling";
	static final String CONSTRAINT_SOLVER_INPUTFILE = "SchedSolver/dtinput";

	static final String DECISION_SUPPORT_ENDPOINT =  "ws://131.227.92.55:8018/websockets/reasoning_request";

	static Map<Integer, PointOfInterest> poiStore = new HashMap<>();
	static Route route = new Route();

	private static final String EVERYSPORTAPI_URL = "http://api.everysport.com/v1/events?";
	private static final String EVERYSPORTAPI_KEY = "";
	static final String EVENT_HANDLER_EVERYSPORT_INPUTFILE = "res/EverysportEvents.json";

	static final int MILLISECONDS_TO_MINUTES = 60000;

	static void initializePoIMap(){
		try {
			CpGdiInterface cgi = new CpGdiInterface(GdiConfig.GDI_HOST, GdiConfig.GDI_PORT, GdiConfig.GDI_DBNAME,
					GdiConfig.GDI_USERNAME, GdiConfig.GDI_PASSWORD);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	static void generatePoIMap(){
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

		String descriptionSlottet = "The Royal Palace of Stockholm is His Majesty The King’s official residence" +
				" and is also the setting for most of the monarchy’s official receptions.";

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

		poiStore.put(poi.getId(),poi);
		poiStore.put(poi2.getId(),poi2);
		poiStore.put(poi3.getId(),poi3);
	}

	static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {

		if (query != null) {
			String pairs[] = query.split("[&]");
			for (String pair : pairs) {
				String param[] = pair.split("[=]");
				String key = null;
				String value = null;
				if (param.length > 0) {
					key = URLDecoder.decode(param[0],
							System.getProperty("file.encoding"));
				}

				if (param.length > 1) {
					value = URLDecoder.decode(param[1],
							System.getProperty("file.encoding"));
				}

				if (parameters.containsKey(key)) {
					Object obj = parameters.get(key);
					if (obj instanceof List<?>) {
						List<String> values = (List<String>) obj;
						values.add(value);

					} else if (obj instanceof String) {
						List<String> values = new ArrayList<>();
						values.add((String) obj);
						values.add(value);
						parameters.put(key, values);
						//System.out.println("Parse(key,value): (" + key + "," + values + ")");
						//System.out.println("Hashmap(key,value): (" + key + "," + parameters.get(key) + ")");
					}
				} else {
					parameters.put(key, value);
					//System.out.println("Parse(key,value): (" + key + "," + value + ")");
					//System.out.println("Hashmap2(key,value): (" + key + "," + parameters.get(key) + ")");
				}
			}
		}
	}

	static void addEvents(ArrayList<PointOfInterest> events) {
		int mapSize = poiStore.size();
		for(int i = 0; i < events.size(); i++){
			poiStore.put(mapSize + i, events.get(i));
		}	
	}
	
	static void printPoIValues(){
		for (Map.Entry<Integer, PointOfInterest> poi : poiStore.entrySet()){
			System.out.println("=========POI Number " + poi.getKey());
			poi.getValue().printValues();
		}
	}

}
