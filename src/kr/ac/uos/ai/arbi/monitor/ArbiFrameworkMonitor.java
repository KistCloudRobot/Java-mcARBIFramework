package kr.ac.uos.ai.arbi.monitor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.monitor.control.FilterTypeActionListener;
import kr.ac.uos.ai.arbi.monitor.control.adapter.AdapterType;
import kr.ac.uos.ai.arbi.monitor.control.adapter.ArbiMonitorAdapter;
import kr.ac.uos.ai.arbi.monitor.control.adapter.ArbiMonitorApolloAdapter;
import kr.ac.uos.ai.arbi.monitor.control.adapter.ArbiMonitorZeroMQAdapter;
import kr.ac.uos.ai.arbi.monitor.control.dispatch.MDFactory;
import kr.ac.uos.ai.arbi.monitor.control.dispatch.MessageDispatcher;
import kr.ac.uos.ai.arbi.monitor.gui.init.InitFrame;
import kr.ac.uos.ai.arbi.monitor.gui.init.InitFramePanel;
import kr.ac.uos.ai.arbi.monitor.gui.main.MainFrame;
import kr.ac.uos.ai.arbi.monitor.model.AbstractListModel;
import kr.ac.uos.ai.arbi.monitor.model.ArbiAgentMessage;
import kr.ac.uos.ai.arbi.monitor.model.ArbiAgentMessage.AgentMessageAction;
import kr.ac.uos.ai.arbi.monitor.model.LTMMessage;
import kr.ac.uos.ai.arbi.monitor.model.LTMMessage.LTMMessageAction;
import kr.ac.uos.ai.arbi.monitor.model.FilterModel;
import kr.ac.uos.ai.arbi.monitor.model.LinearListModel;
import kr.ac.uos.ai.arbi.monitor.model.MonitorModelContent;
import kr.ac.uos.ai.arbi.monitor.model.SequenceColorBean;
import kr.ac.uos.ai.arbi.monitor.model.SequenceColorTableModel;
import kr.ac.uos.ai.arbi.monitor.model.SequenceModel;
import kr.ac.uos.ai.arbi.monitor.model.URLNameBean;
import kr.ac.uos.ai.arbi.monitor.model.URLNameTableModel;

public class ArbiFrameworkMonitor {

	private InitFrame initFrame;
	private InitFramePanel initPanel;
	
	private FilterModel aaFilterModel;
	private AbstractListModel<GeneralizedList> aaFilterListModel;
	private URLNameTableModel aaURLTableModel;
	private AbstractListModel<ArbiAgentMessage> aaSequenceModel;
	private SequenceColorTableModel aaLineColorModel;
	
	private FilterModel ltmFilterModel;
	private AbstractListModel<GeneralizedList> ltmFilterListModel;
	private URLNameTableModel ltmURLTableModel;
	private AbstractListModel<LTMMessage> ltmSequenceModel;
	private SequenceColorTableModel ltmLineColorModel;
	
	private ArbiMonitorAdapter controller;
	private FilterTypeActionListener aaFilterListener;
	private FilterTypeActionListener ltmFilterListener;
	private MessageDispatcher dispatcher;
	
	public ArbiFrameworkMonitor() {
		this.initPanel = new InitFramePanel();
		this.initFrame = new InitFrame(this.initPanel);
		
		this.aaFilterModel = new FilterModel();
		this.aaFilterListModel = new LinearListModel();
		this.aaURLTableModel = new URLNameTableModel();
		this.aaSequenceModel = new SequenceModel();
		this.aaLineColorModel = new SequenceColorTableModel();
		
		this.ltmFilterModel = new FilterModel();
		this.ltmFilterListModel = new LinearListModel();
		this.ltmURLTableModel = new URLNameTableModel();
		this.ltmSequenceModel = new SequenceModel();
		this.ltmLineColorModel = new SequenceColorTableModel();
				
		this.initPanel.addConnectBtnActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ArbiFrameworkMonitor.this.initPanel.disableComponent();
				
				new SwingWorker() {
					protected Boolean doInBackground() throws Exception {
						if(ArbiFrameworkMonitor.this.initMainFrame() != false)
							ArbiFrameworkMonitor.this.initFrame.setVisible(false);
						ArbiFrameworkMonitor.this.initPanel.enableComponent();
						return Boolean.valueOf(true);
					}
				}.execute();
			}
		});
	}
	
	public void start() {
		this.initFrame.init();
		this.initFrame.setVisible(true);
	}
	
	public void start(String brokerURL, String properties) {
		this.initPanel.setBrokerAddress(brokerURL);
		this.initPanel.setPropertiesFilename(properties);
		this.initFrame.init();
		this.initFrame.setVisible(true);
	}
	
	
	private boolean initMainFrame() {
		String brokerURL = checkJMSBrokerURI();
		String monitorID = this.initPanel.getMonitorID();
		String interactionManagerURL = "http://www.arbi.com/interactionManager";
		
		if(brokerURL == null)
			return showWarning("JMS Broker URI is NULL");
		if(Utility.isNullString(monitorID))
			return showWarning("Monitor ID is NULL");
		if(Utility.isNullString(interactionManagerURL))
			interactionManagerURL = "http://www.arbi.com/interactionManager";
		
		initModel();
		
		if(!(initJMSController(brokerURL, monitorID, interactionManagerURL,AdapterType.ZeroMQ)))
			return showWarning("");
		
		initFilterTypeActionListener(monitorID);
		
		new MainFrame(brokerURL, this.aaFilterModel, this.aaFilterListModel,
				this.aaURLTableModel, this.aaSequenceModel, this.aaLineColorModel, this.ltmFilterModel,
				this.ltmFilterListModel, this.ltmURLTableModel, this.ltmSequenceModel, this.ltmLineColorModel, this.aaFilterListener, this.ltmFilterListener).setVisible(true);
		
		return true;
			
	}
	
	private String checkJMSBrokerURI() {
		String brokerURL = this.initPanel.getJMSBrokerURL();
		URI uri = URI.create(brokerURL);
		if(uri == null)
			return null;
		if(Utility.isNullString(uri.getScheme()))
			return null;
		if(Utility.isNullString(uri.getHost()))
			return null;
		if(uri.getPort() <= 0)
			return null;
		
		StringBuilder sb = new StringBuilder();
		sb.append(uri.getScheme()).append("://");
		sb.append(uri.getHost()).append(":");
		sb.append(uri.getPort());
		return sb.toString();
	}
	
	private void initModel() {

		String ltmURL = Utility.LTM_URL;
		
		String propertiesFilename = this.initPanel.getPropertiesFilename();
		MonitorModelContent content = MonitorModelContent.newInstance(new File(propertiesFilename));

		this.ltmURLTableModel.addRow(new URLNameBean(ltmURL, "LTM", Color.decode("#FFFFFF"), Color.decode("#720000")));
		
		this.aaFilterModel.initialize(toStringArray(AgentMessageAction.values()));
		this.ltmFilterModel.initialize(toStringArray(LTMMessageAction.values()));
		
		for(GeneralizedList gl : content.getAAFilters()) {
			this.aaFilterListModel.add(gl);
		}
		
		for(GeneralizedList gl : content.getLTMFilters()) {
			this.ltmFilterListModel.add(gl);
		}
		
		for(URLNameBean entry : content.getAAURLNameMappings()) {
			this.aaURLTableModel.addRow(entry);
		}
		
		for(URLNameBean entry : content.getLTMURLNameMappings()) {
			this.ltmURLTableModel.addRow(entry);
		}
		
		
		this.aaLineColorModel.addRow(new SequenceColorBean(AgentMessageAction.Inform, Color.RED, Color.BLACK));
		this.aaLineColorModel.addRow(new SequenceColorBean(AgentMessageAction.Notify, Color.BLUE, Color.BLACK));
		this.aaLineColorModel.addRow(new SequenceColorBean(AgentMessageAction.Query, Color.GREEN, Color.BLACK));
		this.aaLineColorModel.addRow(new SequenceColorBean(AgentMessageAction.Request, Color.GRAY, Color.BLACK));
		this.aaLineColorModel.addRow(new SequenceColorBean(AgentMessageAction.Response, Color.ORANGE, Color.BLACK));
		this.aaLineColorModel.addRow(new SequenceColorBean(AgentMessageAction.Subscribe, Color.DARK_GRAY, Color.BLACK));
		this.aaLineColorModel.addRow(new SequenceColorBean(AgentMessageAction.Unsubscribe, Color.MAGENTA, Color.BLACK));
		this.aaLineColorModel.addRow(new SequenceColorBean(AgentMessageAction.System, Color.LIGHT_GRAY, Color.BLACK));
		
		this.ltmLineColorModel.addRow(new SequenceColorBean(LTMMessageAction.AssertFact, Color.RED, Color.BLACK));
		this.ltmLineColorModel.addRow(new SequenceColorBean(LTMMessageAction.Match, Color.BLUE, Color.BLACK));
		this.ltmLineColorModel.addRow(new SequenceColorBean(LTMMessageAction.Notify, Color.GREEN, Color.BLACK));
//		this.ltmLineColorModel.addRow(new SequenceColorBean(LTMMessageAction.Push, Color.GRAY, Color.BLACK));
//		this.ltmLineColorModel.addRow(new SequenceColorBean(LTMMessageAction.ReleaseStream, Color.ORANGE, Color.BLACK));
//		this.ltmLineColorModel.addRow(new SequenceColorBean(LTMMessageAction.RequestStream, Color.DARK_GRAY, Color.BLACK));
		this.ltmLineColorModel.addRow(new SequenceColorBean(LTMMessageAction.Result, Color.MAGENTA, Color.BLACK));
		this.ltmLineColorModel.addRow(new SequenceColorBean(LTMMessageAction.RetractFact, Color.PINK, Color.BLACK));
		this.ltmLineColorModel.addRow(new SequenceColorBean(LTMMessageAction.RetrieveFact, Color.RED, Color.BLACK));
		this.ltmLineColorModel.addRow(new SequenceColorBean(LTMMessageAction.Subscribe, RMC.COLOR_VIOLET, Color.BLACK));
		this.ltmLineColorModel.addRow(new SequenceColorBean(LTMMessageAction.Unsubscribe, RMC.COLOR_D_GREEN, Color.BLACK));
		this.ltmLineColorModel.addRow(new SequenceColorBean(LTMMessageAction.UpdateFact, Color.CYAN, Color.BLACK));
	}
	
	private String[] toStringArray(Object[] objs) {
		List list = new LinkedList();
		for (Object obj : objs) {
			list.add(obj.toString());
		}
		return ((String[]) list.toArray(Utility.STR_NULL_ARRAY));
	}
	
	private boolean initJMSController(String brokerURL, String monitorID, String interactionManagerURL,AdapterType controlType) {
		
		if(controlType.equals(AdapterType.ZeroMQ)) {
			controller = new ArbiMonitorZeroMQAdapter(this);
		}else if(controlType.equals(AdapterType.Apollo)) {
			controller = new ArbiMonitorApolloAdapter(this);
		}
		
		if(!(controller.connect(brokerURL, monitorID, interactionManagerURL)))
			return false;
		
		controller.send(initMonitorCommand(monitorID,controlType));
		
		dispatcher = MDFactory.getMessageProcessor();

		//dispatcher.setAAMessageFilterModel(this.aaFilterModel);
		//dispatcher.setAAMessageFilterListModel(this.aaFilterListModel);
		dispatcher.setAAURLTableModel(this.aaURLTableModel);
		dispatcher.setAASequenceModel(this.aaSequenceModel);

		//dispatcher.setCDCMessageFilterModel(this.cdcFilterModel);
		//dispatcher.setCDCMessageFilterListModel(this.cdcFilterListModel);
		dispatcher.setLTMURLTableModel(this.ltmURLTableModel);
		dispatcher.setLTMSequenceModel(this.ltmSequenceModel);
		
		return true;
		
	}
	
	public void onLogMessage(String msg) {
		dispatcher.dispatch(msg);
	}
	
	private void initFilterTypeActionListener(String monitorID) {

		aaFilterListener = new FilterTypeActionListener(monitorID, "AgentMessage");
		ltmFilterListener = new FilterTypeActionListener(monitorID, "LTMMessage");
		
		aaFilterListener.setJMSController(controller);
		ltmFilterListener.setJMSController(controller);
		
		aaFilterListener.setFilterModel(aaFilterModel);
		ltmFilterListener.setFilterModel(ltmFilterModel);
		
		
	}
	
	private String initMonitorCommand(String monitorID,AdapterType aType) {
		
		JSONObject command = new JSONObject();
		command.put("ID", monitorID);
		command.put("Action", "Create Monitor");
		if(aType == AdapterType.Apollo)
			command.put("Protocol", "ActiveMQ");
		else if(aType == AdapterType.ZeroMQ){
			command.put("Protocol", "ZeroMQ");
		}else {
			command.put("Protocol", "ZeroMQ");
		}
		
		
		JSONArray filterArray = new JSONArray();
		
		String[] aaFilterType = this.aaFilterModel.getTypes();
		for(int i=0; i<aaFilterType.length; i++) {
			JSONObject filterCommand = new JSONObject();
			filterCommand.put("LogType", "MessageLog");
			filterCommand.put("Type", "AgentMessage");
			filterCommand.put("Action", aaFilterType[i]);
			filterCommand.put("Flag", "true");
			filterArray.add(filterCommand);
		}
		
		String[] ltmFilterType = this.ltmFilterModel.getTypes();
		for(int i=0; i<ltmFilterType.length; i++) {
			JSONObject filterCommand = new JSONObject();
			filterCommand.put("LogType", "MessageLog");
			filterCommand.put("Type", "LTMMessage");
			filterCommand.put("Action", ltmFilterType[i]);
			filterCommand.put("Flag", "true");
			filterArray.add(filterCommand);
		}
		
		command.put("Filter", filterArray);
		//System.out.println(command.toJSONString());
		return command.toJSONString();
	}
	
	private final boolean showWarning(String text) {
		JOptionPane.showMessageDialog(this.initPanel, text, "Warning", 2);
		return false;
	}
	
}
