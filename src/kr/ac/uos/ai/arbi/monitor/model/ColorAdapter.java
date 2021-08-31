package kr.ac.uos.ai.arbi.monitor.model;

import java.awt.Color;

import javax.xml.bind.annotation.adapters.XmlAdapter;


class ColorAdapter  extends XmlAdapter<String, Color> {
	public String marshal(Color color) throws Exception {
		if (color == null)
			return "";

		StringBuilder sb = new StringBuilder();
		sb.append("#");
		sb.append(String.format("%02X", new Object[] { Integer.valueOf(color.getRed()) }));
		sb.append(String.format("%02X", new Object[] { Integer.valueOf(color.getGreen()) }));
		sb.append(String.format("%02X", new Object[] { Integer.valueOf(color.getBlue()) }));

		return sb.toString();
	}

	public Color unmarshal(String str) throws Exception {
		return Color.decode(str);
	}
}
