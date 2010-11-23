package games.distetris.domain;

import java.util.Vector;

public class TCPServerSend implements Runnable {

	public static final String SERVERIP = "10.0.2.2";
	public static final int SERVERPORT = 4444;

	private Vector<TCPConnection> connections;

	public TCPServerSend(Vector<TCPConnection> connections) {
		super();
		this.connections = connections;
	}

	public void run() {

		try {
			while (true) {
				Thread.sleep(10000);
				for (TCPConnection i : connections) {
					i.out("coses");
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
