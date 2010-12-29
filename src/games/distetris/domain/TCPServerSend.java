package games.distetris.domain;

import java.io.IOException;
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
			try {
				p.out(data);
			} catch (IOException e) {
				// TODO: check for disconnection
			}
		}

	}
}