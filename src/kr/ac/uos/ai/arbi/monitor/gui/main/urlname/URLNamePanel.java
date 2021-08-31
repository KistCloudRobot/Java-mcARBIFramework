package kr.ac.uos.ai.arbi.monitor.gui.main.urlname;

import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.model.URLNameTableModel;

public class URLNamePanel  extends AbstractNullLayoutPanel {
	private static final long serialVersionUID = 6214945874540155356L;
	private URLNameTablePanel aaURLPanel;
	private URLNameTablePanel ltmURLPanel;

	public URLNamePanel(URLNameTableModel aaURLTableModel, URLNameTableModel ltmURLTableModel) {
		this.aaURLPanel = new URLNameTablePanel(aaURLTableModel, "AA URL List");

		if (ltmURLTableModel != null)
			this.ltmURLPanel = new URLNameTablePanel(ltmURLTableModel, "LTM URL List");
	}

	public void initialize() {
		this.aaURLPanel.initialize();
		this.aaURLPanel.setBounds(10, 10, 200, 200);
		add(this.aaURLPanel);

		if (this.ltmURLPanel != null) {
			this.ltmURLPanel.initialize();
			this.ltmURLPanel.setBounds(10, 220, 200, 200);
			add(this.ltmURLPanel);
		}
	}

	public void setSizeComponent(int width, int height) {
		int pHeight = (height - 30) / 2;

		this.aaURLPanel.setSize(width - 20, pHeight);
		if (this.ltmURLPanel != null)
			this.ltmURLPanel.setBounds(10, pHeight + 20, width - 20, pHeight);
	}
}