package kr.ac.uos.ai.arbi.utility;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DebugUtilities {
	private static SimpleDateFormat SDF = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	
	
	public static String getDate() {
		Date date = new Date();
		date.setTime(System.currentTimeMillis());
		return SDF.format(date);
	}
	
	public static void addException(String path, String input) {
		try {
			FileWriter fw = new FileWriter(path,true);
			
			String log = "[ExceptionLog " + getDate() + "]" + input + '\n';

			fw.write(log);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
