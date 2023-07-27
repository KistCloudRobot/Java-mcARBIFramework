package kr.ac.uos.ai.arbi.monitor.gui.main;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import kr.ac.uos.ai.arbi.monitor.RMC;



public class AboutDialog  extends JDialog {
	private static final long serialVersionUID = 4210945493178049030L;
	private static final int DIALOG_WIDTH = 340;
	private static final int DIALOG_HEIGHT = 260;
	private JPanel panel;
	//private ImageComponent logoImage;
	private JTextArea textArea;

	AboutDialog(JFrame frame) {
		super(frame);
		this.panel = new JPanel();
		//this.logoImage = new ImageComponent("about_logo.png");
		this.textArea = new JTextArea();
	}

	void init() {
		initDialog();
		initPanel();
		initLogo();
		initTextArea();
		initSouthPanel();
	}

	private void initDialog() {
		setTitle("RSIF Monitor ����");
		setSize(340, 260);
		setResizable(false);
		setModal(true);
	}

	private void initPanel() {
		this.panel.setLayout(null);
		this.panel.setBackground(Color.WHITE);
		add(this.panel, "Center");
	}

	private void initLogo() {
		//this.logoImage.setBounds(0, 50, 90, 90);
		//this.panel.add(this.logoImage);
	}

	private void initTextArea() {
		this.textArea.setEditable(false);
		this.textArea.setBorder(null);
		this.textArea.setBounds(100, 10, 220, 160);
		this.textArea.setText(getAboutString());
		this.textArea.setLineWrap(true);
		this.panel.add(this.textArea);
	}

	private void initSouthPanel() {
		JPanel cPanel = new JPanel();
		cPanel.setLayout(new FlowLayout(2, 10, 10));
		JButton okButton = new JButton("Ȯ��");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutDialog.this.setVisible(false);
			}
		});
		cPanel.add(okButton);
		add(cPanel, "South");
	}

	private String getAboutString() {
		StringBuilder sb = new StringBuilder();
		sb.append("RSIF Monitor\n");
		sb.append("\n");
		sb.append("����:     ").append(RMC.RMC_VERSION_AS_STRING).append("\n");
		sb.append("������: ").append("2008 12�� 30��\n");
		sb.append("������: ").append("jetzt").append("\n");
		sb.append("�Ҽ�:     ����ø����б� �ΰ����ɿ�����\n");
		sb.append("\n");

		Calendar c = Calendar.getInstance();
		sb.append("�� 2008-").append(c.get(1)).append("��� ���۱��� �����ڿ��� �ֽ��ϴ�.\n");
		return sb.toString();
	}

	public void setVisible(boolean b) {
		if (b) {
			int ox = getOwner().getX();
			int oy = getOwner().getY();
			int ow = getOwner().getWidth();
			int oh = getOwner().getHeight();

			int x = ox;
			int y = oy;
			if (ow > 340)
				x = ox + (ow - 340) / 2;
			if (oh > 260)
				y = oy + (oh - 260) / 2;

			setLocation(x, y);
		}

		super.setVisible(b);
	}
}