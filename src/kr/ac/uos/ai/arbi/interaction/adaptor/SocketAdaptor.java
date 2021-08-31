package kr.ac.uos.ai.arbi.interaction.adaptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.ac.uos.ai.arbi.interaction.InteractionManagerBrokerConfiguration;
import kr.ac.uos.ai.arbi.interaction.MonitorMessageQueue;


public class SocketAdaptor extends Thread implements InteractionMessageAdaptor {

	private ServerSocket serverSocket = null;
	private HashMap<String, Socket> socketMap;
	private MonitorMessageQueue queue;

	public SocketAdaptor(MonitorMessageQueue queue) {
		this.queue = queue;
		try {
			String[] brokerAddress = InteractionManagerBrokerConfiguration.SocketBroker.split(":");
			String addr = brokerAddress[1].substring(2);
			int port = Integer.parseInt(brokerAddress[2]);

			serverSocket = new ServerSocket(port);
			socketMap = new HashMap<>();

			this.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				socket.setSoTimeout(0);
				new SocketThread(socket).start();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}

	}

	class SocketThread extends Thread {

		private Socket socket;

		public SocketThread(Socket socket) {
			// TODO Auto-generated constructor stub
			this.socket = socket;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {

				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String message;
				while ((message = br.readLine()) != null) {

					JSONParser jsonParser = new JSONParser();
					JSONObject jsonMessage = (JSONObject) jsonParser.parse(message);

					socketMap.put(jsonMessage.get("ID").toString(), socket);

					queue.enqueue(message);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} finally {
				try {
					socket.close();
					socketMap.remove(socket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				
			}
		}

	}

	@Override
	public void send(String monitorID, String message) {
		// TODO Auto-generated method stub
		try {
			Socket socket = null;

			if (socketMap.containsKey(monitorID))
				socket = socketMap.get(monitorID);

			if (socket == null)
				return;
			
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			pw.println(message);
			pw.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	@Override
	public void sendStatus(String status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

}
