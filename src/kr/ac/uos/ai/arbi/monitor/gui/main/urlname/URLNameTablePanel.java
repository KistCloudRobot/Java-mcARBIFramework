package kr.ac.uos.ai.arbi.monitor.gui.main.urlname;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;

import kr.ac.uos.ai.arbi.monitor.Utility;
import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.gui.ColorCellRenderer;
import kr.ac.uos.ai.arbi.monitor.gui.TitledTextField;
import kr.ac.uos.ai.arbi.monitor.model.URLNameBean;
import kr.ac.uos.ai.arbi.monitor.model.URLNameTableModel;



public class URLNameTablePanel  extends AbstractNullLayoutPanel {
	private static final long serialVersionUID = 6214945874540155356L;
	private static final Color DEFAULT_FONT_COLOR = Color.BLACK;
	private static final Color DEFAULT_BG_COLOR = Color.getHSBColor(60.0F, 27.0F, 100.0F);
	private static final int TEXT_FIELD_HEIGHT = 50;
	private static final int NAME_WIDTH = 120;
	private static final int COLOR_C_WIDTH = 40;
	private static final int BUTTON_WIDTH = 60;
	private static final int BUTTON_HEIGHT = 40;
	private String title;
	private URLNameTableModel tableModel;
	private JTable table;
	private JScrollPane scrollPane;
	private TitledTextField urlTextField;
	private TitledTextField nameTextField;
	private ColorChoosePanel fontColorPanel;
	private ColorChoosePanel bgColorPanel;
	private JButton addBtn;

	URLNameTablePanel(URLNameTableModel tableModel, String title) {
		this.title = title;
		this.tableModel = tableModel;
		this.table = new JTable(this.tableModel);
		this.scrollPane = new JScrollPane(this.table);

		this.urlTextField = new TitledTextField("URL");
		this.nameTextField = new TitledTextField("Name");
		this.fontColorPanel = new ColorChoosePanel("Font", DEFAULT_FONT_COLOR);
		this.bgColorPanel = new ColorChoosePanel("BG", DEFAULT_BG_COLOR);
		this.addBtn = new JButton("ADD");
	}

	public void initialize() {
		super.setLayout(null);

		TitledBorder border = new TitledBorder(new LineBorder(Color.LIGHT_GRAY, 1), this.title);
		setBorder(border);

		initializeTable();

		this.urlTextField.setBounds(10, 210, 150, 50);
		this.nameTextField.setBounds(170, 210, 30, 50);
		add(this.urlTextField);
		add(this.nameTextField);

		this.fontColorPanel.initialize();
		this.bgColorPanel.initialize();

		this.fontColorPanel.setBounds(210, 210, 40, 50);
		this.bgColorPanel.setBounds(260, 210, 40, 50);

		add(this.fontColorPanel);
		add(this.bgColorPanel);

		this.addBtn.setBounds(210, 210, 60, 40);
		add(this.addBtn);

		this.addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				URLNameTablePanel.this.addData();
			}
		});
	}

	private void initializeTable() {
		this.scrollPane.setBounds(10, 20, 200, 200);
		this.scrollPane.setVerticalScrollBarPolicy(22);
		add(this.scrollPane);

		TableColumnModel tcm = this.table.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(170);
		tcm.getColumn(1).setPreferredWidth(60);
		tcm.getColumn(2).setPreferredWidth(60);
		tcm.getColumn(3).setPreferredWidth(60);

		this.table.setDefaultRenderer(this.table.getColumnClass(2), new ColorCellRenderer(2));
		this.table.setDefaultRenderer(this.table.getColumnClass(3), new ColorCellRenderer(2));

		this.table.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() != 2)
					return;
				Point point = new Point(e.getX(), e.getY());
				URLNameTablePanel.this.table.columnAtPoint(point);
				int row = URLNameTablePanel.this.table.rowAtPoint(point);
				int col = URLNameTablePanel.this.table.columnAtPoint(point);

				if ((col != 2) && (col != 3))
					return;

				Color oldColor = (Color) URLNameTablePanel.this.tableModel.getValueAt(row, col);
				Color newColor = null;
				if (col == 2)
					newColor = JColorChooser.showDialog(URLNameTablePanel.this, "Font Color", oldColor);
				else if (col == 3) {
					newColor = JColorChooser.showDialog(URLNameTablePanel.this, "Background Color", oldColor);
				}

				if (newColor == null)
					return;
				URLNameTablePanel.this.tableModel.setValueAt(newColor, row, col);
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
			}
		});
		this.table.setSelectionMode(0);
	}

	private void addData() {
		String url = this.urlTextField.getText();
		String name = this.nameTextField.getText();
		Color fontColor = this.fontColorPanel.getColor();
		Color bgColor = this.bgColorPanel.getColor();
		if ((Utility.isNullString(url)) || (Utility.isNullString(name)))
			return;

		this.tableModel.addRow(new URLNameBean(url, name, fontColor, bgColor));

		this.urlTextField.setText("");
		this.nameTextField.setText("");
		this.fontColorPanel.reset();
		this.bgColorPanel.reset();
	}

	public void setSizeComponent(int width, int height) {
		int sHeight = height - 50 - 30;
		this.scrollPane.setSize(width - 20, sHeight);

		int y = height - 50 - 10;
		int uWidth = width - 60 - 120 - 60 - 80;
		this.urlTextField.setBounds(10, y, uWidth, 50);

		int nX = uWidth + 20;
		this.nameTextField.setBounds(nX, y, 120, 50);

		int fcX = nX + 120 + 10;
		this.fontColorPanel.setBounds(fcX, y, 40, 50);

		int bcX = fcX + 40 + 10;
		this.bgColorPanel.setBounds(bcX, y, 40, 50);

		int bX = bcX + 40 + 10;
		this.addBtn.setBounds(bX, y + 8, 60, 40);
	}
}