/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package kr.ac.uos.ai.arbi.monitor.gui.main.sequence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import kr.ac.uos.ai.arbi.monitor.Utility;
import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.model.AbstractListModel;

class FilterListPanel extends AbstractNullLayoutPanel implements ActionListener {
	private static final long serialVersionUID = -6231922134035057663L;
	private static final int BTN_WIDTH = 100;
	private static final int TEXT_FEILD_HEIGHT = 20;
	private AbstractListModel<GeneralizedList> filterListModel;
	private JList filterList;
	private JScrollPane filterScrollPane;
	private JTextField textField;
	private JButton addBtn;
	private JButton removeBtn;

	FilterListPanel(AbstractListModel<GeneralizedList> filterListModel) {
		this.filterListModel = filterListModel;
		this.filterList = new JList(this.filterListModel);
		this.filterScrollPane = new JScrollPane(this.filterList, 22, 31);
		this.textField = new JTextField();
		this.addBtn = new JButton("ADD");
		this.removeBtn = new JButton("REMOVE");
	}

	public void initialize() {
		this.filterScrollPane.setBounds(0, 0, 200, 55);
		this.textField.setBounds(0, 60, 200, 20);
		this.removeBtn.setBounds(210, 20, 100, 35);
		this.addBtn.setBounds(210, 65, 100, 35);

		add(this.filterScrollPane);
		add(this.textField);
		add(this.removeBtn);
		add(this.addBtn);

		this.removeBtn.setActionCommand("REMOVE");
		this.addBtn.setActionCommand("ADD");

		this.removeBtn.addActionListener(this);
		this.addBtn.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		String command = button.getActionCommand();
		if ("ADD".equals(command)) {
			addFileter();
		} else {
			if (!("REMOVE".equals(command)))
				return;
			removeFilter();
		}
	}

	private void addFileter() {
		new Thread() {
			public void run() {
				String text = FilterListPanel.this.textField.getText();
				if (Utility.isNullString(text)) {
					FilterListPanel.this.showWarning("�߰��� filter GL�� �Է��� �ּ���.");
					return;
				}
				try {
					GeneralizedList gl = GLFactory.newGLFromGLString(text);
					SwingUtilities.invokeLater(new FilterGLAdder(FilterListPanel.this.filterListModel, gl));
				} catch (ParseException e) {
					FilterListPanel.this.showWarning("GL Syntax�� �߸��Ǿ����ϴ�.");
					return;
				}
				FilterListPanel.this.textField.setText("");
			}
		}.start();
	}

	private void removeFilter() {
		int index = this.filterList.getSelectedIndex();
		SwingUtilities.invokeLater(new FilterGLRemover(this.filterListModel, index));
	}

	protected void setSizeComponent(int width, int height) {
		int sWidth = width - 100 - 10;
		int sHeight = height - 25;
		this.filterScrollPane.setSize(sWidth, sHeight);

		int tY = sHeight + 5;
		this.textField.setBounds(0, tY, sWidth, 20);

		int bX = sWidth + 10;
		int bHeight = (height - 10) / 2;
		this.removeBtn.setBounds(bX, 0, 100, bHeight);
		this.addBtn.setBounds(bX, bHeight + 10, 100, bHeight);
	}
}