package eu.citypulse.uaso.gdiclient.trials;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.postgis.PGgeometry;

import eu.citypulse.uaso.gdiclient.CpGdiInterface;
import eu.citypulse.uaso.gdiclient.objects.CpAmenity;

public class CpExamineDistanceResearch {
	public static void main(String[] args) throws IOException {

		CpGdiInterface cgi;
		try {
			cgi = new CpGdiInterface();

			// has been replaced with samplelocations that ignore the sea
			// double xMin= 9.876;
			// double yMin = 56.0266;
			// double xMax = 10.32;
			// double yMax = 56.30;
			BufferedReader CSVFile = new BufferedReader(new FileReader(
					"/Users/danielk/svn/CityPulseDevelopment/CityPulseGDI/res/200000sampleLocations.csv"));
			String dataRow = CSVFile.readLine();//skip header
			ArrayList<PGgeometry> points = new ArrayList<PGgeometry>();
			while ((dataRow = CSVFile.readLine()) != null) {
				String[] dataArray = dataRow.split(",");
				points.add(cgi.getPointGeometry(Double.parseDouble(dataArray[1]), Double.parseDouble(dataArray[2]), 4326));
			}
			System.out.println("We have "+ points.size()+ "Random Points");
			int searchcount = 100;
			int repetitions = 100;

			for (int repetition=0; repetition<repetitions;repetition++){
				System.out.println("Repetition:"+repetition);
				//String amenities[] = { "parking", "toilets", "waste_basket", "atm", "pharmacy" };
				String amenities[] = { "hospital", "parking", "toilets", "waste_basket", "atm", "pharmacy" };
					for (int a = 0; a < amenities.length; a++) {
					// Random r = new Random();
					//for (int c = 2; c <= 5; c++) {
						int c=5;
						//if (!((amenities[a] == "hospital") && c > 3)) {
							int wrong = 0;
							int right = 0;
							for (int i = repetition*searchcount; i < ((repetition+1)*searchcount); i++) {//points.size(); i++) {
								try {
									System.out.print(i + " ");
									ArrayList<CpAmenity> amens = cgi.getnNextLocationsByAmenity(points.get(i), amenities[a], c, repetition);
									for (int j = 0; j < amens.size(); j++) {
										if (j > 0) {
											if (amens.get(j - 1).getRoute().getLengthM() > amens.get(j).getRoute().getLengthM()) {
												wrong++;
											} else {
												right++;
											}
										}
									}
								} catch (NullPointerException e) {
									// TODO Auto-generated catch block
									// e.printStackTrace();
								}
							}
							System.out.println("\n Amenity:" + amenities[a] + " Count:" + c + " Wrong: " + wrong + " Right:" + right);
						//}
					//}
				}
			}
			// double trafficX= 10.130;
			// double trafficY= 56.1665;
			// int affected = cgi.updateCostMultiplicatorRadial(trafficX,
			// trafficY, 3000, CpRouteRequest.ROUTE_COST_METRIC_TIME,15.0);
			// System.out.println("Affected: "+affected);
			// double xFrom= 10.2290737715;
			// double yFrom = 56.01471016241;
			// double xTo = 9.924696321;
			// double yTo = 56.239317738894;
			// CpRouteRequest cprr = cgi.getCityRoutes(xFrom,yFrom,xTo,yTo,
			// CpRouteRequest.ROUTE_COST_METRIC_TIME, 10);
			// System.out.println(cprr);
			//
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
