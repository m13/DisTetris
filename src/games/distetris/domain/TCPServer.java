package games.distetris.domain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import android.util.Log;

public class TCPServer extends Thread {
	
	private Vector<TCPConnection> connections;
	private Integer numTeams;
	private Integer numTurns;
	private Boolean keepRunning;
	ServerSocket serverSocket;
	
	public TCPServer(Vector<TCPConnection> connections, int numTeams, int numTurns) {
		super();
		this.connections = connections;
		this.numTeams = numTeams;
		this.numTurns = numTurns;
		this.keepRunning = true;
	}
	
	public void run() {
	
		try {
			Log.d("TCP", "S: Connecting...");
			serverSocket = new ServerSocket(CtrlNet.PORT);
			
			while (keepRunning) {
				Socket client = serverSocket.accept();
				TCPConnection conn = new TCPConnection(client);
				connections.add(conn);
				conn.out("WAITING " + (connections.size()) + "," + (numTeams - 1) + "," + numTurns);
			}
		
		} catch (Exception e) {
			Log.e("TCP", "S: Error", e);
		}
	}

	public void close() {
		try {
			this.keepRunning = false;
			this.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
