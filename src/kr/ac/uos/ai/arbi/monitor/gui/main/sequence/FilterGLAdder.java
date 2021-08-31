/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package kr.ac.uos.ai.arbi.monitor.gui.main.sequence;

import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.monitor.model.AbstractListModel;

class FilterGLAdder implements Runnable {
	private AbstractListModel<GeneralizedList> listModel;
	private GeneralizedList gl;

	FilterGLAdder(AbstractListModel<GeneralizedList> listModel, GeneralizedList gl) {
		this.listModel = listModel;
		this.gl = gl;
	}

	public void run() {
		if ((this.listModel == null) || (this.gl == null))
			return;
		this.listModel.add(this.gl);
	}
}