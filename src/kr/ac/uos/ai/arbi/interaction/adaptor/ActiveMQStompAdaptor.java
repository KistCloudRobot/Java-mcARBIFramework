package kr.ac.uos.ai.arbi.interaction.adaptor;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;
import org.apache.activemq.transport.stomp.Stomp.Headers.Subscribe;

import kr.ac.uos.ai.arbi.interaction.InteractionManager;
import kr.ac.uos.ai.arbi.interaction.InteractionManagerBrokerConfiguration;
import kr.ac.uos.ai.arbi.interaction.MonitorMessageQueue;
import kr.ac.uos.ai.arbi.interaction.adaptor.SocketAdaptor.SocketThread;
public class ActiveMQStompAdaptor extends Thread implements InteractionMessageAdaptor{

	private StompConnection stompConnection;
	private MonitorMessageQueue queue;
	
	public ActiveMQStompAdaptor(MonitorMessageQueue queue) {
		this.queue = queue;
		try {
			String[] addr = InteractionManagerBrokerConfiguration.StompBroker.split(":");
			String url = addr[1].substring(2);
			String port = addr[2];
			System.out.println("[ addr ] " +url +":"+port);
			
			stompConnection = new StompConnection();
			stompConnection.open(url, Integer.parseInt(port));
			stompConnection.connect("system", "manager");
			stompConnection.subscribe("/queue/interactionManager", Subscribe.AckModeValues.CLIENT);
		   
			
			this.start();
			
		    } catch (UnknownHostException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				//stompConnection.begin("monitorAction");
				StompFrame message = stompConnection.receive(0);
				//stompConnection.ack(message, "monitorAction");
				//stompConnection.commit("monitorAction");
			    System.out.println("[ Stomp Message ] " +message.getBody());
				
			    queue.enqueue(message.getBody());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}

	}
	
	public void close() {
		try {
			stompConnection.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	@Override
	public void send(String monitorID, String message) {
		// TODO Auto-generated method stub
		try {
			stompConnection.begin("log");
			System.out.println("[ Stomp Log ] " +message);
			stompConnection.send("/queue/"+monitorID, message);
			stompConnection.commit("log");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	public void sendStatus(String status) {
		// TODO Auto-generated method stub
		
	}
	
	
}
