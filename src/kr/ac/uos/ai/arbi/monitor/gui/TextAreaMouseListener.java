package kr.ac.uos.ai.arbi.monitor.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

public class TextAreaMouseListener implements MouseListener, ActionListener {
	private JTextArea textArea;
	private JPopupMenu popup;

	public TextAreaMouseListener(JTextArea textArea) {
		this.textArea = textArea;
		this.popup = new JPopupMenu();

		JMenuItem menu = new JMenuItem("Copy");
		menu.setMnemonic('c');
		menu.addActionListener(this);
		this.popup.add(menu);
	}

	public void mouseClicked(MouseEvent event) {
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mouseReleased(MouseEvent event) {
	}

	public void mousePressed(MouseEvent event) {
		if (event.getModifiers() == 4)
			this.popup.show(event.getComponent(), event.getX(), event.getY());
	}

	public void actionPerformed(ActionEvent e) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		String str = this.textArea.getSelectedText();
		if (str != null) {
			StringSelection contents = new StringSelection(str);
			clipboard.setContents(contents, null);
		}
	}
}