package kr.ac.uos.ai.arbi.monitor.control.dispatch;

import javax.jms.Message;
import javax.jms.MessageListener;

import kr.ac.uos.ai.arbi.monitor.model.AbstractListModel;
import kr.ac.uos.ai.arbi.monitor.model.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.monitor.model.ArbiAgentMessage.AgentMessageAction;
import kr.ac.uos.ai.arbi.monitor.model.LTMMessage;
import kr.ac.uos.ai.arbi.monitor.model.LTMMessage.LTMMessageAction;
import kr.ac.uos.ai.arbi.monitor.model.URLNameTableModel;

public abstract interface MessageDispatcher{
	
	//public abstract void setAAMessageFilterModel(FilterModel paramFilterModel);

	//public abstract void setAAMessageFilterListModel(AbstractListModel<GeneralizedList> paramAbstractListModel);

	public abstract void setAAURLTableModel(URLNameTableModel paramURLNameTableModel);

	public abstract void setAASequenceModel(AbstractListModel<ArbiAgentMessage> paramAbstractListModel);

	//public abstract void setCDCMessageFilterModel(FilterModel paramFilterModel);

	//public abstract void setCDCMessageFilterListModel(AbstractListModel<GeneralizedList> paramAbstractListModel);

	public abstract void setLTMURLTableModel(URLNameTableModel paramURLNameTableModel);

	public abstract void setLTMSequenceModel(AbstractListModel<LTMMessage> paramAbstractListModel);

	public abstract boolean checkAAFilter(AgentMessageAction paramAction);

	public abstract boolean checkAAContent(String paramString);

	public abstract boolean checkLTMFilter(LTMMessageAction paramAction);

	public abstract boolean checkLTMContent(String paramString);

	public abstract void dispatch(String paramMessage);

	public abstract void addLTMMessageData(String paramString1, String paramString2,
			LTMMessageAction paramAction, String paramString3);

	public abstract void addAAMessageData(String paramString1, String paramString2, AgentMessageAction paramAction,
			String paramString3);
}
