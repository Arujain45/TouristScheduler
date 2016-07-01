import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.*;

import citypulse.commons.data.Coordinate;
/*import citypulse.commons.reasoning_request.*;
import citypulse.commons.reasoning_request.Answer;
import citypulse.commons.reasoning_request.concrete.*;
import citypulse.commons.reasoning_request.functional_requirements.*;*/
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import Objects.PointOfInterest;
import Objects.DaySchedule;
import Objects.Request;
import Objects.RouteSegment;
import Objects.GDIEdge;

import eu.citypulse.uaso.gdiclient.CpGdiInterface;
import eu.citypulse.uaso.gdiclient.GdiConfig;
import eu.citypulse.uaso.gdiclient.routes.CpRouteRequest;

class GenerateScheduleHandler implements HttpHandler{
	private Map<Integer, GDIEdge> candidateRoutes = new HashMap<>();
	private CpGdiInterface cgi;

	@Override
	public void handle(HttpExchange t) throws IOException {
		try {
			System.out.println("RECEIVED generate_schedule REQUEST FROM REMOTE COMPUTER");
			Map<String, Object> parameters = new HashMap<>();
			String url = t.getRequestURI().toString();
			String query = url.substring(url.indexOf('?') + 1);
			DataStore.parseQuery(query, parameters);
			buildRequest(parameters);

			//Get the visit time for each PoI
			cgi = new CpGdiInterface(GdiConfig.GDI_HOST, GdiConfig.GDI_PORT, GdiConfig.GDI_DBNAME, GdiConfig.
					GDI_USERNAME, GdiConfig.GDI_PASSWORD);
			String constraintSolverInput = buildConstraintSolverArguments();
			executeConstraintSolver(constraintSolverInput);
			System.out.println("MAIN - executing solver finished");

			//Creates a list of sortedPoIs for simpler iterations to build the route segments
			ArrayList<PointOfInterest> sortedPoIs = Request.sortPoIByVisitTime();

			//Get routing data between PoIs if public transportation is selected
			if (Request.getMobility().compareToIgnoreCase("Public Transportation") == 0) {
				//Initial iteration between user's origin location and first PoI
				ResRobotReader.getRoute(Request.getStartLocationLat(), Request.getStartLocationLong(),
						Request.getPoIAt(0).getLocationLat(), Request.getPoIAt(0).getLocationLong(),
						Request.getPoIAt(0).getVisitTimeStart());
				for (int i = 0; i < Request.getSelectedPoIs().size() - 1; i++) {
					//Loop stops at size-2 since every iteration finds the route i->i+1
					//TODO USE SORTEDPOIS HERE AS WELL
					ResRobotReader.getRoute(Request.getPoIAt(i).getLocationLat(), Request.getPoIAt(i).getLocationLong(),
							Request.getPoIAt(i + 1).getLocationLat(), Request.getPoIAt(i + 1).getLocationLong(),
							Request.getPoIAt(0).getVisitTimeStart());
				}
			} else {
				RouteSegment startingSegment = new RouteSegment();
				startingSegment.setDirection("Starting Location");
				startingSegment.setDestinationLat(Request.getStartLocationLat());
				startingSegment.setDestinationLong(Request.getStartLocationLong());
				startingSegment.setStartTime(Request.getTripStart().getTime());
				startingSegment.setEndTime(Request.getTripStart().getTime());
				startingSegment.setPoISegment(true);
				DataStore.route.appendRouteSegment(startingSegment);

				//using the id of the first PoI
				int firstId = Request.getPoIfromObject(sortedPoIs.get(0)).getId();
				System.out.println("ID ==> " + firstId + "\nPOI ==> " + Request.getPoIAt(firstId).getName());
				//Retrieving the route between the starting location and the first PoI
				CpRouteRequest cprr = cgi.getCityRoutes(Request.getStartLocationLong(), Request.getStartLocationLat(),
						Request.getPoIAt(firstId).getLocationLong(), Request.getPoIAt(firstId).getLocationLat(),
						CpRouteRequest.ROUTE_COST_METRIC_TIME, 1);
				RouteSegment firstSegment = new RouteSegment(
						getTrajectoryFromString(cprr.getFirstRoute().getGeom().getGeometry().getValue()));
				firstSegment.setDirection(Request.getPoIAt(firstId).getName());
				firstSegment.setDestinationLat(Request.getPoIAt(firstId).getLocationLat());
				firstSegment.setDestinationLong(Request.getPoIAt(firstId).getLocationLong());
				firstSegment.setStartTime(Request.getTripStart().getTime());
				System.out.println("Start Time: " + firstSegment.getStartTime());
				Calendar endTime = Calendar.getInstance();
				endTime.setTime(Request.getTripStart().getTime());
				endTime.add(Calendar.SECOND, cprr.getFirstRoute().getTimeS());
				firstSegment.setEndTime(endTime.getTime());
				System.out.println("End Time: " + firstSegment.getEndTime());
				firstSegment.setMobility(Request.getMobility());
				firstSegment.setPoISegment(false);
				DataStore.route.appendRouteSegment(firstSegment);

				RouteSegment firstPoISegment = new RouteSegment();
				firstPoISegment.setDirection(Request.getPoIAt(firstId).getName());
				firstPoISegment.setDestinationLat(Request.getPoIAt(firstId).getLocationLat());
				firstPoISegment.setDestinationLong(Request.getPoIAt(firstId).getLocationLong());
				firstPoISegment.setStartTime(Request.getPoIAt(firstId).getVisitTimeStart().getTime());
				firstPoISegment.setEndTime(Request.getPoIAt(firstId).getVisitTimeEnd().getTime());
				firstPoISegment.setPoISegment(true);
				DataStore.route.appendRouteSegment(firstPoISegment);

				System.out.println("**************************PRINTING CANDIDATE ROUTE TIMES**************************");
				System.out.println("null - " + candidateRoutes.get(1).getTime() + " - " +
						candidateRoutes.get(2).getTime() + "\n" +
						candidateRoutes.get(3).getTime() + " - null - " +
						candidateRoutes.get(5).getTime() + "\n" +
						candidateRoutes.get(6).getTime() + " - " +
						candidateRoutes.get(7).getTime() + " - null");

				int poiNumber = Request.getSelectedPoIs().size();
				for (int i = 0; i < poiNumber-1; i++) {
					int sourceID = Request.getPoIfromObject(sortedPoIs.get(i)).getId();
					int destinationID = Request.getPoIfromObject(sortedPoIs.get(i+1)).getId();
					System.out.println("SOURCE ID ==> " + sourceID  + "\nPOI ==> " + Request.getPoIAt(sourceID).getName());
					System.out.println("DESTINATION ID ==> " + destinationID  + "\nPOI ==> " + Request.getPoIAt(destinationID).getName());
					RouteSegment segment = new RouteSegment(
							getTrajectoryFromString(candidateRoutes.get(sourceID*poiNumber + destinationID).getRoute()));
					segment.setDestinationLat(Request.getPoIAt(destinationID).getLocationLat());
					segment.setDestinationLong(Request.getPoIAt(destinationID).getLocationLong());
					segment.setMobility(Request.getMobility());
					segment.setStartTime(Request.getPoIAt(sourceID).getVisitTimeEnd().getTime());
					System.out.println("Start Time: " + segment.getStartTime());
					endTime.clear();
					endTime.setTime(segment.getStartTime());
					endTime.add(Calendar.MINUTE, candidateRoutes.get(sourceID*poiNumber + destinationID).getTime());
					segment.setEndTime(endTime.getTime());
					System.out.println("End time: " + segment.getEndTime());
					segment.setPoISegment(false);
					segment.setDirection(Request.getPoIAt(destinationID).getName());
					DataStore.route.appendRouteSegment(segment);

					RouteSegment poiSegment = new RouteSegment();
					poiSegment.setDirection(Request.getPoIAt(destinationID).getName());
					poiSegment.setDestinationLat(Request.getPoIAt(destinationID).getLocationLat());
					poiSegment.setDestinationLong(Request.getPoIAt(destinationID).getLocationLong());
					poiSegment.setStartTime(Request.getPoIAt(destinationID).getVisitTimeStart().getTime());
					poiSegment.setEndTime(Request.getPoIAt(destinationID).getVisitTimeEnd().getTime());
					poiSegment.setPoISegment(true);
					DataStore.route.appendRouteSegment(poiSegment);
				}
			}

			// send response
			OutputStream os = t.getResponseBody();
			Gson gson = new GsonBuilder()
					.setDateFormat("MMM dd, yyyy HH:mm:ss")

					.create();
			String response = gson.toJson(DataStore.route);
			System.out.println("\nRESPONSE is\n" + response);
			t.sendResponseHeaders(200, response.getBytes().length);
			os.write(response.getBytes());
			os.close();
			t.close();
			cgi.closeConnection();
		} catch (Exception e){
			e.printStackTrace();
			OutputStream os = t.getResponseBody();
			String response = "ERROR - Could not generate schedule";
			t.sendResponseHeaders(200, response.getBytes().length);
			os.write(response.getBytes());
			os.close();
			t.close();
		}
	}

	private List<Coordinate> getTrajectoryFromString(String data) {
		List<Coordinate> trajectory = new ArrayList<>();
		String[] waypoints = data.substring(1,data.length()-1).split(",");

		for(String waypoint : waypoints){
			String[] coordinate = waypoint.split(" ");
			trajectory.add(new Coordinate(Double.valueOf(coordinate[0]), Double.valueOf(coordinate[1])));
		}
		return trajectory;
	}

	private String buildConstraintSolverArguments() {
		int poiNumber = Request.getSelectedPoIs().size();
		/*String arguments = poiNumber + "\n"
				+ Request.getTripStart().getTime() + "\n"
				+ Request.getTripEnd().getTime() + "\n";*/
		String arguments = poiNumber + "\n"
				+ "480\n"
				+ "1080\n";
		//TODO: change the value from "today" to schedule in day tripStart
		for(PointOfInterest poi : Request.getSelectedPoIs()){
			DaySchedule sched = poi.getSchedule().getTodaySchedule();
			arguments += (sched.getOpeningTime().getTime() / DataStore.MILLISECONDS_TO_MINUTES) + ";"
					+ (sched.getClosingTime().getTime() / DataStore.MILLISECONDS_TO_MINUTES) + ";"
					+ poi.getVisitTimeInMinutes() + ";\n";
		}
		arguments += "#distances & times\n"; //mark the start of cost data
		for (int i = 0; i < poiNumber; i++) {
			for (int j = 0; j < poiNumber; j++) {
				if (i == j) {
					arguments += "0;0;\n";
				} else {
					candidateRoutes.put(i*poiNumber + j, getRoutingDataGDI(i,j));
					if( candidateRoutes.get(i*poiNumber + j).getDistance() == -1 ||
							candidateRoutes.get(i*poiNumber + j).getTime() == -1){
						arguments += "999999;999999;";
					} else {
						arguments += candidateRoutes.get(i * poiNumber + j).getDistance() + ";" +
								candidateRoutes.get(i * poiNumber + j).getTime() + ";\n";
					}
				}
			}
		}
		arguments += "end";
		return arguments;
	}

	private GDIEdge getRoutingDataGDI(int i, int j) {
		PointOfInterest src = Request.getSelectedPoIs().get(i);
		PointOfInterest dst = Request.getSelectedPoIs().get(j);
		try {
			CpRouteRequest cprr = cgi.getCityRoutes(src.getLocationLong(), src.getLocationLat(),
					dst.getLocationLong(), dst.getLocationLat(), CpRouteRequest.ROUTE_COST_METRIC_TIME, 1);
			if(cprr.getFirstRoute() == null){
				return new GDIEdge();
			} else {
				return new GDIEdge(cprr.getFirstRoute().getLengthM(),
						(int) Math.ceil(cprr.getFirstRoute().getTimeS()/60),
						cprr.getFirstRoute().getGeom().getGeometry().getValue());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void executeConstraintSolver(String solverData){
		try
		{
			System.out.println("CSP SOLVER - path: " + System.getProperty("user.dir"));
			Process process = new ProcessBuilder(DataStore.CONSTRAINT_SOLVER_CMD, solverData).start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			process.waitFor();
			String line = reader.readLine(); // reads a dummy first line ("PI Scheduler")
			System.out.println("CSP SOLVER - process finished ==> " + line);
			while ( (line = reader.readLine()) != null && !line.equals("")) {
				System.out.println("CSP SOLVER - result: " + line);
				String[] results = line.split(";");
				Request.getSelectedPoIs().get(Integer.parseInt(results[0])).setVisitTimeStart(Integer.parseInt(results[1]));
				Request.getSelectedPoIs().get(Integer.parseInt(results[0])).setVisitTimeInMinutes(Integer.parseInt(results[2]));
			}
		}
		catch(IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void buildRequest(Map<String, Object> parameters){
		try{
			Request.setStartLocationLat(Double.parseDouble(parameters.get("start_location_latitude").toString()));
			Request.setStartLocationLong(Double.parseDouble(parameters.get("start_location_longitude").toString()));
			Request.setBudget(Double.parseDouble(parameters.get("budget").toString()));
			Request.setNumberOfTravelers(Integer.parseInt(parameters.get("travelers").toString()));
			Request.setMobility(parameters.get("mobility").toString());
			Calendar calStart = Calendar.getInstance();
			calStart.setTimeInMillis(Long.parseLong(parameters.get("trip_start").toString()));
			Request.setTripStart(calStart);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(Long.parseLong(parameters.get("trip_end").toString()));
			Request.setTripEnd(calEnd);
			String[] poiList = parameters.get("poilist").toString().split(";");
			String[] poiDurationList = parameters.get("poi_visitduration").toString().split(";");
			for(int i = 0; i < poiList.length; i++){
				Request.getSelectedPoIs().add(DataStore.poiStore.get(Integer.parseInt(poiList[i])));
				Request.getSelectedPoIs().get(i).setVisitTimeInMinutes(Integer.parseInt(poiDurationList[i]));
			}
			System.out.println("REQUEST BUILDER - Success!");
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	/*private GDIEdge getRoutingDataDS(int i, int j) {
		String requestString;

		PointOfInterest src = Request.getSelectedPoIs().get(i);
		PointOfInterest dst = Request.getSelectedPoIs().get(j);

		// PARAMETERS
		FunctionalParameters requestFunctionalParameters = new FunctionalParameters();
		Coordinate travelPlannerStartingPointCoordinate = new Coordinate(
				src.getLocationLong(), src.getLocationLat());
		Coordinate destinationPointCoordinate = new Coordinate(
				dst.getLocationLong(), dst.getLocationLat());

		requestFunctionalParameters
				.addFunctionalParameter(new FunctionalParameter(
						FunctionalParameterName.STARTING_POINT,
						new StringFunctionalParameterValue(
								travelPlannerStartingPointCoordinate.toString())));

		requestFunctionalParameters
				.addFunctionalParameter(new FunctionalParameter(
						FunctionalParameterName.ENDING_POINT,
						new StringFunctionalParameterValue(
								destinationPointCoordinate.toString())));

		requestFunctionalParameters
				.addFunctionalParameter(new FunctionalParameter(
						FunctionalParameterName.TRANSPORTATION_TYPE,
						new StringFunctionalParameterValue(Request.getMobility())));

		requestFunctionalParameters
				.addFunctionalParameter(new FunctionalParameter(
						FunctionalParameterName.STARTING_DATETIME,
						new StringFunctionalParameterValue(
								Long.toString(Request.getTripStart().getTimeInMillis()))));

		// PREFERENCES
		FunctionalPreferences requestFunctionalPreferences = new FunctionalPreferences();

		if (Request.getMobility().compareToIgnoreCase("Walk") == 0) {
			requestFunctionalPreferences
					.addFunctionalPreference(new FunctionalPreference(2,
							FunctionalPreferenceOperation.MINIMIZE,
							FunctionalConstraintName.DISTANCE));
		}

		else {
			requestFunctionalPreferences
					.addFunctionalPreference(new FunctionalPreference(1,
							FunctionalPreferenceOperation.MINIMIZE,
							FunctionalConstraintName.TIME));
		}

		//CREATE REASONING REQUEST
		ReasoningRequest reasoningRequest = new ReasoningRequest(new User(),
				ARType.TRAVEL_PLANNER, new FunctionalDetails(
				requestFunctionalParameters, new FunctionalConstraints(),
				requestFunctionalPreferences));
		GsonBuilder builder = new GsonBuilder();

		builder.registerTypeAdapter(FunctionalParameterValue.class,
				new FunctionalParameterValueAdapter());
		builder.registerTypeAdapter(FunctionalConstraintValue.class,
				new FunctionalConstraintValueAdapter());
		builder.registerTypeAdapter(Answer.class, new AnswerAdapter());

		Gson gson = builder.create();
		requestString = gson.toJson(reasoningRequest);

		WebSocketBasicClient webSocketBasicClient = new WebSocketBasicClient(
				DataStore.DECISION_SUPPORT_ENDPOINT, requestString);

		String routeReasoningResponse = webSocketBasicClient
				.sendWebsocketRequest();

		System.out.println("DECISION SUPPORT - Travel module: the following response was received from the decision support "
				+ routeReasoningResponse);

		if (routeReasoningResponse == null) {
			System.out.println("DECISION SUPPORT - The routeReasoningResponse message is null");
			return new GDIEdge();
		} else{
			Answers answers = MessageConverters.decisionSupportResponsefromJson(routeReasoningResponse);

			if(!answers.getAnswers().isEmpty()){
				System.out.println("DECISION SUPPORT - Number of answers: " + answers.getAnswers().size());
				AnswerTravelPlanner answerTravelPlanner = (AnswerTravelPlanner) answers.getAnswers().get(0);
				return new GDIEdge((int) answerTravelPlanner.getLength(),
						(int) Math.ceil(answerTravelPlanner.getNumber_of_seconds()/60),
						answerTravelPlanner.getRoute());
			}
			return new GDIEdge();
		}
	}*/
}
