/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package kr.ac.uos.ai.arbi.monitor.gui.main.sequence;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import kr.ac.uos.ai.arbi.monitor.control.GLFormatter;
import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.gui.TextAreaMouseListener;


class MessageContentPanel extends AbstractNullLayoutPanel implements ActionListener {
	private static final long serialVersionUID = -7322468899856239635L;
	private String content;
	private String title;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JCheckBox lineWrapCB;
	private JCheckBox glFormatCB;

	MessageContentPanel(String title, String content) {
		this.content = content;
		this.title = title;
		this.textArea = new JTextArea(GLFormatter.format(this.content));
		this.scrollPane = new JScrollPane(this.textArea);
		this.lineWrapCB = new JCheckBox("Word Wrap", true);
		this.glFormatCB = new JCheckBox("GL Format", true);
	}

	public void initialize() {
		setBorder(new TitledBorder(this.title));

		this.scrollPane.setVerticalScrollBarPolicy(20);
		this.scrollPane.setHorizontalScrollBarPolicy(30);
		this.textArea.setEditable(false);
		this.textArea.setBackground(Color.WHITE);
		this.textArea.setLineWrap(true);
		this.textArea.addMouseListener(new TextAreaMouseListener(this.textArea));

		this.lineWrapCB.setActionCommand("WordWrap");
		this.glFormatCB.setActionCommand("GLFormat");

		add(this.scrollPane);
		add(this.lineWrapCB);
		add(this.glFormatCB);

		this.lineWrapCB.addActionListener(this);
		this.glFormatCB.addActionListener(this);
	}

	protected void setSizeComponent(int width, int height) {
		this.scrollPane.setBounds(10, 20, width - 20, height - 50);

		int cy = height - 25;
		int cwidth = (width - 10) / 2;
		this.lineWrapCB.setBounds(5, cy, cwidth, 15);
		this.glFormatCB.setBounds(5 + cwidth, cy, cwidth, 15);
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if ("WordWrap".equals(command)) {
			this.textArea.setLineWrap(this.lineWrapCB.isSelected());
		} else if (this.glFormatCB.isSelected())
			this.textArea.setText(GLFormatter.format(this.content));
		else
			this.textArea.setText(this.content);
	}
}