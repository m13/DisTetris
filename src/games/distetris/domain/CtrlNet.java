package games.distetris.domain;

import java.util.Vector;

import android.os.Handler;

public class CtrlNet {
	
	private Vector<TCPConnection> connections;
	private Vector<Vector<Integer>> teamPlayer;
	private Integer pointerTeamPlayer = 0;
	
	private Integer numPlayers;
	private Integer numTeams;
	
	
	CtrlNet() {
		this.connections = new Vector<TCPConnection>();
		this.teamPlayer = new Vector<Vector<Integer>>();
	}
	
	
	public void createServer(int numPlayers, int numTeams, int numTurns) {
		this.numPlayers = numPlayers;
		this.numTeams = numTeams;
		
		for (int i = 0; i<(numTeams); i++) {
			Vector<Integer> seq = new Vector<Integer>();
			teamPlayer.add(seq);
		}
		
		Thread sThread = new Thread(new TCPServer(connections, numTeams, numTurns));
		sThread.start();
		
		Thread sendThread = new Thread(new TCPServerSend(connections));
		sendThread.start();
	}

	
	public void connectServer(String ip, Handler handler) {
		Thread cThread = new Thread(new TCPClient(ip, connections, handler));
		cThread.start();
	}


	public void sendSignal(String string) {
		connections.lastElement().out(string);
	}
	
	public void sendSignals(String string) {
		for (TCPConnection conn : connections) {
			conn.out(string);
		}
	}


	public int registerPlayer(int posPlayer, int chosenTeam) {
		Integer pos = new Integer(posPlayer);
		(teamPlayer.elementAt(chosenTeam)).add(pos);
		numPlayers--;
		return numPlayers;
	}


	public void nextPlayer() {
		do {
			pointerTeamPlayer++;
			if (pointerTeamPlayer>=numTeams) {
				pointerTeamPlayer = 0;
			}
		} while (teamPlayer.get(pointerTeamPlayer).isEmpty());
		
		Integer first = teamPlayer.get(pointerTeamPlayer).remove(0);
		teamPlayer.get(pointerTeamPlayer).add(first);
		
		Integer firstPlayerPos = teamPlayer.get(pointerTeamPlayer).get(0);
		connections.elementAt(firstPlayerPos).out("CONTINUE ");
	}
}
