package kr.ac.uos.ai.arbi.monitor.gui.main.wm;

import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.monitor.model.AbstractListModel;

public class WMDumper  implements Runnable {
	private AbstractListModel<GeneralizedList> listModel;
	private GeneralizedList[] list;

	WMDumper(AbstractListModel<GeneralizedList> listModel, GeneralizedList[] list) {
		this.listModel = listModel;
		this.list = list;
	}

	public void run() {
		if ((this.listModel == null) || (this.list == null))
			return;
		this.listModel.clear();
		this.listModel.addAll(this.list);
	}
}