package games.distetris.domain;

import java.net.Socket;
import java.util.Vector;

import android.util.Log;

public class TCPClient implements Runnable {

	private String ip;
	private int port;
	private Vector<TCPConnection> connections;

	public TCPClient(String ip, Vector<TCPConnection> connections) {
		this.ip = ip;

		//DEBUG: remove for final version
		this.ip = "10.0.2.2";
		this.port = 3333;

		this.connections = connections;
	}

	public void run() {
		try {

			Log.d("TCP", "C: Connecting...");

			Socket socket = new Socket(ip, port);
			connections.add(new TCPConnection(socket));
			try {
				String received;
				while ((received = connections.firstElement().in()) != null) {
					Log.d("TCP", "C: Received");
					Log.d("TCP", received);
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
}
