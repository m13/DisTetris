package games.distetris.domain;

import java.util.Vector;

public class TCPServerSend extends Thread {

	private Vector<Player> players;
	private String data;
	private Vector<String> data_vector;
	private Integer mode;

	public TCPServerSend(Vector<Player> players, String data) {
		super();

		setName("TCPServerSend");

		this.players = players;
		this.data = data;

		this.mode = 1;
	}

	public TCPServerSend(Vector<Player> players, Vector<String> data) {
		super();

		setName("TCPServerSend");

		this.players = players;
		this.data_vector = data;

		this.mode = 2;
	}

	public TCPServerSend(Vector<Player> players) {
		super();

		setName("TCPServerSend");

		this.players = players;

		this.mode = 3;
	}

	public void run() {

		if (mode == 1) {
			//Just send one thing

			for (int i = (this.players.size() - 1); i >= 0; i--) {
				try {
					L.d("sending to player " + i + " " + data);
					this.players.get(i).out(data);
				} catch (Exception e) {

					// If the data we are sending is SHUTDOWN ignore the closed sockets and continue sending
					if (!data.equals("SHUTDOWN")) {
						CtrlDomain.getInstance().disconnectionDetected(this.players.get(i).getConnection());
					}
				}
			}

		} else if (mode == 2) {
			// Send more than one thing

			for (int d = 0; d < data_vector.size(); d++) {
				for (int i = (this.players.size() - 1); i >= 0; i--) {
					try {
						L.d("sending to player " + i + " " + data_vector.get(d));
						this.players.get(i).out(data_vector.get(d));
					} catch (Exception e) {
						CtrlDomain.getInstance().disconnectionDetected(this.players.get(i).getConnection());
					}
				}
			}

		} else if (mode == 3) {
			// Send the startGame
			
			
			String board = "UPDATEBOARD " + CtrlDomain.getInstance().serialize(CtrlGame.getInstance().getBoardToSend());

			for (int i = 0; i < this.players.size(); i++) {
				try {
					L.d("sending to player " + i + " " + board);
					this.players.get(i).out(board);
				} catch (Exception e) {
					CtrlDomain.getInstance().disconnectionDetected(this.players.get(i).getConnection());
				}
			}

			// TODO: Ugly fix, wait between sending packets
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
			}


			Vector<String> turns = new Vector<String>();
			Integer turn = CtrlDomain.getInstance().getCurrentTurn();

			for (int i = 0; i < this.players.size(); i++) {
				if (i == turn) {
					turns.add("UPDATEMYTURN " + CtrlDomain.getInstance().getServerNumTurns());
				} else {
					turns.add("UPDATEMYTURN 0");
				}
			}

			for (int i = 0; i < this.players.size(); i++) {
				try {
					L.d("sending to player " + i + " " + turns.get(i));
					this.players.get(i).out(turns.get(i));
				} catch (Exception e) {
					CtrlDomain.getInstance().disconnectionDetected(this.players.get(i).getConnection());
				}
			}

			// TODO: Ugly fix, wait between sending packets
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
			}

			String text_to_start = "STARTGAME";

			for (int i = 0; i < this.players.size(); i++) {
				try {
					L.d("Sending to player " + i + " STARTGAME");
					this.players.get(i).out(text_to_start);
				} catch (Exception e) {
					CtrlDomain.getInstance().disconnectionDetected(this.players.get(i).getConnection());
				}
			}
		}
	}
}