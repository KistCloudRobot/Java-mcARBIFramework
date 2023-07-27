package kr.ac.uos.ai.arbi.framework.broker;

import org.apache.activemq.apollo.broker.Broker;
import org.apache.activemq.apollo.dto.AcceptingConnectorDTO;
import org.apache.activemq.apollo.dto.BrokerDTO;
import org.apache.activemq.apollo.dto.VirtualHostDTO;

public class ApolloBroker implements kr.ac.uos.ai.arbi.framework.broker.Broker  {
	private Broker broker;

	public ApolloBroker(String host, int port) {
		broker = new Broker();
		String url = "tcp://" + host + ":" + port;
		broker.setConfig(createConfig(url));
	}

	private BrokerDTO createConfig(String url) {
		// TODO Auto-generated method stub
		
		BrokerDTO broker = new BrokerDTO();

		VirtualHostDTO host = new VirtualHostDTO();
		host.id = "localhost";
		
		broker.virtual_hosts.add(host);

		AcceptingConnectorDTO connector = new AcceptingConnectorDTO();
		connector.id = "tcp";
		connector.bind = url;
		broker.connectors.add(connector);
		
		return broker;
	}

	public void start() {
		broker.start(new Runnable() {
			public void run() {
				System.out.println("The broker has now started.");
			}
		});

	}

}
