package games.distetris.domain;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Vector;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

public class CtrlNet {
	
	public static Integer PORT = 5555;
	
	private static CtrlNet INSTANCE = null;

	private Vector<Player> players;
	private Vector<Vector<Integer>> teamPlayer;
	private Integer pointerTeamPlayer = 0;
	
	private Integer numTeams;
	
	private TCPServer threadTCPServer;
	private TCPConnection threadTCPClient;
	private UDPServer threadUDPServer;

	private WifiManager wifiManager;

	
	private CtrlNet() {
		L.d("Created");

		this.players = new Vector<Player>();
		this.teamPlayer = new Vector<Vector<Integer>>();
	}
	
	public static CtrlNet getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CtrlNet();
		}
		return INSTANCE;
	}
	
	public void serverTCPStart(int numTeams, int numTurns, Handler handler) {

		// TODO: Revisar! Sergio's code
		this.numTeams = numTeams;
		for (int i = 0; i<(numTeams); i++) {
			Vector<Integer> seq = new Vector<Integer>();
			teamPlayer.add(seq);
		}
		
		// Creating server
		this.threadTCPServer = new TCPServer(players, numTeams, numTurns);
		this.threadTCPServer.start();

		// Waiting for the server to start
		while (!this.threadTCPServer.isAlive()) {
			;
		}
		while (!this.threadTCPServer.isListening()) {
			;
		}

		// Connecting like a normal client
		this.threadTCPClient = new TCPConnection("127.0.0.1", PORT, handler);
		this.threadTCPClient.out(CtrlDomain.getInstance().getPlayerName());
		this.threadTCPClient.start();
	}

	
	public void serverTCPConnect(String ip, int port, Handler handler) {
		//TODO: check if the connections of the vector are already closed
		this.players.clear();

		this.threadTCPClient = new TCPConnection(ip, port, handler);
		this.threadTCPClient.out(CtrlDomain.getInstance().getPlayerName());
		this.threadTCPClient.start();
	}

	public void serverTCPStop() {
		if (this.threadTCPServer != null && this.threadTCPServer.isAlive()) {
			this.threadTCPServer.close();
			while (this.threadTCPServer.isAlive()) {
				;
			}
		}
		L.d("Closed");
	}


	public void sendSignal(String string) {
		threadTCPClient.out(string);
	}
	
	public void sendSignals(String string) {
		TCPServerSend threadTCPServerSend = new TCPServerSend(players, string);
		threadTCPServerSend.start();
	}


	public void registerPlayer(int posPlayer, int chosenTeam) {
		Integer pos = new Integer(posPlayer);
		(teamPlayer.elementAt(chosenTeam)).add(pos);
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
		players.elementAt(firstPlayerPos).out("CONTINUE ");
	}

	public void serverUDPStart() {
		// If a previous server is already running, stop it
		serverUDPStop();

		this.threadUDPServer = new UDPServer(UDPServer.MODE_SERVER, null);
		this.threadUDPServer.start();
	}

	public void serverUDPFind(Handler handler) {
		// If a previous server is already running, stop it
		serverUDPStop();

		this.threadUDPServer = new UDPServer(UDPServer.MODE_CLIENT, handler);
		this.threadUDPServer.start();
		L.d("Its running!");
		this.threadUDPServer.sendBroadcast("PING");
		L.d("Sent PING");
	}

	public void serverUDPStop() {
		if (this.threadUDPServer != null && this.threadUDPServer.isAlive()) {
			this.threadUDPServer.close();
			while (this.threadUDPServer.isAlive()) {
				;
			}
		}
		L.d("Closed");
	}

	public InetAddress getBroadcastAddress() throws IOException {
		DhcpInfo dhcp = wifiManager.getDhcpInfo();

		if (dhcp == null) {
			Log.d("NET", "Could not get dhcp info");
			return null;
		}

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}

	public InetAddress getLocalAddress() throws IOException {

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress;
					}
				}
			}
		} catch (SocketException e) {
			Log.e("NET", e.toString());
		}
		return null;
	}

	public void setWifiManager(WifiManager systemService) {
		L.d("Wifi set");
		this.wifiManager = systemService;
	}

	public String[] serverTCPGetConnectedPlayers() {
		Vector<String> n = new Vector<String>();

		for (int i = 0; i < players.size(); i++) {
			n.add(players.get(i).getName());
		}

		String[] st = new String[n.size()];
		n.toArray(st);

		return st;
	}

}
