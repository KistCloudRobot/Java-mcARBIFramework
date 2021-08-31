package test.demo;

import kr.ac.uos.ai.arbi.ServerLauncher;

public class LocalServer {
	public static void main(String[] ar) {
		String[] list = new String[1];
		list[0] = "configuration/LocalServerConfiguration.xml";
		
		ServerLauncher launcher = new ServerLauncher(list);
	}
}
