package kr.ac.uos.ai.arbi.monitor.model;

import java.awt.Color;

public class SequenceColorBean implements TableEntry<Object>{
	private static final SequenceColorBean[] NULL_ARRAY = new SequenceColorBean[0];
	private static final Color DEFAULT_COLOR = Color.decode("#000000");
	private Object key;
	private Color lineColor;
	private Color fontColor;
	
	public SequenceColorBean(Object key) {
		setKey(key);
	}
	
	public SequenceColorBean(Object key, Color lineColor, Color fontColor) {
		setKey(key);
		setLineColor(lineColor);
		setFontColor(fontColor);
	}
	
	void setKey(Object key) {
		this.key = key;
	}
	
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
		if(this.lineColor != null)
			return;
		this.lineColor = DEFAULT_COLOR;
	}
	
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
		if(this.fontColor != null)
			return;
		this.fontColor = DEFAULT_COLOR;
	}
	
	public Object getKey() {
		return this.key;
	}

	public Color getLineColor() {
		return this.lineColor;
	}

	public Color getFontColor() {
		return this.fontColor;
	}

	public Object getValue(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return getKey();
		case 1:
			return getLineColor();
		case 2:
			return getFontColor();
		}
		return null;
	}

	public void setValue(int columnIndex, Object value) {
		switch (columnIndex) {
		case 0:
			setKey(value);
		case 1:
			setLineColor((Color) value);
		case 2:
			setFontColor((Color) value);
		}
	}

}
