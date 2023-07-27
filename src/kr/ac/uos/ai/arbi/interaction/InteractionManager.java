package kr.ac.uos.ai.arbi.interaction;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.framework.broker.ActiveMQBroker;
import kr.ac.uos.ai.arbi.interaction.proxy.MonitorProxy;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class InteractionManager extends ArbiAgent {

	public static final String interactionManagerURI = "agent://www.arbi.com/interactionManager";
	private static final String interactionManagerDS = "ds://www.arbi.com/interactionManager";

	private static final String CONTEXTMANAGER_ADDRESS = "agent://www.arbi.com/contextManager";
	private static final String CONTEXTMONITORMANAGER_ADDRESS = "agent://www.arbi.com/contextMonitorManager";
	private static final String TASKMANAGER_ADDRESS = "agent://www.arbi.com/taskManager";
	private static final String KNOWLEDGEMANAGER_ADDRESS = "agent://www.arbi.com/knowledgeManager";
	private static final String SERVICEDISPATCHER_ADDRESS = "agent://www.arbi.com/serviceDispatcher";
	///////////////////////////////////////////////////////////////////////////////////
	private MonitorMessageQueue tempMessageQueue;				//temporary code for demo. Remove related code later
	///////////////////////////////////////////////////////////////////////////////////
	private ArrayList<MonitorProxy> monitorProxyList;
	private LogManager logManager;
	
	private MonitorMessageToolkit monitorMessageToolkit;
	
	private ActiveMQBroker activeMQBroker = null;
	
	public InteractionManager() {
		monitorProxyList = new ArrayList<>();
		logManager = new LogManager(this);
		tempMessageQueue = new MonitorMessageQueue();
		initMessageBroker();
		monitorMessageToolkit = new MonitorMessageToolkit(this);
	}
	
	private void initMessageBroker() {
		String activeMQBrokerHost = InteractionManagerBrokerConfiguration.getActiveMQBrokerHost();
		if(activeMQBrokerHost != null) {
			this.activeMQBroker = new ActiveMQBroker(activeMQBrokerHost, InteractionManagerBrokerConfiguration.getActiveMQBrokerPort());
		}
	}
	
	@Override
	public void onStart(){
		if(this.activeMQBroker != null) {
			this.activeMQBroker.start();
		}
		this.send("Server", "(ActivateLogging)");
	}
	
	public void stop() {
		monitorMessageToolkit.stopThread();
	}
	
	/*
	private void initDataSource(String brokerURI, int brokerType) {
		DataSource dc = new DataSource() {
			@Override
			public void onNotify(String content) {
				sendMessage(content);
				System.out.println("[ LogData ]\t" +"<SystemLog>"+"\t" +content);
			}
		};
		dc.connect(brokerURI, interactionManagerDS, brokerType);
		String id = dc.subscribe(
				"(rule (fact (SystemLog $Actor $Type $Action $Content $Time)) --> (notify (SystemLog $Actor $Type $Action $Content $Time)))");
	}
*/
	public void createProxy(String monitorID, String protocol, JSONArray filter) {
		
		for(int i=0; i<monitorProxyList.size(); i++) {
			if(monitorProxyList.get(i).getMonitorID().equals(monitorID))
				return;
		}
		MonitorProxy monitorProxy = new MonitorProxy(monitorID, protocol, filter);
		monitorProxyList.add(monitorProxy);
		String msg = tempMessageQueue.dequeue();
		while(msg != null) {
			this.sendMessage(msg);
			msg = tempMessageQueue.dequeue();
		}
		
	}

	private void changeProxyFilter(String monitorID, JSONArray filter) {

		for (int i = 0; i < monitorProxyList.size(); i++) {
			MonitorProxy monitorProxy = monitorProxyList.get(i);
			if (monitorProxy.getMonitorID().equals(monitorID))
				monitorProxy.setFilter(filter);
		}
	}

	private void deleteProxy(String monitorID) {
		for (int i = 0; i < monitorProxyList.size(); i++) {
			MonitorProxy monitorProxy = monitorProxyList.get(i);
			if (monitorProxy.getMonitorID().equals(monitorID)) {
				JSONArray filter = monitorProxy.getFilterToJSON();
				monitorProxyList.remove(monitorProxy);
				setGlobalFilter(filter);
			}
		}
	}

	@Override
	public void onSystem(String sender, String data) {
		System.out.println(sender + " -> " + data);
		sendMessage(data);

	}
	
	private void sendMessage(String data) {
		
		if(monitorProxyList.size() == 0) {
			tempMessageQueue.enqueue(data);
		}else {
			//System.out.println("************************out filter");
//			System.out.println(data.toString());
			JSONObject message = logManager.logParseToJSON(data);
			String logType = message.get("LogType").toString();
			//System.out.println("[ LogData ]\t" +"<" + logType+ ">"+"\t" +data);
			
			for (int i = 0; i < monitorProxyList.size(); i++) {
				MonitorProxy monitorProxy = monitorProxyList.get(i);
				if (monitorProxy.isFilterON(message)) {
					monitorMessageToolkit.sendMessage(monitorProxy.getMonitorID(), monitorProxy.getMonitorProtocol(),
							message.toJSONString());
				}
			}
		}
		
		
	}

	public void setGlobalFilter(JSONArray filter) {
		logManager.setGlobalFilter(filter);
	}

	public int getProxySize() {
		return this.monitorProxyList.size();
	}

	public boolean checkProxyFilter(int i, JSONObject filterObject) {
		return monitorProxyList.get(i).isFilterON(filterObject);
	}

	public void globalFilterChange(JSONObject filterObject) {
		String logType = filterObject.get("LogType").toString();
		if(logType.toLowerCase().equals("messagelog")) {
			String type = filterObject.get("Actor").toString();
			system(type,filterObject.toJSONString());
		}
		

		/*
		if (actor.equals("TM") || actor.equals("TASKMANAGER")) {
			system(TASKMANAGER_ADDRESS, filterObject.toJSONString());
		} else if (actor.equals("CM") || actor.equals("CONTEXTMANAGER")) {
			system(CONTEXTMANAGER_ADDRESS, filterObject.toJSONString());
		} else if (actor.equals("KM") || actor.equals("KNOWLEDGEMANAGER")) {
			system(KNOWLEDGEMANAGER_ADDRESS, filterObject.toJSONString());
		} else if(actor.equals("CMM") || actor.equals("CONTEXTMONITORMANAGER")) {
			system(CONTEXTMONITORMANAGER_ADDRESS, filterObject.toJSONString());
		} else if(actor.contentEquals("SERVICEDISPATCHER")) {
			system(SERVICEDISPATCHER_ADDRESS, filterObject.toJSONString());
		}*/
	}
	
	public void sendCommand(JSONObject jsonObject) {
		
		String sender = jsonObject.get("Sender").toString();
		//System.out.println("sender = " +sender);
		JSONObject glObject = (JSONObject)jsonObject.get("GeneralizedList");
		GeneralizedList gl = GLFactory.newGLFromJSON(glObject);

		if(gl == null)
			return;
		
		String receiver = jsonObject.get("Receiver").toString();
	
		if(receiver == null)
			return;
		
		String messageType = jsonObject.get("MessageType").toString().toLowerCase();
		
		String result = null;
		if(messageType.equals("request"))
			result = request(receiver, gl.toString());
		else if(messageType.equals("query"))
			result = query(receiver, gl.toString());
		else if(messageType.equals("send"))
			send(receiver, gl.toString());
		else if(messageType.equals("subscribe"))
			result = subscribe(receiver, gl.toString());
		else if(messageType.equals("unsubscribe"))
			unsubscribe(receiver, gl.toString());
		else if(messageType.equals("notify"))
			notify(receiver, gl.toString());
	
		if(result != null) {
			JSONObject resultObject = GLFactory.newJSONObjectFromGLString(result);
			resultObject.put("Sender", receiver);
			resultObject.put("Receiver", sender);
			for(int i=0; i<monitorProxyList.size(); i++) {
				MonitorProxy monitorProxy = monitorProxyList.get(i);
				if(sender.equals(monitorProxy.getMonitorID())) {
					monitorMessageToolkit.sendMessage(monitorProxy.getMonitorID(), monitorProxy.getMonitorProtocol(),
							resultObject.toJSONString());
				}
			}
		}
	}
 
	public void messageRecieved(String monitorAction) {
		try {
			//System.out.println("[ Monitor Action ] " +monitorAction);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(monitorAction);
			String action = jsonObject.get("Action").toString();
			//System.out.println("action : " + action);
			if (action.toLowerCase().equals("create monitor")) {
				String monitorID = jsonObject.get("ID").toString();
				String protocol = jsonObject.get("Protocol").toString();
				JSONArray filter = (JSONArray) jsonObject.get("Filter");
				createProxy(monitorID, protocol, filter);
				setGlobalFilter(filter);
			} else if (action.toLowerCase().equals("change filter")) {
				
				String monitorID = jsonObject.get("ID").toString();
				JSONArray filter = (JSONArray) jsonObject.get("Filter");
				changeProxyFilter(monitorID, filter);
				setGlobalFilter(filter);
			} else if (action.toLowerCase().equals("delete monitor")) {
				String monitorID = jsonObject.get("ID").toString();
				
				deleteProxy(monitorID);
			} else if(action.toLowerCase().equals("command")) {
				sendCommand(jsonObject);
				
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
