package kr.ac.uos.ai.arbi.interaction;

import java.util.HashMap;

import org.json.simple.JSONObject;

import kr.ac.uos.ai.arbi.model.log.SystemLog;

public class GlobalFilter {

	private HashMap<SystemLog, Boolean> systemLogFilter;
	private LogManager logManager;

	public GlobalFilter(LogManager logManager) {
		systemLogFilter = new HashMap<>();
		this.logManager = logManager;
	}

	public void setGlobalFilter(JSONObject filterObject) {

		String logType = filterObject.get("LogType").toString();

		if (logType.toLowerCase().equals("systemlog")) {
			setSystemLogFilter(filterObject);
		}
		
	}

	private void setSystemLogFilter(JSONObject filterObject) {

		String filterActor = filterObject.get("Actor").toString();
		String filterAction = filterObject.get("Action").toString();
		boolean filterFlag = Boolean.parseBoolean(filterObject.get("Flag").toString());

		for (SystemLog systemLog : systemLogFilter.keySet()) {
			
			if (systemLog.getActor().toLowerCase().equals(filterActor.toLowerCase())
					&& systemLog.getAction().toLowerCase().equals(filterAction.toLowerCase())) {

				if (systemLogFilter.get(systemLog) != isGlobalFilterON(filterObject)) {
					systemLogFilter.replace(systemLog, filterFlag);
					logManager.globalFilterChange(filterObject);
					return;
				} else
					return;
			}
		}
		
		systemLogFilter.put(new SystemLog(filterActor, filterAction), filterFlag);
		logManager.globalFilterChange(filterObject);
	}


	private boolean isGlobalFilterON(JSONObject filterObject) {

		for (int i = 0; i < logManager.getInteractionManagerProxySize(); i++) {
			
			if (logManager.checkInteractionManagerProxyFilter(i, filterObject))
				return true;
		}
		return false;
	}

}
