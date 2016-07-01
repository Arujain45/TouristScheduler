package eu.citypulse.uaso.gdiclient.trials;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;

import eu.citypulse.uaso.gdiclient.CpGdiInterface;
import eu.citypulse.uaso.gdiclient.persistdata.CpGdiPersistable;
import eu.citypulse.uaso.gdiclient.routes.CpRouteRequest;

/**
 * Test and Example calass for Geospatial Data Infrastructure(GDI) client of
 * CityPulse
 * 
 * @author Daniel Kuemper, University of Applied Sciences Osnabrueck
 * @version 0.1 draft
 *
 */
public class CpClientDeepSearch {

	/**
	 * Simple Class that shows functionality of the Requests
	 * 
	 * @param args
	 *            are not used
	 */
	public static void main(String[] args) {

		CpGdiInterface cgi;
		try {
			cgi = new CpGdiInterface();
			cgi.resetCostMultiplicators(CpRouteRequest.ROUTE_COST_METRIC_TIME);

			double trafficX = 10.130;
			double trafficY = 56.1665;
			int targetNode = cgi.getNearestNodeID(trafficX, trafficY);
			int idx =0;
			cgi.depthSearch(idx++,targetNode);
			cgi.depthSearch(idx++,4179);
			cgi.depthSearch(idx++,4122);
			cgi.depthSearch(idx++,17289);
			cgi.depthSearch(idx++,25560);
			cgi.depthSearch(idx++,3786);
			cgi.depthSearch(idx++,1027);
			cgi.depthSearch(idx++,29085);

			
			
			double xMin= 9.86849;
			double yMin = 56.0123;
			double xMax = 10.4182;
			double yMax = 56.3069;
			
//			Random r = new Random();
//			for (int i = idx; i < 11; i++) {
//				double fromLon= (r.nextDouble()*(xMax-xMin)) + xMin;
//				double fromLat= (r.nextDouble()*(yMax-yMin)) + yMin;
//				targetNode = cgi.getNearestNodeID(fromLon, fromLat);
//				cgi.depthSearch(i,targetNode);
//			}
			
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
