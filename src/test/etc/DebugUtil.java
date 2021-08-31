package test.etc;

import kr.ac.uos.ai.arbi.utility.DebugUtilities;

public class DebugUtil {
	public static void main(String[] ar) {
		new DebugUtil();
	}
	
	public DebugUtil() {
		DebugUtilities.addException("debug/debugtest.txt", "testing");
	}
}
