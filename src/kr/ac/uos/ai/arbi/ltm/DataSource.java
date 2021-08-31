package kr.ac.uos.ai.arbi.ltm;

import kr.ac.uos.ai.arbi.agent.datastream.DataStreamToolkit;
import kr.ac.uos.ai.arbi.ltm.communication.DataCenterInterfaceToolkit;

public class DataSource {
	private boolean running;
	private String dataSourceURI;
	private DataCenterInterfaceToolkit dataInterfaceToolkit;

	public DataSource() {

	}
	
	public void close() {
		dataInterfaceToolkit.close();
		this.running = false;
	}
	
	public final void connect(String brokerURL, String dataSourceURI, int brokerType) {
		this.dataSourceURI = dataSourceURI;
		running = true;
		dataInterfaceToolkit = new DataCenterInterfaceToolkit(brokerURL, dataSourceURI, this, brokerType);
	}

	public void assertFact(String fact) {
		dataInterfaceToolkit.assertFact(this.dataSourceURI, fact);
	}

	public String retractFact(String fact) {
		return dataInterfaceToolkit.retractFact(this.dataSourceURI, fact);
	}

	public String retrieveFact(String fact) {
		return dataInterfaceToolkit.retrieveFact(this.dataSourceURI, fact);
	}

	public void updateFact(String fact) {
		dataInterfaceToolkit.updateFact(this.dataSourceURI, fact);
	}

	public String match(String fact) {
		return dataInterfaceToolkit.match(this.dataSourceURI, fact);
	}

	public String subscribe(String rule) {
		return dataInterfaceToolkit.subscribe(this.dataSourceURI, rule);
	}

	public void unsubscribe(String subID) {
		dataInterfaceToolkit.unsubscribe(this.dataSourceURI, subID);
	}

	public void onNotify(String content) {
	}

	public DataStreamToolkit registerStream(String rule) {
		return dataInterfaceToolkit.registerStream(this.dataSourceURI, rule);
	}
	
	public String getLastModifiedTime(String content) {
		return dataInterfaceToolkit.getLastModifiedTime(this.dataSourceURI,content);
	}
	
	public boolean isRunning() {
		return running;
	}

}
