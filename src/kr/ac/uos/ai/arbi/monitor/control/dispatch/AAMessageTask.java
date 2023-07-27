package kr.ac.uos.ai.arbi.monitor.control.dispatch;

import javax.jms.MapMessage;
import javax.jms.TextMessage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import kr.ac.uos.ai.arbi.monitor.model.ArbiAgentMessage.AgentMessageAction;
import kr.ac.uos.ai.arbi.monitor.util.StringUtility;


public class AAMessageTask implements DispatchTask {
	private MessageDispatcher dispatcher;

	AAMessageTask(MessageDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void doTask(String message) {
		String sender = null;
		String receiver = null;
		String content = null;
		AgentMessageAction action = null;

		try {

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonMessage = (JSONObject) jsonParser.parse(message);

			sender = jsonMessage.get("Sender").toString();
			receiver = jsonMessage.get("Receiver").toString();
			content = StringUtility.unescape(jsonMessage.get("Content").toString());
			action = AgentMessageAction.valueOf(jsonMessage.get("Action").toString());
		} catch (Exception e) {
			return;
		}

		this.dispatcher.addAAMessageData(sender, receiver, action, content);
	}
}