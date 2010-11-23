package games.distetris.domain;

import java.net.ServerSocket;
import java.util.Vector;

public class CtrlDomain {

	private static CtrlDomain INSTANCE = null;
	private Listener listener;
	private ServerSocket s;
	private Vector<TCPConnection> connections;

	private CtrlDomain() {
		this.connections = new Vector<TCPConnection>();
	}

	public static CtrlDomain getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CtrlDomain();
		}
		return INSTANCE;
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public void createServer() {
		Thread sThread = new Thread(new TCPServer(connections));
		sThread.start();

		Thread sendThread = new Thread(new TCPServerSend(connections));
		sendThread.start();
	}

	public void connectServer(String ip) {
		Thread cThread = new Thread(new TCPClient(ip, connections));
		cThread.start();
	}
}
