package kr.ac.uos.ai.arbi.monitor.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import kr.ac.uos.ai.arbi.monitor.control.adapter.ArbiMonitorAdapter;
import kr.ac.uos.ai.arbi.monitor.control.adapter.ArbiMonitorApolloAdapter;
import kr.ac.uos.ai.arbi.monitor.gui.main.sequence.ModelUpdater;
import kr.ac.uos.ai.arbi.monitor.model.FilterModel;

public class FilterTypeActionListener implements ActionListener{

	private String monitorID;
	private String filterType;
	private ArbiMonitorAdapter controller;
	private FilterModel filterModel;
	
	public FilterTypeActionListener(String monitorID, String filterType) {
		this.monitorID = monitorID;
		this.filterType = filterType;
	}
	
	public void setJMSController(ArbiMonitorAdapter controller) {
		this.controller = controller;
	}
	
	public void setFilterModel(FilterModel filterModel) {
		this.filterModel = filterModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if (!(e.getSource() instanceof JCheckBox))
			return;
		JCheckBox checkBox = (JCheckBox) e.getSource();
		String t = checkBox.getActionCommand();
		boolean v = checkBox.isSelected();
		SwingUtilities.invokeLater(new ModelUpdater(this.filterModel, t, v));
		
		JSONObject command = new JSONObject();
		
		JSONArray filterCommand = new JSONArray();
		
		JSONObject filterObject = new JSONObject();
		filterObject.put("Type", filterType);
		filterObject.put("LogType", "MessageLog");
		filterObject.put("Action", t);
		filterObject.put("Flag", String.valueOf(v));
		
		filterCommand.add(filterObject);
		
		System.out.println("filterObject = " +filterObject.toJSONString());
		
		command.put("Action", "Change Filter");
		command.put("ID", this.monitorID);
		command.put("Filter", filterCommand);
		
		System.out.println("command = " +command.toJSONString());
		controller.send(command.toJSONString());
	}

}
