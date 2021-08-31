/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package kr.ac.uos.ai.arbi.monitor.gui.main.sequence;

import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.monitor.model.AbstractListModel;

class FilterGLRemover implements Runnable {
	private AbstractListModel<GeneralizedList> listModel;
	private int index;

	FilterGLRemover(AbstractListModel<GeneralizedList> listModel, int index) {
		this.listModel = listModel;
		this.index = index;
	}

	public void run() {
		if (this.listModel == null)
			return;
		if (this.index < 0)
			return;
		this.listModel.remove(this.index);
	}
}