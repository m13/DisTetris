package games.distetris.domain;

import java.net.Socket;
import java.util.Vector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TCPClient implements Runnable {
	
	private String ip;
	private int port;
	private Vector<TCPConnection> connections;
	private Handler handler;
	
	public TCPClient(String ip, Vector<TCPConnection> connections, Handler handler) {
		this.ip = ip;
		this.port = 3333;
		this.connections = connections;
		this.handler = handler;
	}
	
	public void run() {
		
		try {
			Log.d("TCP", "C: Connecting...");
			
			Socket socket = new Socket(ip, port);
			connections.add(0, new TCPConnection(socket));
			try {
				String received;
				while ((received = connections.firstElement().in()) != null) {
					Log.d("TCP", "C: Received");
					sendMsg("MSG", received);
				}
				
			} catch (Exception e) {
				Log.e("TCP", "S: Error", e);
			} finally {
				connections.firstElement().close();
			}
		} catch (Exception e) {
			Log.e("TCP", "C: Error", e);
		}
	}
	
	public void sendMsg(String type, String content) {
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString(type, content);
		msg.setData(data);
		handler.sendMessage(msg);
	}
}
