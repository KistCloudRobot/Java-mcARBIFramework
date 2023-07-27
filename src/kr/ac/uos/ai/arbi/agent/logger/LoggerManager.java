package kr.ac.uos.ai.arbi.agent.logger;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.logger.action.AgentAction;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class LoggerManager {

	private static LoggerManager instance;
	public static final String INTERACTION_MANAGER_ADDRESS = "agent://www.arbi.com/interactionManager";
	
	private String actor;
	private HashMap<String, AgentAction> actionMap;
	private ArbiAgent agent;
	
	
	private LoggerManager() {
		this.actionMap = new HashMap<>();
	}
		
	public static LoggerManager getInstance() {
		if(instance == null) 
			instance = new LoggerManager();
		
		return instance;
	}
	
	public void initLoggerManager(String agentURI, ArbiAgent arbiAgent) {
		this.agent = arbiAgent;
		this.actor = agentURI;	
	}
	
	public void registerAction(AgentAction action, LogTiming logTiming) {
		AgentAction foundAction = actionMap.get(action.getActionName());
		if(foundAction == null) {
			action.initLoggingFunction(this.agent, this.actor, logTiming);
			actionMap.put(action.getActionName(), action);
		}
	}
	
	public void registerAction(AgentAction action) {
		this.registerAction(action,LogTiming.NonAction);
	}
	
	public void freeAction(String actionName) {
		if (actionMap.containsKey(actionName))
			actionMap.remove(actionName);

	}

	public AgentAction getAction(String actionName) {

		if(actionMap.containsKey(actionName))
			return actionMap.get(actionName);
		
		return null;
	}
	
	public void changeFilterOption(String data) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject filter;
			filter = (JSONObject) parser.parse(data);

			String action = filter.get("Action").toString().replaceAll("\"", "");
			boolean flag = Boolean.parseBoolean(filter.get("Flag").toString().replaceAll("\"", ""));
			AgentAction foundAction = actionMap.get(action.toLowerCase());
			if(foundAction != null) {
				foundAction.changeAction(flag);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
