package kr.ac.uos.ai.arbi.monitor.gui.init;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import kr.ac.uos.ai.arbi.monitor.Utility;
import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.gui.LabeledTextField;


public class InitFramePanel extends AbstractNullLayoutPanel {

	private static final long serialVersionUID = -7507437941582523972L;
	private static final int LABEL_WIDTH = 90;
	private static final int BTN_WIDTH = 90;
	private static final int BTN_HEIGHT = 25;
	private JPanel settingPanel;
	private LabeledTextField brokerURLTF;
	private LabeledTextField monitorID;
	private LabeledTextField propertiesTF;
	private JButton connectBtn;
	private JButton exitBtn;

	public InitFramePanel() {
		
		this.settingPanel = new JPanel();
		this.brokerURLTF = new LabeledTextField("JMS Broker : ", "tcp://localhost:61613");
		this.monitorID = new LabeledTextField("MonitorID : ", "ArbiFrameWorkMonitor");
		this.propertiesTF = new LabeledTextField("Properties: ", "./conf/monitor-setting.xml");
		this.connectBtn = new JButton("CONNECT");
		this.exitBtn = new JButton("EXIT");
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.DARK_GRAY, 1));

		initExitButton();
		initSettingPanel();

		add(this.settingPanel);
		add(this.connectBtn);
		add(this.exitBtn);
	}

	public void setBrokerAddress(String ip) {
		if (Utility.isNullString(ip))
			return;
		this.brokerURLTF.setText(ip);
	}

	public void setPropertiesFilename(String filename) {
		if (Utility.isNullString(filename))
			return;
		this.propertiesTF.setText(filename);
	}

	private void initSettingPanel() {
		this.settingPanel.setLayout(null);
		this.settingPanel.setBorder(new TitledBorder(""));
		this.settingPanel.setOpaque(false);

		this.brokerURLTF.setOpaque(false);
		this.brokerURLTF.setLabelWidth(150);

		this.monitorID.setOpaque(false);
		this.monitorID.setLabelWidth(150);

		this.propertiesTF.setOpaque(false);
		this.propertiesTF.setLabelWidth(150);

		this.settingPanel.add(this.brokerURLTF);
		this.settingPanel.add(this.monitorID);
		this.settingPanel.add(this.propertiesTF);
	}

	private void initExitButton() {
		this.exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	protected void setSizeComponent(int width, int height) {
		
		this.settingPanel.setBounds(10, 20, width - 20, 100);

		int tfWidth = this.settingPanel.getWidth() - 20;
		this.brokerURLTF.setBounds(10, 10, tfWidth, 20);
		this.monitorID.setBounds(10, 40, tfWidth, 20);
		this.propertiesTF.setBounds(10, 70, tfWidth, 20);

		this.connectBtn.setBounds(width - 190 - 10 , height - 25 - 10 , 90, 25);
		this.exitBtn.setBounds(width - 90 - 10, height - 25 - 10, 90, 25);
	}

	public String getJMSBrokerURL() {
		return this.brokerURLTF.getText();
	}

	public String getMonitorID() {
		return this.monitorID.getText();
	}


	public String getPropertiesFilename() {
		return this.propertiesTF.getText();
	}

	public void addConnectBtnActionListener(ActionListener listener) {
		if (listener == null)
			return;
		this.connectBtn.addActionListener(listener);
	}

	public void enableComponent() {
		this.brokerURLTF.setTextFieldEditable(true);
		this.monitorID.setTextFieldEditable(true);
		this.propertiesTF.setTextFieldEditable(true);
		this.connectBtn.setEnabled(true);
	}

	public void disableComponent() {
		this.brokerURLTF.setTextFieldEditable(false);
		this.monitorID.setTextFieldEditable(false);
		this.propertiesTF.setTextFieldEditable(false);
		this.connectBtn.setEnabled(false);
	}
	

}
