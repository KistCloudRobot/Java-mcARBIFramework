package kr.ac.uos.ai.arbi.monitor.gui.main.linecolor;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JColorChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;

import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.gui.ColorCellRenderer;
import kr.ac.uos.ai.arbi.monitor.model.SequenceColorTableModel;


public class LineColorTablePanel extends AbstractNullLayoutPanel{

	private static final long serialVersionUID = -220719161751102256L;
	private String title;
	private SequenceColorTableModel tableModel;
	private JTable table;
	private JScrollPane scrollPane;

	LineColorTablePanel(SequenceColorTableModel tableModel, String title) {
		this.title = title;
		this.tableModel = tableModel;
		this.table = new JTable(this.tableModel);
		this.scrollPane = new JScrollPane(this.table);
	}

	public void initialize() {
		super.setLayout(null);

		TitledBorder border = new TitledBorder(new LineBorder(Color.LIGHT_GRAY, 1), this.title);
		setBorder(border);

		initializeTable();
	}

	private void initializeTable() {
		this.scrollPane.setVerticalScrollBarPolicy(22);
		add(this.scrollPane);

		TableColumnModel tcm = this.table.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(170);
		tcm.getColumn(1).setPreferredWidth(60);
		tcm.getColumn(2).setPreferredWidth(60);

		this.table.setDefaultRenderer(this.table.getColumnClass(1), new ColorCellRenderer(5));
		this.table.setDefaultRenderer(this.table.getColumnClass(2), new ColorCellRenderer(5));

		this.table.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() != 2)
					return;
				Point point = new Point(e.getX(), e.getY());
				LineColorTablePanel.this.table.columnAtPoint(point);
				int row = LineColorTablePanel.this.table.rowAtPoint(point);
				int col = LineColorTablePanel.this.table.columnAtPoint(point);

				if ((col != 1) && (col != 2))
					return;

				Color oldColor = (Color) LineColorTablePanel.this.tableModel.getValueAt(row, col);
				Color newColor = null;
				if (col == 1)
					newColor = JColorChooser.showDialog(LineColorTablePanel.this, "Line Color", oldColor);
				else if (col == 2) {
					newColor = JColorChooser.showDialog(LineColorTablePanel.this, "Font Color", oldColor);
				}

				if (newColor == null)
					return;
				LineColorTablePanel.this.tableModel.setValueAt(newColor, row, col);
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

	protected void setSizeComponent(int width, int height) {
		this.scrollPane.setBounds(10, 20, width - 20, height - 30);
	}
}
