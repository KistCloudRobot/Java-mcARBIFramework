package kr.ac.uos.ai.arbi.monitor.gui.main.wm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.monitor.Utility;
import kr.ac.uos.ai.arbi.monitor.gui.AbstractNullLayoutPanel;
import kr.ac.uos.ai.arbi.monitor.gui.TitledTextField;
import kr.ac.uos.ai.arbi.monitor.model.AbstractListModel;


public class WMPanel  extends AbstractNullLayoutPanel {
	private static final long serialVersionUID = -5156149748337671832L;
	private static final int DUMP_CYCLE = 1000;
	private static final int DEFAULT_MARGIN = 10;
	private static final int BUTTON_HEIGHT = 50;
	private AbstractListModel<GeneralizedList> listModel;
	private DataSource ds;
	private JScrollPane listPanel;
	private JList list;
	private TitledTextField oldGLField;
	private TitledTextField newGLField;
	private JButton assertBtn;
	private JButton updateBtn;
	private JButton retractBtn;
	private JPanel optionPanel;
	private JCheckBox cyclicDumpCB;
	private AtomicBoolean cyclicDump;
	private JButton dumpBtn;
	private ExecutorService threadPool;

	public WMPanel(AbstractListModel<GeneralizedList> listModel, DataSource ds) {
		this.listModel = listModel;
		this.ds = ds;

		this.list = new JList(this.listModel);
		this.listPanel = new JScrollPane(this.list, 22, 31);

		this.oldGLField = new TitledTextField("Old GL");
		this.newGLField = new TitledTextField("New GL");
		this.assertBtn = new JButton("ASSERT");
		this.updateBtn = new JButton("UPDATE");
		this.retractBtn = new JButton("RETRACT");

		this.optionPanel = new JPanel();
		this.cyclicDumpCB = new JCheckBox("Cyclic Dump", false);
		this.cyclicDump = new AtomicBoolean(false);
		this.dumpBtn = new JButton("DUMP");

		this.threadPool = Executors.newCachedThreadPool();
	}

	public void initialize() {
		initializeOptionPanel();
		initalizeListPanel();
		initializeInputPart();
		intializeDumpThread();
	}

	private void initializeOptionPanel() {
		this.optionPanel.setLayout(null);
		this.optionPanel.setBorder(new TitledBorder("Option"));
		add(this.optionPanel);

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (event.getSource() instanceof AbstractButton) {
					AbstractButton button = (AbstractButton) event.getSource();

					String command = button.getActionCommand();
					if ("DUMP".equals(command)) {
						WMPanel.this.threadPool.execute(WMPanel.this.createDumpRunnable());
					} else {
						if (!("CyclicDump".equals(command)))
							return;
						WMPanel.this.cyclicDump();
					}
				}
			}
		};
		this.optionPanel.add(this.cyclicDumpCB);

		this.cyclicDumpCB.setActionCommand("CyclicDump");
		this.cyclicDumpCB.addActionListener(listener);

		this.optionPanel.add(this.dumpBtn);

		this.dumpBtn.setActionCommand("DUMP");
		this.dumpBtn.addActionListener(listener);
	}

	private void initalizeListPanel() {
		add(this.listPanel);
		this.list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				WMPanel.this.changeSelectedGL();
			}
		});
	}

	private void changeSelectedGL() {
		Object obj = this.list.getSelectedValue();
		if (obj == null)
			return;
		GeneralizedList gl = (GeneralizedList) obj;
		this.oldGLField.setText(gl.toString());
	}

	private void initializeInputPart() {
		add(this.oldGLField);
		add(this.newGLField);
		add(this.assertBtn);
		add(this.updateBtn);
		add(this.retractBtn);

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (event.getSource() instanceof AbstractButton) {
					AbstractButton button = (AbstractButton) event.getSource();

					String command = button.getActionCommand();
					Runnable r = null;
					if ("ASSERT".equals(command))
						r = WMPanel.this.createAssertRunnable();
					else if ("UPDATE".equals(command))
						r = WMPanel.this.createUpdateRunnable();
					else if ("RETRACT".equals(command))
						r = WMPanel.this.createRetractRunnable();
					if (r == null)
						return;
					WMPanel.this.threadPool.execute(r);
				}
			}
		};
		this.assertBtn.setActionCommand("ASSERT");
		this.updateBtn.setActionCommand("UPDATE");
		this.retractBtn.setActionCommand("RETRACT");

		this.assertBtn.addActionListener(listener);
		this.updateBtn.addActionListener(listener);
		this.retractBtn.addActionListener(listener);
	}

	private void intializeDumpThread() {
		this.threadPool.execute(new Runnable() {
			public void run() {
				while (true) {
					if (WMPanel.this.cyclicDump.get())
						WMPanel.this.dump();
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException localInterruptedException) {
					}
				}
			}
		});
	}

	private void dump() {
		if (this.ds == null)
			return;

		GeneralizedList[] list = (GeneralizedList[]) null;
		try {
			//list = this.ds.getBlackboard().dumpWorkingMemory();
		
		} catch (Exception e) {
			return;
		}

		SwingUtilities.invokeLater(new WMDumper(this.listModel, list));
	}

	private void cyclicDump() {
		if (this.cyclicDumpCB.isSelected()) {
			this.cyclicDump.set(true);
			this.dumpBtn.setEnabled(false);
		} else {
			this.cyclicDump.set(false);
			this.dumpBtn.setEnabled(true);
		}
	}

	private void assertFact() {
		if (!(checkDS(true)))
			return;

		String text = this.newGLField.getText();
		if (text.isEmpty()) {
			showWarning("Assert�� GL�� �Է��� �ּ���");
			return;
		}

		GeneralizedList gl = parseGL(text, true);
		if (gl == null)
			return;
		try {
			//this.ks.getBlackboard().assertFact(gl);
		} catch (Exception e) {
			showWarning("GL�� Blackboard�� assert���� ���߽��ϴ�.");
			return;
		}
		this.newGLField.clear();
		dump();
	}

	private void updateFact() {
		if (!(checkDS(true)))
			return;

		String olgGLText = this.oldGLField.getText();
		String newGLText = this.newGLField.getText();
		if ((Utility.isNullString(olgGLText)) || (Utility.isNullString(newGLText))) {
			showWarning("GL�� �Է��� �ּ���.");
			return;
		}

		GeneralizedList oldGL = parseGL(olgGLText, true);
		GeneralizedList newGL = parseGL(newGLText, true);
		if ((oldGL == null) || (newGL == null))
			return;
		try {
			//this.ks.getBlackboard().updateFact(oldGL, newGL);
		} catch (Exception e) {
			showWarning("Blackboard�� update���� ���߽��ϴ�.");
			return;
		}
		this.oldGLField.clear();
		this.newGLField.clear();
		dump();
	}

	private void retractFact() {
		if (!(checkDS(true)))
			return;

		String text = this.oldGLField.getText();
		if (text.isEmpty()) {
			showWarning("Retract�� GL�� �Է��� �ּ���");
			return;
		}

		GeneralizedList gl = parseGL(text, true);
		if (gl == null)
			return;
		try {
			this.ds.retractFact(gl.toString());
		} catch (Exception e) {
			showWarning("GL�� Blackboard�� retract���� ���߽��ϴ�.");
			return;
		}

		this.oldGLField.clear();
		dump();
	}

	private boolean checkDS(boolean isShowWarning) {
		if (this.ds == null) {
			if (isShowWarning)
				showWarning("KnowledgeSource�� �����Ǿ� ���� �ʽ��ϴ�!!!");
			return false;
		}
		return true;
	}

	private GeneralizedList parseGL(String str, boolean isShowWarning) {
		try {
			GeneralizedList gl = GLFactory.newGLFromGLString(str);
			return gl;
		} catch (Exception e) {
			if (isShowWarning)
				showWarning("GL Syntax was Wrong");
		}
		return null;
	}

	private Runnable createAssertRunnable() {
		return new Runnable() {
			public void run() {
				WMPanel.this.assertFact();
			}
		};
	}

	private Runnable createUpdateRunnable() {
		return new Runnable() {
			public void run() {
				WMPanel.this.updateFact();
			}
		};
	}

	private Runnable createRetractRunnable() {
		return new Runnable() {
			public void run() {
				WMPanel.this.retractFact();
			}
		};
	}

	private Runnable createDumpRunnable() {
		return new Runnable() {
			public void run() {
				WMPanel.this.dump();
			}
		};
	}

	public void setSizeComponent(int width, int height) {
		this.optionPanel.setBounds(5, 5, width - 10, 50);
		this.dumpBtn.setBounds(width - 120, 20, 100, 20);
		this.cyclicDumpCB.setBounds(5, 20, 100, 20);

		int bWidth = (width - 30) / 3;
		int bY = height - 50 - 10;
		int tfWidth = (width - 20) / 2;
		int ntfX = 15 + tfWidth;
		int tfY = bY - 50 - 10;
		int lHeight = height - 105 - 50 - 40;

		this.listPanel.setBounds(5, 70, width - 10, lHeight);

		this.oldGLField.setBounds(5, tfY, tfWidth, 50);
		this.newGLField.setBounds(ntfX, tfY, tfWidth, 50);

		this.assertBtn.setBounds(5, bY, bWidth, 50);
		this.updateBtn.setBounds(5 + bWidth + 10, bY, bWidth, 50);
		this.retractBtn.setBounds(5 + (bWidth + 10) * 2, bY, bWidth, 50);
	}
}