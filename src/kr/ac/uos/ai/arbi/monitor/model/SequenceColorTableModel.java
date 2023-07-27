package kr.ac.uos.ai.arbi.monitor.model;

import java.awt.Color;

public class SequenceColorTableModel extends AbstractTableModel<Object, SequenceColorBean>{

	public int getColumnCount() {
		return 3;
	}
	
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0)
			return String.class;
		if (columnIndex == 1)
			return Color.class;
		if (columnIndex == 2)
			return Color.class;
		return String.class;
	}

	public String getColumnName(int columnIndex) {
		if (columnIndex < 0)
			return "";
		switch (columnIndex) {
		case 0:
			return "Type";
		case 1:
			return "Line Color";
		case 2:
			return "Font Color";
		}
		return "";
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	
}
