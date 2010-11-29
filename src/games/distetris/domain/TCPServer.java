package games.distetris.domain;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import android.util.Log;

public class TCPServer implements Runnable {

	public static final String SERVERIP = "10.0.2.2";
	public static final int SERVERPORT = 4444;
	
	private Vector<TCPConnection> connections;
	private Integer numTeams;
	private Integer numTurns;
	
	public TCPServer(Vector<TCPConnection> connections, int numTeams, int numTurns) {
		super();
		this.connections = connections;
		this.numTeams = numTeams;
		this.numTurns = numTurns;
	}
	
	public void run() {
	
		try {
			Log.d("TCP", "S: Connecting...");
			ServerSocket serverSocket = new ServerSocket(SERVERPORT);
			
			while (true) {
				Socket client = serverSocket.accept();
				TCPConnection conn = new TCPConnection(client);
				connections.add(conn);
				conn.out("WAITING " + (connections.size()) + "," + (numTeams-1) + "," + numTurns);
			}
		
		} catch (Exception e) {
			Log.e("TCP", "S: Error", e);
		}
	}
}
