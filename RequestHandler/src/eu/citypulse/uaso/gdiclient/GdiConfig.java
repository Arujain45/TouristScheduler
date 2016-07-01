package eu.citypulse.uaso.gdiclient;

public class GdiConfig {
	//public static String GDI_DBNAME ="cp_traffic"; //aarhus and romania
	public static String GDI_DBNAME = "cp_sweden_stockholm"; //Ericson sweden database
			
	public static String GDI_HOST = "localhost";
	public static int GDI_PORT = 5438; //5432 is the standard port, but we use it with a tunnel 
	public static String GDI_USERNAME="wp4";
	public static String GDI_PASSWORD="wp4natss!";
	public static boolean GDI_INSERT_DEBUG_DATA = false; //DB writeback used for research experiements
}

//password for tunnel is cpTunnel0816