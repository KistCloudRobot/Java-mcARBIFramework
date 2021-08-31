package kr.ac.uos.ai.arbi.monitor;

public class Utility {

	public static final String LTM_URL = "arbi.server";
	
	public static final String[] STR_NULL_ARRAY = new String[0];

	public static boolean isNullString(String str) {
		if (str == null)
			return true;
		return ("".equals(str));
	}
}
