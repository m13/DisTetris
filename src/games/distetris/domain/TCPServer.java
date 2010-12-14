package games.distetris.domain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import android.os.Handler;
import android.util.Log;

public class TCPServer extends Thread {
	
	ServerSocket serverSocket;

	private Vector<TCPConnection> connections;

	private Integer numTeams;
	private Integer numPlayers;
	private Integer numTurns;

	private Boolean keepRunning;

	private Handler handler;
	
	public TCPServer(Vector<TCPConnection> connections, int numTeams, int numPlayers, int numTurns, Handler handler) {
		super();

		this.connections = connections;

		this.numTeams = numTeams;
		this.numPlayers = numPlayers;
		this.numTurns = numTurns;

		this.handler = handler;

		this.keepRunning = true;
	}
	
	public void run() {
	
		try {
			Log.d("TCP", "S: Connecting...");
			serverSocket = new ServerSocket(CtrlNet.PORT);
			
			while (keepRunning) {
				Socket client = serverSocket.accept();
				TCPConnection conn = new TCPConnection(client, handler);
				connections.add(conn);
				conn.out("WAITING " + (connections.size()) + "," + (numTeams - 1) + "," + numTurns);
				CtrlDomain.getInstance().updatedPlayers();
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
