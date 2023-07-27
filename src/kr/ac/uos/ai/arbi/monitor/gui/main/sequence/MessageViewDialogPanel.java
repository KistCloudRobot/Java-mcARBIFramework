/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package kr.ac.uos.ai.arbi.monitor.gui.main.sequence;

import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.gui.LabeledTextField;
import kr.ac.uos.ai.arbi.monitor.model.Message;

class MessageViewDialogPanel extends AbstractNullLayoutPanel {
	
	private static final long serialVersionUID = -5547651867664713298L;
	private Message message;
	private LabeledTextField typeTF;
	private LabeledTextField fromTF;
	private LabeledTextField toTF;
	private MessageContentPanel[] contentPanels;

	MessageViewDialogPanel(Message message) {
		this.message = message;
	}

	public void initialize() {
		Enum type = this.message.getType();
		String from = this.message.getFrom();
		String to = this.message.getTo();
		String[] contents = this.message.getContent();
		String[] cTitles = this.message.getContentTitle();

		this.typeTF = new LabeledTextField(type.getDeclaringClass().getSimpleName() + ": ", 70, type.toString());
		this.fromTF = new LabeledTextField("From: ", 70, from);
		this.toTF = new LabeledTextField("To: ", 70, to);
		this.contentPanels = new MessageContentPanel[contents.length];

		add(this.typeTF);
		add(this.fromTF);
		add(this.toTF);

		for (int i = 0; i < contents.length; ++i) {
			this.contentPanels[i] = new MessageContentPanel(cTitles[i], contents[i]);
			this.contentPanels[i].initialize();
			add(this.contentPanels[i]);
		}

		this.typeTF.setTextFieldEditable(false);
		this.fromTF.setTextFieldEditable(false);
		this.toTF.setTextFieldEditable(false);
	}

	protected void setSizeComponent(int width, int height) {
		int tfWidth = width - 20;
		this.typeTF.setBounds(10, 10, tfWidth, 20);
		this.fromTF.setBounds(10, 40, tfWidth, 20);
		this.toTF.setBounds(10, 70, tfWidth, 20);

		int cpHeight = (height - 100) / this.contentPanels.length;
		int cpIX = 100;
		for (int i = 0; i < this.contentPanels.length; ++i)
			this.contentPanels[i].setBounds(10, cpIX + cpHeight * i, tfWidth, cpHeight - 10);
	}
}