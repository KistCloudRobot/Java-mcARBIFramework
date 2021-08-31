package test.routing;

import kr.ac.uos.ai.arbi.ServerLauncher;

public class RoutingTestReceiveRouter {
	public static void main(String[] ar) {
		String[] list = new String[1];
		list[0] = "configuration/RouterReceiverConfiguration.xml";
		
		ServerLauncher launcher = new ServerLauncher(list);
	}
}
