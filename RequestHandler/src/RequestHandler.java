import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class RequestHandler {

	public static void main(String[] args) throws IOException {
		System.out.println("Initializing server...");
		DataStore.generatePoIMap();
		//DataStore.initializePoIMap();
		HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getByName(DataStore.SERVER_IP_ADDRESS), DataStore.PORT), 0);
		System.out.println("IP ADDRESS IS: " + server.getAddress().getHostName());
		server.createContext("/", new TestHandler());
		server.createContext("/generate_schedule", new GenerateScheduleHandler());
		server.createContext("/get_poilist", new GetPoIListHandler());
		server.createContext("/echoPost", new EchoPostHandler());
		server.setExecutor(null);
		server.start();
		System.out.println("Server started at port " + DataStore.PORT);
		
		// Periodically get list of sports events
		/*ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		Runnable task = () -> {
		    try {
		    	System.out.println("Fetching new events...");
		    	DateFormat shortFormat = DateFormat.getDateInstance(DateFormat.SHORT);
		    	Date startDate = Calendar.getInstance().getTime();
		    	Calendar cal = Calendar.getInstance();
		    	cal.add(Calendar.DAY_OF_MONTH, 1);
		    	Date endDate = cal.getTime();
		        DataStore.addEvents(EverySportReader.getEvents(shortFormat.format(startDate), shortFormat.format(endDate)));
				System.out.println("New events added to PoI list");
				//DataStore.printPoIValues();
		    }
		    catch (Exception e) {
		        System.err.println("task interrupted");
		    }
		};
		executor.scheduleWithFixedDelay(task, 0, 1, TimeUnit.DAYS);*/
		
	}

	private static class TestHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			System.out.println("RECEIVED REQUEST FROM REMOTE COMPUTER");
			String response = "Test Successful";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}
}
