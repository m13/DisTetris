package games.distetris.domain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class TCPServer extends Thread {
	
	ServerSocket serverSocket;

	private Vector<Player> players;

	private Integer numTeams;
	private Integer numTurns;
	private Integer numPlayerLastAssigned;
	private Integer numTeamLastAssigned;

	private Boolean keepRunning;
	private Boolean listening;
	
	public TCPServer(Vector<Player> players, Integer numTeams, Integer numTurns) {
		super();

		setName("TCPServer");

		this.players = players;

		this.numTeams = numTeams;
		this.numTurns = numTurns;
		this.numPlayerLastAssigned = 0;
		this.numTeamLastAssigned = 0;

		this.keepRunning = true;
		this.listening = false;
	}
	
	public void run() {
	
		try {

			serverSocket = new ServerSocket(CtrlNet.PORT);

			L.d("TCPServer Started");

			this.listening = true;
			
			while (keepRunning) {
				Socket client = serverSocket.accept();
				Player new_player = new Player(new TCPConnection(client), this.numPlayerLastAssigned, this.numTeamLastAssigned);
				this.numPlayerLastAssigned++;
				this.numTeamLastAssigned = (++this.numTeamLastAssigned) % numTeams;
				players.add(new_player);
				CtrlDomain.getInstance().updatedPlayers();
			}
		
		} catch (Exception e) {
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

	public Boolean isListening() {
		return this.listening;
	}
}
