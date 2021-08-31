package kr.ac.uos.ai.arbi.monitor.control.dispatch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.swing.SwingUtilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.ac.uos.ai.arbi.model.Binding;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.monitor.Utility;
import kr.ac.uos.ai.arbi.monitor.model.AbstractListModel;
import kr.ac.uos.ai.arbi.monitor.model.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.monitor.model.ArbiAgentMessage.AgentMessageAction;
import kr.ac.uos.ai.arbi.monitor.model.LTMMessage;
import kr.ac.uos.ai.arbi.monitor.model.LTMMessage.LTMMessageAction;
import kr.ac.uos.ai.arbi.monitor.model.FilterModel;
import kr.ac.uos.ai.arbi.monitor.model.URLNameTableModel;

public class MessageDispatcherAll  implements MessageDispatcher{

	
	private static final GeneralizedList[] GL_NULL_ARRAY = new GeneralizedList[0];
	private FilterModel aaFilterModel;
	private AbstractListModel<GeneralizedList> aaFilterListModel;
	private URLNameTableModel aaURLTableModel;
	private AbstractListModel<ArbiAgentMessage> aaSequenceModel;
	private FilterModel ltmFilterModel;
	private AbstractListModel<GeneralizedList> ltmFilterListModel;
	private URLNameTableModel ltmURLTableModel;
	private AbstractListModel<LTMMessage> ltmSequenceModel;
	private Map<String, DispatchTask> taskMap;
	private MessageAdder<ArbiAgentMessage> aaMessageAdder;
	private MessageAdder<LTMMessage> ltmMessageAdder;

	MessageDispatcherAll() {
		this.taskMap = new ConcurrentHashMap();

	}

	public void setAAMessageFilterModel(FilterModel filterModel) {
		if (filterModel == null)
			return;
		this.aaFilterModel = filterModel;
	}

	public void setAAMessageFilterListModel(AbstractListModel<GeneralizedList> filterListModel) {
		if (filterListModel == null)
			return;
		this.aaFilterListModel = filterListModel;
	}

	public void setAAURLTableModel(URLNameTableModel aaURLTableModel) {
		if (aaURLTableModel == null)
			return;
		this.aaURLTableModel = aaURLTableModel;
	}

	public void setAASequenceModel(AbstractListModel<ArbiAgentMessage> aaSequenceModel) {
		if (aaSequenceModel == null)
			return;
		this.aaSequenceModel = aaSequenceModel;
	}

	public void setLTMMessageFilterModel(FilterModel filterModel) {
		if (filterModel == null)
			return;
		this.ltmFilterModel = filterModel;
	}

	public void setLTMMessageFilterListModel(AbstractListModel<GeneralizedList> filterListModel) {
		if (filterListModel == null)
			return;
		this.ltmFilterListModel = filterListModel;
	}

	public void setLTMURLTableModel(URLNameTableModel ltmURLTableModel) {
		if (ltmURLTableModel == null)
			return;
		this.ltmURLTableModel = ltmURLTableModel;
	}

	public void setLTMSequenceModel(AbstractListModel<LTMMessage> ltmSequenceModel) {
		if (ltmSequenceModel == null)
			return;
		this.ltmSequenceModel = ltmSequenceModel;
	}



	public void dispatch(String log) {


		if (this.aaMessageAdder == null) {
			this.aaMessageAdder = new MessageAdder(this.aaURLTableModel, this.aaSequenceModel);
		}
		if (this.ltmMessageAdder == null) {
			this.ltmMessageAdder = new MessageAdder(this.ltmURLTableModel, this.ltmSequenceModel);
		}

		
		try {
			if (Utility.isNullString(log))
				return;

			//System.out.println("[ LOG ] " + log);

			DispatchTask task = null;

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonMessage = (JSONObject) jsonParser.parse(log);

			if (jsonMessage.get("Type").toString().toLowerCase().equals("agentmessage"))
				task = new AAMessageTask(this);
			else if (jsonMessage.get("Type").toString().toLowerCase().equals("ltmmessage")) {
				task = new LTMMessageTask(this);
			}

			if (task == null)
				return;
			task.doTask(log);

		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addLTMMessageData(String sender, String receiver, LTMMessageAction action, String content) {

		if (this.ltmMessageAdder == null) {
			this.ltmMessageAdder = new MessageAdder(this.ltmURLTableModel, this.ltmSequenceModel);
		}

		LTMMessage m;
		if(action.equals(LTMMessageAction.Result) || action.equals(LTMMessageAction.Notify))
			m = new LTMMessage(receiver, sender, action, content);
		else
			m = new LTMMessage(sender, receiver, action, content);
		
		this.ltmMessageAdder.setMessage(m);
		try {
			SwingUtilities.invokeAndWait(this.ltmMessageAdder);
		} catch (Exception localException) {
		}
	}

	public void addAAMessageData(String sender, String receiver, AgentMessageAction action, String content) {

		if (this.aaMessageAdder == null) {
			this.aaMessageAdder = new MessageAdder(this.aaURLTableModel, this.aaSequenceModel);
		}

		ArbiAgentMessage m = new ArbiAgentMessage(sender, receiver, action, content);
		this.aaMessageAdder.setMessage(m);
		try {
			SwingUtilities.invokeAndWait(this.aaMessageAdder);
		} catch (Exception localException) {
		}
	}

	public boolean checkLTMContent(String content) {
		GeneralizedList predicate = null;
		try {
			predicate = GLFactory.newGLFromGLString(content);
		} catch (Throwable t) {
			return true;
		}

		GeneralizedList[] list = (GeneralizedList[]) this.ltmFilterListModel.toArray(GL_NULL_ARRAY);
		for (GeneralizedList gl : list) {
			Binding binding = gl.unify(predicate, null);
			if (binding != null)
				return true;
		}

		return false;
	}

	public boolean checkLTMFilter(LTMMessageAction action) {
		return (this.ltmFilterModel.isEnabled(action.toString()));
	}

	public boolean checkAAContent(String content) {
		GeneralizedList contentGL = null;
		try {
			contentGL = GLFactory.newGLFromGLString(content);
		} catch (Throwable t) {
			return false;
		}

		GeneralizedList[] list = (GeneralizedList[]) this.aaFilterListModel.toArray(GL_NULL_ARRAY);
		for (GeneralizedList gl : list) {
			Binding binding = gl.unify(contentGL, null);
			if (binding != null)
				return true;
		}

		return false;
	}

	public boolean checkAAFilter(AgentMessageAction action) {
		return (this.aaFilterModel.isEnabled(action.toString()));
	}

}
