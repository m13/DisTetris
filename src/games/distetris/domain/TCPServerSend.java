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
				Thread.sleep(20000);
				for (TCPConnection conn : connections) {
					// just for testing!
					conn.out("ERROR 0");
				}
			}
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	}
}