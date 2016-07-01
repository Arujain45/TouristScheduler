package eu.citypulse.uaso.gdiclient.trials;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;

import eu.citypulse.uaso.gdiclient.CpGdiInterface;
import eu.citypulse.uaso.gdiclient.persistdata.CpGdiPersistable;
import eu.citypulse.uaso.gdiclient.routes.CpRouteRequest;

/**
 * Test and Example calass for Geospatial Data Infrastructure(GDI) client of CityPulse 
 * @author Daniel Kuemper, University of Applied Sciences Osnabrueck 
 * @version 0.1 draft
 *
 */
public class CpClientRouteCreator {

	/**
	 * Simple Class that shows functionality of the Requests
	 * @param args are not used
	 */
	public static void main(String[] args) { 

		CpGdiInterface cgi;
		try {
			cgi = new CpGdiInterface();
//			double xMin= 9.86849;
//			double yMin = 56.0123;
//			double xMax = 10.4182;
//			double yMax = 56.3069;
//			
//			Random r = new Random();
//			for (int i = 0; i < 10; i++) {
//				double fromLon= (r.nextDouble()*(xMax-xMin)) + xMin;
//				double fromLat= (r.nextDouble()*(yMax-yMin)) + yMin;
//				double toLon= (r.nextDouble()*(xMax-xMin)) + xMin;
//				double toLat= (r.nextDouble()*(yMax-yMin)) + yMin;
//				int metric = r.nextInt(4);
//				int count = r.nextInt(20)+1;
//				System.out.println("Metric: "+metric+ " Count:"+count+"  From: "+ fromLon +" "+fromLat +" to "+ toLon + " "+ toLat);
//				CpRouteRequest cprr = cgi.getCityRoutes(fromLon,fromLat,toLon,toLat, CpRouteRequest.ROUTE_COST_METRICS[metric], count);
//					
//			}
			
			
			double trafficX= 10.130;
			double trafficY= 56.1665;
			int affected = cgi.updateCostMultiplicatorRadial(trafficX, trafficY, 3000,  CpRouteRequest.ROUTE_COST_METRIC_TIME,15.0);
			System.out.println("Affected: "+affected);
			double xFrom= 10.2290737715;
			double yFrom = 56.01471016241;
			double xTo = 9.924696321;
			double yTo = 56.239317738894;
			long before = System.currentTimeMillis();
				CpRouteRequest cprr = cgi.getCityRoutes(xFrom,yFrom,xTo,yTo, CpRouteRequest.ROUTE_COST_METRIC_TIME, 50);
				System.out.println(cprr);
			long after = System.currentTimeMillis();
			System.out.println(after-before+"ms");
			
			
			
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
