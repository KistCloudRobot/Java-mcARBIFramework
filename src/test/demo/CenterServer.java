package test.demo;

import kr.ac.uos.ai.arbi.ServerLauncher;

public class CenterServer {
	public static void main(String[] ar) {
		String[] list = new String[1];
		list[0] = "configuration/CenterServerConfiguration.xml";
		
		ServerLauncher launcher = new ServerLauncher(list);
	}
	
}
