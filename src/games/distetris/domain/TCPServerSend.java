package games.distetris.domain;

import java.util.Vector;

public class TCPServerSend extends Thread {

	private Vector<Player> players;
	private String data;

	public TCPServerSend(Vector<Player> players, String data) {
		super();

		setName("TCPServerSend");

		this.players = players;
		this.data = data;
	}

	public void run() {

		for (int i = (players.size() - 1); i >= 0; i--) {
			try {
				players.get(i).out(data);
			} catch (Exception e) {
				// TODO: check for disconnection, really necessary? The SHUTDOWN command uses this
			}
		}

	}
}