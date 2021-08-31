/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package kr.ac.uos.ai.arbi.monitor.gui.main.sequence;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import kr.ac.uos.ai.arbi.monitor.control.FilterTypeActionListener;
import kr.ac.uos.ai.arbi.monitor.model.FilterModel;

class FilterTypePanel extends JPanel {
	private static final long serialVersionUID = 5638986157796622116L;
	private FilterModel filterModel;
	private Map<String, JCheckBox> checkBoxMap;
	private GridLayout gridLayout;

	FilterTypePanel(FilterModel filterModel) {
		this.filterModel = filterModel;
		this.checkBoxMap = new ConcurrentHashMap();
		this.gridLayout = new GridLayout();
	}

	public void initialize(int rows, int cols) {
		this.gridLayout.setRows(rows);
		this.gridLayout.setColumns(cols);
		super.setLayout(this.gridLayout);

		String[] types = this.filterModel.getTypes();
		for (String t : types) {
			JCheckBox checkBox = new JCheckBox(t.toString(), true);
			checkBox.setActionCommand(t.toString());
			//checkBox.addActionListener(this);

			this.checkBoxMap.put(t, checkBox);
			add(checkBox);
		}
	}
	
	public void setActionListener(FilterTypeActionListener listener) {
		for(String t : this.checkBoxMap.keySet()) {
			this.checkBoxMap.get(t).addActionListener(listener);
		}
	}

//	public void actionPerformed(ActionEvent e) {
//		if (!(e.getSource() instanceof JCheckBox))
//			return;
//		JCheckBox checkBox = (JCheckBox) e.getSource();
//		String t = checkBox.getActionCommand();
//		boolean v = checkBox.isSelected();
//		SwingUtilities.invokeLater(new ModelUpdater(this.filterModel, t, v));
//	}

	public void setLayout(LayoutManager mgr) {
	}
}