package kr.ac.uos.ai.arbi.interaction;

public class InteractionManagerBrokerConfiguration {
	public static String SocketBroker = null;
	public static String StompBroker = null;
	public static String ZeroMQBroker = null;
	public static String ApolloBroker = null;
	
	public static String getSocketBroker() {
		return SocketBroker;
	}
	public static void setSocketBroker(String socketBroker) {
		SocketBroker = socketBroker;
	}
	public static String getStompBroker() {
		return StompBroker;
	}
	public static void setStompBroker(String activeMQBroker) {
		StompBroker = activeMQBroker;
	}
	public static String getZeroMQBroker() {
		return ZeroMQBroker;
	}
	public static void setZeroMQBroker(String zeroMQBroker) {
		ZeroMQBroker = zeroMQBroker;
	}
	public static String getApolloBroker() {
		return ApolloBroker;
	}
	public static void setApolloBroker(String apolloBroker) {
		ApolloBroker = apolloBroker;
	}
	
}
