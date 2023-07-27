package kr.ac.uos.ai.arbi.interaction;

public class InteractionManagerBrokerConfiguration {
	private static String socketBrokerHost = null;
	private static Integer socketBrokerPort = null;
	private static String activeMQBrokerHost = null;
	private static Integer activeMQBrokerPort = null;
	private static String zeroMQBrokerHost = null;
	private static Integer zeroMQBrokerPort = null;
//	private static String apolloBroker = null;
	
	public static String getSocketBrokerHost() {
		return socketBrokerHost;
	}
	
	public static void setSocketBrokerHost(String socketBrokerHost) {
		InteractionManagerBrokerConfiguration.socketBrokerHost = socketBrokerHost;
	}
	
	public static int getSocketBrokerPort() {
		return socketBrokerPort;
	}
	
	public static void setSocketBrokerPort(int socketBrokerPort) {
		InteractionManagerBrokerConfiguration.socketBrokerPort = socketBrokerPort;
	}
	
	public static String getActiveMQBrokerHost() {
		return activeMQBrokerHost;
	}
	
	public static void setActiveMQBrokerHost(String activeMQBrokerHost) {
		InteractionManagerBrokerConfiguration.activeMQBrokerHost = activeMQBrokerHost;
	}
	
	public static int getActiveMQBrokerPort() {
		return activeMQBrokerPort;
	}
	
	public static void setActiveMQBrokerPort(int activeMQBrokerPort) {
		InteractionManagerBrokerConfiguration.activeMQBrokerPort = activeMQBrokerPort;
	}

	
	public static String getZeroMQBrokerHost() {
		return zeroMQBrokerHost;
	}
	
	public static void setZeroMQBrokerHost(String zeroMQBrokerHost) {
		InteractionManagerBrokerConfiguration.zeroMQBrokerHost = zeroMQBrokerHost;
	}
	
	public static int getZeroMQBrokerPort() {
		return zeroMQBrokerPort;
	}
	
	public static void setZeroMQBrokerPort(int zeroMQBrokerPort) {
		InteractionManagerBrokerConfiguration.zeroMQBrokerPort = zeroMQBrokerPort;
	}
//	public static String getApolloBroker() {
//		return ApolloBroker;
//	}
//	public static void setApolloBroker(String apolloBroker) {
//		ApolloBroker = apolloBroker;
//	}
	
}
