/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package kr.ac.uos.ai.arbi.monitor.gui.main.sequence;

import kr.ac.uos.ai.arbi.monitor.model.FilterModel;

public class ModelUpdater implements Runnable {
	private FilterModel filterModel;
	private String type;
	private boolean value;

	public ModelUpdater(FilterModel filterModel, String type, boolean value) {
		this.filterModel = filterModel;
		this.type = type;
		this.value = value;
	}

	public void run() {
		if (this.filterModel == null)
			return;
		if (this.value)
			this.filterModel.enableFilter(this.type);
		else
			this.filterModel.disableFilter(this.type);
	}
}