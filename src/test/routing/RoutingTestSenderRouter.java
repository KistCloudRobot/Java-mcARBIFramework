package test.routing;

import kr.ac.uos.ai.arbi.ServerLauncher;

public class RoutingTestSenderRouter {
	public static void main(String[] ar) {
		String[] list = new String[1];
		list[0] = "configuration/RouterSenderConfiguration.xml";
		
		ServerLauncher launcher = new ServerLauncher(list);
	}
}
