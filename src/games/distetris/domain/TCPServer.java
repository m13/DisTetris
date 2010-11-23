package games.distetris.domain;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import android.util.Log;

public class TCPServer implements Runnable {

	public static final String SERVERIP = "10.0.2.2";
	public static final int SERVERPORT = 4444;

	private Vector<TCPConnection> connections;

	public TCPServer(Vector<TCPConnection> connections) {
		super();
		this.connections = connections;
	}

	public void run() {

		try {

			Log.d("TCP", "S: Connecting...");
			ServerSocket serverSocket = new ServerSocket(SERVERPORT);

			while (true) {
				Socket client = serverSocket.accept();
				connections.add(new TCPConnection(client));
				/*
				Log.d("TCP", "S: Receiving...");
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					String str = in.readLine();
					Log.d("TCP", "S: Received: '" + str + "'");
				} catch (Exception e) {
					Log.e("TCP", "S: Error", e);
				} finally {
					client.close();
					Log.d("TCP", "S: Done.");
				}
				*/
			}

		} catch (Exception e) {
			Log.e("TCP", "S: Error", e);
		}
	}
}
