package kr.ac.uos.ai.arbi.utility;

public class Configuration {
	
	private static boolean LOG_AVAILABILITY = false;
	
	public static void setLogAvailability(boolean isAvailable) {
		Configuration.LOG_AVAILABILITY = isAvailable;
	}
	
	public static boolean getLogAvailability() {
		return Configuration.LOG_AVAILABILITY;
	}
}
