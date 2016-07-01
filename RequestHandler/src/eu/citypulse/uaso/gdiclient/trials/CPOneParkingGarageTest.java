package eu.citypulse.uaso.gdiclient.trials;

import java.io.IOException;
import java.sql.SQLException;

import eu.citypulse.uaso.gdiclient.CpGdiInterface;
import eu.citypulse.uaso.gdiclient.routes.CpRouteRequest;


public class CPOneParkingGarageTest {
	public static void main(String[] args) throws IOException {

		CpGdiInterface cgi;
		try {
			cgi = new CpGdiInterface();

			
			String startWkt="POINT(10.197 56.1527)";
			String sensors[]=  {"POINT(10.2095997754632 56.1489275059127)","POINT(10.2095997754632 56.1489275059127)","POINT(10.1828416111146 56.1549969139243)","POINT(10.1826163055573 56.15498346926)","POINT(10.1836794662704 56.1498479048318)","POINT(10.1834997582664 56.1498075654196)","POINT(10.1828416111146 56.1549969139243)","POINT(10.1826163055573 56.15498346926)","POINT(10.1836794662704 56.1498479048318)","POINT(10.2095997754632 56.1489275059127)"};
			
			for (int i = 0; i < sensors.length; i++) {
				CpRouteRequest cprr = cgi.getCityRoutes(startWkt,sensors[i],CpRouteRequest.ROUTE_COST_METRIC_DISTANCE,1);
				System.out.println(cprr);
			}
			
			
			cgi.closeConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
