package kr.ac.uos.ai.arbi.monitor.gui.messagetest;

import javax.swing.SwingUtilities;

import kr.ac.uos.ai.arbi.framework.center.LTMNotificationHandler;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.monitor.gui.ScrolledTextArea;
import kr.ac.uos.ai.arbi.monitor.model.LTMMessage.LTMMessageAction;

public class LTMMessageSender implements Runnable {

	private DataSource ds;
	private ScrolledTextArea textArea;
	private LTMMessageAction type;
	private String arbiURL;
	private String contentGL;
	private String contentString;
//	private GeneralizedList newGL;
//	private LTMMessageAction performative;
//	private String receiverURL;
	private LTMNotificationHandler handler;

	LTMMessageSender(DataSource ds, LTMMessageAction type, String arbiURL, ScrolledTextArea textArea) {
		this.ds = ds;
		this.type = type;
		this.arbiURL = arbiURL;
		this.textArea = textArea;
	}

	void setContentGLString(String gl) {
		this.contentGL = gl;
	}
	
//	void setNewGL(GeneralizedList newGL) {
//		if (newGL == null)
//			return;
//		this.newGL = newGL;
//	}
//
//	void setSendParameters(LTMMessageAction performative, String receiverURL) {
//		if (performative == null)
//			return;
//		if (receiverURL == null)
//			return;
//
//		this.performative = performative;
//		this.receiverURL = receiverURL;
//	}

//	void setNotificationHandler(LTMNotificationHandler handler) {
//		if (handler == null)
//			return;
//		this.handler = handler;
//	}

	void setContentString(String contentString) {
		if ((contentString == null) || ("".equals(contentString)))
			return;
		this.contentString = contentString;
	}

	public void run() {
		String response = "";
		try {
			switch (type) {
//			case RequestStream:
//				this.ds.registerStream(contentGL.toString());
//				break;
			case AssertFact:
				this.ds.assertFact(contentGL.toString());
				break;
			case RetrieveFact:
				response = this.ds.retrieveFact(contentGL.toString());
				break;
			case UpdateFact:
				this.ds.updateFact(contentGL.toString());
				break;
			case RetractFact:
				response = this.ds.retractFact(contentGL.toString());
				break;
			case Match:
				response = this.ds.match(contentGL.toString());
				break;
			case Subscribe:
				response = this.ds.subscribe(contentGL.toString());
				break;
			case Unsubscribe:
				this.ds.unsubscribe(contentGL.toString());
				break;
			}
		} catch (Exception e) {
			return;
		}

		if (response == null)
			response = "";

		StringBuilder builder = new StringBuilder();

		String m = this.textArea.getText();
		builder.append(m);
		if (!("".equals(m)))
			builder.append("\n\n");
		builder.append("type:\t").append(this.type).append("\n");

		if (this.type == LTMMessageAction.UpdateFact) {
			builder.append("content:\t").append(this.contentGL).append("\n");
//			builder.append("new gl:\t").append(this.newGL).append("\n");
		} else if (this.type == LTMMessageAction.Match) {
			builder.append("pattern:\t").append(this.contentGL).append("\n");
		} else if (this.type == LTMMessageAction.Subscribe) {
			builder.append("rule:\t").append(this.contentGL).append("\n");
		} else if (this.type == LTMMessageAction.Unsubscribe) {
			builder.append("s-id:\t").append(this.contentString).append("\n");
		} 
//		else if (this.type == LTMMessageAction.Push) {
//		builder.append("performative:\t").append(this.performative).append("\n");
//		builder.append("receiver url:\t").append(this.receiverURL).append("\n");
//		builder.append("content:\t").append(this.contentGL).append("\n");
//	}
		else {
			builder.append("content:\t").append(this.contentGL).append("\n");
		}
		
		builder.append("response:\t").append(response).append("\n");

		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				LTMMessageSender.this.textArea.setText(builder.toString());
				LTMMessageSender.this.textArea.repaint();
			}
		});
	}
}
