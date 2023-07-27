/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package kr.ac.uos.ai.arbi.monitor.gui.main.sequence;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.model.Message;


class MessageViewDialog extends JDialog {
	private static final long serialVersionUID = -5462051497797321055L;
	private static final int DEFAULT_DIALOG_WIDTH = 400;
	private static final int DEFAULT_DIALOG_HEIGHT = 400;

	public MessageViewDialog(Message message) {
		initialize(message);
	}

	public MessageViewDialog(Dialog owner, Message message) {
		super(owner);
		initialize(message);
	}

	public MessageViewDialog(Frame owner, Message message) {
		super(owner);
		initialize(message);
	}

	public MessageViewDialog(Window owner, Message message) {
		super(owner);
		initialize(message);
	}

	private void initialize(Message message) {
		setSize(400, 400);
		setTitle("Message View Dialog");

		AbstractNullLayoutPanel panel = new MessageViewDialogPanel(message);
		panel.initialize();
		add(panel, "Center");

		JPanel cPanel = new JPanel();
		cPanel.setLayout(new FlowLayout(1));
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MessageViewDialog.this.setVisible(false);
			}
		});
		cPanel.add(okButton);
		add(cPanel, "South");
	}
}