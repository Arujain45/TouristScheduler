import java.io.IOException;
import java.io.OutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class GetPoIListHandler implements HttpHandler{
	@Override
	public void handle(HttpExchange t) throws IOException {
		String response = "";
		try {
			System.out.println("RECEIVED get_poilist REQUEST FROM REMOTE COMPUTER");
			Gson gson = new GsonBuilder()
					.setDateFormat("MMM dd, yyyy HH:mm:ss")
					.create();
			response = gson.toJson(DataStore.poiStore);

			t.getResponseHeaders().set("Content-Type", "charset=UTF-8");
			t.sendResponseHeaders(200, response.getBytes().length);
			OutputStream os = t.getResponseBody();
			System.out.println("writing " + response.getBytes().length + " bytes of data");
			os.write(response.getBytes());
			os.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}