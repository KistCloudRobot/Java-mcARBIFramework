package test.demo;

import kr.ac.uos.ai.arbi.ServerLauncher;

public class Lift2RobotServer {
	public static void main(String[] ar) {
		String[] list = new String[1];
		list[0] = "configuration/Lift2RobotServerConfiguration.xml";
		
		ServerLauncher launcher = new ServerLauncher(list);
	}
}
