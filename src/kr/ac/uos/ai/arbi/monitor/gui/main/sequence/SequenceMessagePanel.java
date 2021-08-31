/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package kr.ac.uos.ai.arbi.monitor.gui.main.sequence;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.monitor.control.FilterTypeActionListener;
import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.model.AbstractListModel;
import kr.ac.uos.ai.arbi.monitor.model.FilterModel;
import kr.ac.uos.ai.arbi.monitor.model.Message;
import kr.ac.uos.ai.arbi.monitor.model.SequenceColorTableModel;
import kr.ac.uos.ai.arbi.monitor.model.URLNameTableModel;

public class SequenceMessagePanel extends AbstractNullLayoutPanel implements TableModelListener, ListDataListener {
	private static final long serialVersionUID = -4076643222317106041L;
	private static final int TOP_HEIGHT = 31;
	private static final int FILTER_PANEL_HEIGHT = 130;
	private static final int DEFAULT_CHECKBOX_WIDTH = 50;
	private URLNameTableModel nameModel;
	private AbstractListModel<?> sequenceModel;
	private JScrollPane nameScrollPane;
	private JScrollPane sequenceScrollPane;
	private JPanel filterPanel;
	private FilterTypePanel filterTypePanel;
	//private FilterListPanel filterListPanel;
	private int checkBoxWidth;
	private NameComponent nameComponent;
	private SequenceComponent sequenceComponent;

	public SequenceMessagePanel(URLNameTableModel nameModel, AbstractListModel<? extends Message> sequenceModel,
			FilterModel filterModel, AbstractListModel<GeneralizedList> filterListModel,
			SequenceColorTableModel lineColorModel) {
		this.nameModel = nameModel;
		this.sequenceModel = sequenceModel;

		this.nameComponent = new NameComponent(nameModel);
		this.sequenceComponent = new SequenceComponent(nameModel, sequenceModel, lineColorModel);

		this.nameScrollPane = new JScrollPane(this.nameComponent, 21, 31);
		this.sequenceScrollPane = new JScrollPane(this.sequenceComponent, 22, 32);

		this.filterPanel = new JPanel();
		this.filterTypePanel = new FilterTypePanel(filterModel);
		//this.filterListPanel = new FilterListPanel(filterListModel);

		this.checkBoxWidth = 50;
	}

	public void initialize() {
		setBackground(Color.WHITE);

		initializeNameComponent();
		initializeSequencePanel();
		initializeFilterPanel();
		initializeScrollBar();
		initializeListener();
		resizeComponent();
	}

	private void initializeNameComponent() {
		this.nameComponent.setBoxSize(80, 30);
		this.nameComponent.setHorizonGap(40);
		this.nameComponent.initialize();
		this.nameScrollPane.setBorder(null);
		add(this.nameScrollPane);
	}

	private void initializeSequencePanel() {
		this.sequenceComponent.initialize(80, 40, 80);
		this.sequenceScrollPane.setBorder(null);
		add(this.sequenceScrollPane);
	}

	private void initializeFilterPanel() {
		this.filterPanel.setLayout(null);
		this.filterPanel.setBorder(new TitledBorder("Filter"));

		this.filterTypePanel.initialize(4, 3);
		//this.filterListPanel.initialize();

		this.filterPanel.add(this.filterTypePanel);
		//this.filterPanel.add(this.filterListPanel);

		add(this.filterPanel);
	}

	private void initializeScrollBar() {
		JScrollBar scrollBar = this.sequenceScrollPane.getHorizontalScrollBar();
		scrollBar.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				JScrollBar sScrollBar = SequenceMessagePanel.this.sequenceScrollPane.getHorizontalScrollBar();
				JScrollBar nScrollBar = SequenceMessagePanel.this.nameScrollPane.getHorizontalScrollBar();

				nScrollBar.setValue(sScrollBar.getValue());
				nScrollBar.setValue(sScrollBar.getValue());
			}

			public void mouseMoved(MouseEvent e) {
			}
		});
	}

	private void initializeListener() {
		this.nameModel.addTableModelListener(this);
		this.sequenceModel.addListDataListener(this);
	}

	private void resizeComponent() {
		int nSize = this.nameModel.getRowCount();
		int sSize = this.sequenceModel.size();

		int tw = (nSize + 1) * 120;
		int tWidth1 = Math.max(this.nameComponent.getWidth(), tw + 7);
		int tWidth2 = Math.max(this.sequenceComponent.getWidth(), tw);
		int nHeight = 31;
		int sHeight = (sSize + 1) * 80;

		this.nameComponent.setPreferredSize(new Dimension(tWidth1, nHeight));
		this.nameComponent.setSize(tWidth1, nHeight);
		this.sequenceComponent.setPreferredSize(new Dimension(tWidth2, sHeight));
		this.sequenceComponent.setSize(tWidth2, sHeight);

		JScrollBar scrollBar = this.sequenceScrollPane.getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getMaximum() + 80);
		scrollBar.setUnitIncrement(scrollBar.getMaximum() / 5);
	}

	protected void setSizeComponent(int width, int height) {
		int lWidth = width - this.checkBoxWidth - 25;
		int lHeight = 95;

		this.filterPanel.setBounds(0, 0, width, 130);
		this.filterTypePanel.setBounds(5, 20, this.checkBoxWidth, 100);
		//this.filterListPanel.setBounds(this.checkBoxWidth + 15, 20, lWidth, lHeight);

		int sW = getParent().getWidth() - 10;
		int sH = height - 166;
		this.nameScrollPane.setBounds(5, 135, width - 20, 31);
		this.sequenceScrollPane.setBounds(5, 166, sW, sH);
	}

	public void setCheckBoxWidth(int width) {
		this.checkBoxWidth = width;
		this.filterTypePanel.setSize(this.checkBoxWidth, 100);
		//this.filterListPanel.setLocation(this.checkBoxWidth + 15, 20);
	}

	public void tableChanged(TableModelEvent e) {
		resizeComponent();
		repaintComponent();
	}

	public void contentsChanged(ListDataEvent e) {
	}

	public void intervalAdded(ListDataEvent e) {
		resizeComponent();
		repaintComponent();
	}

	public void intervalRemoved(ListDataEvent e) {
		resizeComponent();
		repaintComponent();
	}

	private void repaintComponent() {
		this.nameComponent.repaint();
		this.sequenceComponent.repaint();
	}

	public void setFilterTypeActionListener(FilterTypeActionListener listener) {
		this.filterTypePanel.setActionListener(listener);
		
	}
}