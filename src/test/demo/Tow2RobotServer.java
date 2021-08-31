package test.demo;

import kr.ac.uos.ai.arbi.ServerLauncher;

public class Tow2RobotServer {
	public static void main(String[] ar) {
		String[] list = new String[1];
		list[0] = "configuration/Tow2RobotServerConfiguration.xml";
		
		ServerLauncher launcher = new ServerLauncher(list);
	}
}
