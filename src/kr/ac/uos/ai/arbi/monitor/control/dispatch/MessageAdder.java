package kr.ac.uos.ai.arbi.monitor.control.dispatch;

import kr.ac.uos.ai.arbi.monitor.model.AbstractListModel;
import kr.ac.uos.ai.arbi.monitor.model.Message;
import kr.ac.uos.ai.arbi.monitor.model.URLNameBean;
import kr.ac.uos.ai.arbi.monitor.model.URLNameTableModel;

public class MessageAdder<M extends Message> implements Runnable {
	private URLNameTableModel nameModel;
	private AbstractListModel<M> sequenceModel;
	private M message;

	MessageAdder(URLNameTableModel nameModel, AbstractListModel<M> sequenceModel) {
		this.nameModel = nameModel;
		this.sequenceModel = sequenceModel;
	}

	void setMessage(M message) {
		this.message = message;
	}

	public void run() {
		if ((this.nameModel == null) || (this.sequenceModel == null))
			return;
		if (this.message == null)
			return;

		String sender = this.message.getFrom();
		String receiver = this.message.getTo();

		this.nameModel.addAllRow(new URLNameBean[] { new URLNameBean(sender, ""), new URLNameBean(receiver, "") });
		this.sequenceModel.add(this.message);

		this.message = null;
	}
}
