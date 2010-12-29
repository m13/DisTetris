package games.distetris.domain;

import java.util.Vector;

public class TCPServerSend extends Thread {

	private Vector<Player> players;
	private String data;

	public TCPServerSend(Vector<Player> players, String data) {
		super();
		this.players = players;
		this.data = data;
	}

	public void run() {

		for (Player p : players) {
			p.out(data);
		}

	}
}