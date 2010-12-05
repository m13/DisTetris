package games.distetris.domain;

import java.net.Socket;
import java.util.Vector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class TCPClient extends Thread {
	
	private String ip;
	private int port;
	private Vector<TCPConnection> connections;
	private Handler handler;
	
	public TCPClient(String ip, int port, Vector<TCPConnection> connections, Handler handler) {
		this.ip = ip;
		this.port = port;
		this.connections = connections;
		this.handler = handler;
	}
	
	public void run() {
		
		try {
			L.d("C: Connecting...");
			
			Socket socket = new Socket(ip, port);
			connections.add(0, new TCPConnection(socket));
			try {
				String received;
				while ((received = connections.firstElement().in()) != null) {
					L.d("C: Received");
					sendMsg("MSG", received);
				}
				
			} catch (Exception e) {
				L.e("S: Error");
			} finally {
				connections.firstElement().close();
			}
		} catch (Exception e) {
			L.e("C: Error");
		}
	}
	
	private void sendMsg(String type, String content) {
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString(type, content);
		msg.setData(data);
		handler.sendMessage(msg);
	}
}
