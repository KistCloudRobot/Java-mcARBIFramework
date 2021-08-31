package kr.ac.uos.ai.arbi.monitor.model;

import java.awt.Color;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import kr.ac.uos.ai.arbi.monitor.Utility;


@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "url-name-bean-type", propOrder = {"URL", "name", "fontColor", "bgColor"})
@XmlRootElement(name = "url-name-bean")

public class URLNameBean implements TableEntry<String> {

	public static final URLNameBean[] NULL_ARRAY = new URLNameBean[0];
	private static final Color DEFAULT_FONT_COLOR = Color.decode("#000000");
	private static final Color DEFAULT_BG_COLOR = Color.decode("#FFEB29");
	
	@XmlTransient
	private String url;

	@XmlTransient
	private String name;

	@XmlTransient
	private Color fontColor;

	@XmlTransient
	private Color bgColor;
	
	URLNameBean() {
		setFontColor(DEFAULT_FONT_COLOR);
		setBgColor(DEFAULT_BG_COLOR);
	}
	
	public URLNameBean(String url) {
		setURL(url);
		setName(null);
		setFontColor(DEFAULT_FONT_COLOR);
		setBgColor(DEFAULT_BG_COLOR);
	}
	
	public URLNameBean(String url, String name) {
		setURL(url);
		setName(name);
		setFontColor(DEFAULT_FONT_COLOR);
		setBgColor(DEFAULT_BG_COLOR);
	}
	
	public URLNameBean(String url, String name, Color fontColor, Color bgColor) {
		setURL(url);
		setName(name);
		setFontColor(fontColor);
		setBgColor(bgColor);
	}
	
	void setURL(String url) {
		this.url = url.toLowerCase();
	}

	public void setName(String name) {
		this.name = name;
		if (!(Utility.isNullString(this.name)))
			return;
		this.name = getNameFromURL();
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
		if (this.bgColor != null)
			return;
		this.bgColor = DEFAULT_BG_COLOR;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
		if (this.fontColor != null)
			return;
		this.fontColor = DEFAULT_FONT_COLOR;
	}

	private String getNameFromURL() {
		String[] t = this.url.split("/");
		return t[(t.length - 1)];
	}

	@XmlElement(name = "name", required = true)
	public String getName() {
		return this.name;
	}

	@XmlElement(name = "url", required = true)
	public String getURL() {
		return this.url;
	}

	@XmlElement(name = "font-color")
	@XmlJavaTypeAdapter(ColorAdapter.class)
	public Color getFontColor() {
		return this.fontColor;
	}

	@XmlElement(name = "bg-color")
	@XmlJavaTypeAdapter(ColorAdapter.class)
	public Color getBgColor() {
		return this.bgColor;
	}

	public String getKey() {
		return getURL();
	}

	public Object getValue(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return getURL();
		case 1:
			return getName();
		case 2:
			return getFontColor();
		case 3:
			return getBgColor();
		}
		return null;
	}

	public void setValue(int columnIndex, Object value) {
		switch (columnIndex) {
		case 0:
			setURL((String) value);
			break;
		case 1:
			setName((String) value);
			break;
		case 2:
			setFontColor((Color) value);
			break;
		case 3:
			setBgColor((Color) value);
		}
	}

}
