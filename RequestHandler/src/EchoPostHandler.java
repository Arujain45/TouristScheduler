import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class EchoPostHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange he) throws IOException {
		System.out.println("RECEIVED echopost REQUEST FROM REMOTE COMPUTER");
		// parse request
		Map<String, Object> parameters = new HashMap<>();
		String url = he.getRequestURI().toString();
		String query = url.substring(url.indexOf('?') + 1);
		System.out.println("Query value is: " + query);
		DataStore.parseQuery(query, parameters);
		// send response
		String response = "TEST SUCCESSFUL FOR POST\n";
		for (String key : parameters.keySet())
			response += key + " = " + parameters.get(key) + "\n";
		he.sendResponseHeaders(200, response.length());
		OutputStream os = he.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}