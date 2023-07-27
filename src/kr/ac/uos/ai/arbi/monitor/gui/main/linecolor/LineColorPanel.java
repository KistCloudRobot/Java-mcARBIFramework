package kr.ac.uos.ai.arbi.monitor.gui.main.linecolor;

import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.model.SequenceColorTableModel;

public class LineColorPanel extends AbstractNullLayoutPanel {
	private static final long serialVersionUID = 1269024691218362491L;
	private LineColorTablePanel aaAtionTablePanel;
	private LineColorTablePanel ltmActionTablePanel;

	public LineColorPanel(SequenceColorTableModel aaActionTM, SequenceColorTableModel ltmActionTM) {
		this.aaAtionTablePanel = new LineColorTablePanel(aaActionTM, "AA Action Line Color");
		this.ltmActionTablePanel = new LineColorTablePanel(ltmActionTM, "LTM Action Line Color");
	}

	public void initialize() {
		this.aaAtionTablePanel.initialize();
		this.ltmActionTablePanel.initialize();
		add(this.aaAtionTablePanel);
		add(this.ltmActionTablePanel);
	}

	public void setSizeComponent(int width, int height) {
		int pHeight = (height - 30) / 2;
		this.aaAtionTablePanel.setBounds(10, 10, width - 20, pHeight);
		this.ltmActionTablePanel.setBounds(10, pHeight + 20, width - 20, pHeight);
	}
}