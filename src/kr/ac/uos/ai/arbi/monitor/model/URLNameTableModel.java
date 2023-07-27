package kr.ac.uos.ai.arbi.monitor.model;

import java.awt.Color;


public class URLNameTableModel extends AbstractTableModel<String, URLNameBean>{
	
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		if(columnIndex <= 1)
			return String.class;
		
		return Color.class;
	}

	@Override
	public String getColumnName(int columnIndex) {
		// TODO Auto-generated method stub
		if(columnIndex < 0)
			return "";
		
		switch(columnIndex) {
		case 0:
			return "URL";
		case 1:
			return "Name";
		case 2:
			return "FontColor";
		case 3:
			return "BgColor";
		}
		return "";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return (columnIndex == 1);
	}

}
