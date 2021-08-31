package kr.ac.uos.ai.arbi.monitor.control.dispatch;

import javax.jms.TextMessage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import kr.ac.uos.ai.arbi.monitor.Utility;
import kr.ac.uos.ai.arbi.monitor.model.LTMMessage.LTMMessageAction;
import kr.ac.uos.ai.arbi.monitor.util.StringUtility;

public class LTMMessageTask  implements DispatchTask {
	private MessageDispatcher dispatcher;

	LTMMessageTask(MessageDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void doTask(String message) {
		String client = null;
		String serverURL = null;
		String content = null;
		LTMMessageAction action = null;
		
		try {
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonMessage = (JSONObject) jsonParser.parse(message);
			
			client = jsonMessage.get("Client").toString();
			serverURL = Utility.LTM_URL;
			content = StringUtility.unescape(jsonMessage.get("Content").toString());
			action = LTMMessageAction.valueOf(jsonMessage.get("Action").toString());
		} catch (Exception e) {
			return;
		}

		this.dispatcher.addLTMMessageData(client, serverURL, action, content);
	}
}