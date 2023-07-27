package kr.ac.uos.ai.arbi.monitor.gui.main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.monitor.RMC;
import kr.ac.uos.ai.arbi.monitor.control.FilterTypeActionListener;
import kr.ac.uos.ai.arbi.monitor.gui.GUI;
import kr.ac.uos.ai.arbi.monitor.gui.main.linecolor.LineColorPanel;
import kr.ac.uos.ai.arbi.monitor.gui.main.sequence.SequenceMessagePanel;
import kr.ac.uos.ai.arbi.monitor.gui.main.urlname.URLNamePanel;
import kr.ac.uos.ai.arbi.monitor.gui.messagetest.MessageTestFrame;
import kr.ac.uos.ai.arbi.monitor.model.AbstractListModel;
import kr.ac.uos.ai.arbi.monitor.model.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.monitor.model.FilterModel;
import kr.ac.uos.ai.arbi.monitor.model.LTMMessage;
import kr.ac.uos.ai.arbi.monitor.model.SequenceColorTableModel;
import kr.ac.uos.ai.arbi.monitor.model.URLNameTableModel;



public class MainFrame extends JFrame{

	private static final long serialVersionUID = 2154387573296272301L;
	private JTabbedPane tabbedPane;
	private ControlPanel controlPanel;
	
	private URLNamePanel urlPanel;
	private LineColorPanel lineColorPanel;
	private SequenceMessagePanel aaMessagePanel;
	private SequenceMessagePanel ltmMessagePanel;
	private String brokerURL;

	public MainFrame(String brokerURL, FilterModel aaFilterModel,
			AbstractListModel<GeneralizedList> aaFilterListModel, URLNameTableModel aaURLTableModel,
			AbstractListModel<ArbiAgentMessage> aaSequenceModel, SequenceColorTableModel aaLineColorModel,
			FilterModel ltmFilterModel, AbstractListModel<GeneralizedList> ltmFilterListModel,
			URLNameTableModel ltmURLTableModel, AbstractListModel<LTMMessage> ltmSequenceModel,
			SequenceColorTableModel ltmLineColorModel, FilterTypeActionListener aaFilterListener, FilterTypeActionListener ltmFilterListener) {
		
		this.brokerURL = brokerURL;

		this.tabbedPane = new JTabbedPane();
		this.controlPanel = new ControlPanel();

		this.aaMessagePanel = new SequenceMessagePanel(aaURLTableModel, aaSequenceModel, aaFilterModel,
				aaFilterListModel, aaLineColorModel);
		this.ltmMessagePanel = new SequenceMessagePanel(ltmURLTableModel, ltmSequenceModel, ltmFilterModel,
				ltmFilterListModel, ltmLineColorModel);

		this.urlPanel = new URLNamePanel(aaURLTableModel, ltmURLTableModel);
		this.lineColorPanel = new LineColorPanel(aaLineColorModel, ltmLineColorModel);

		initialize();
		
		this.aaMessagePanel.setFilterTypeActionListener(aaFilterListener);
		this.ltmMessagePanel.setFilterTypeActionListener(ltmFilterListener);
	}
	
	private void initialize() {
		this.aaMessagePanel.initialize();
		this.aaMessagePanel.setCheckBoxWidth(300);
		this.ltmMessagePanel.initialize();
		this.ltmMessagePanel.setCheckBoxWidth(450);
		this.urlPanel.initialize();
		this.lineColorPanel.initialize();
		this.controlPanel.initialize();

		initializeFrame();
		initializeMenuBar();
		initializeTabbedPane();
		initializeControlPanel();
	}
	
	private void initializeFrame() {
		Dimension screenSize = GUI.getScreenSize();
		int fX = (screenSize.width - 750) / 2;
		if (fX < 0)
			fX = 0;
		int fY = (screenSize.height - 650) / 2;
		if (fY < 0)
			fY = 0;

		setTitle(RMC.getTitle());
		setDefaultCloseOperation(3);
		setBounds(fX, fY, 750, 650);
		getContentPane().add(this.tabbedPane);
		getContentPane().add(this.controlPanel, "South");
	}

	private void initializeMenuBar() {
		MenuActionListener listener = new MenuActionListener(this);

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(70);
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic(88);
		exitItem.addActionListener(listener);
		exitItem.setActionCommand(MenuActionListener.MenuType.EXIT.toString());
		fileMenu.add(exitItem);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(72);
		JMenuItem aboutItem = new JMenuItem("About ArbiFrameWorkMonitor");
		aboutItem.setMnemonic(65);
		aboutItem.addActionListener(listener);
		aboutItem.setActionCommand(MenuActionListener.MenuType.ABOUT.toString());
		helpMenu.add(aboutItem);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
	}

	private void initializeTabbedPane() {
		this.tabbedPane.add("AA Message", this.aaMessagePanel);
		this.tabbedPane.add("LTM Message", this.ltmMessagePanel);
		//this.tabbedPane.add("Working Memory", this.wmPanel);
		this.tabbedPane.add("URL-Name List", this.urlPanel);
		this.tabbedPane.add("Sequence Color", this.lineColorPanel);
	}

	private void initializeControlPanel() {
		this.controlPanel.addNewMessageTesterButtonActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MessageTestFrame f = new MessageTestFrame(MainFrame.this.brokerURL, MainFrame.this);
				f.setVisible(true);
			}
		});
	}
}
