package test.demo;

import kr.ac.uos.ai.arbi.ServerLauncher;

public class Lift1RobotServer {
	public static void main(String[] ar) {
		String[] list = new String[1];
		list[0] = "configuration/Lift1RobotServerConfiguration.xml";
		
		ServerLauncher launcher = new ServerLauncher(list);
	}
}
