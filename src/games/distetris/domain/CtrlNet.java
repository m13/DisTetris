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

	private Vector<TCPConnection> connections;
	private Vector<Vector<Integer>> teamPlayer;
	private Integer pointerTeamPlayer = 0;
	
	private Integer numPlayers;
	private Integer numTeams;
	
	private Thread threadTCPServer;
	private Thread threadTCPServerSend;
	
	private UDPServer threadUDPServer;

	private WifiManager wifiManager;
	
	private CtrlNet() {
		L.d("Created");

		this.connections = new Vector<TCPConnection>();
		this.teamPlayer = new Vector<Vector<Integer>>();
	}

	public static CtrlNet getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CtrlNet();
		}
		return INSTANCE;
	}
	
	public void serverTCPStart(int numPlayers, int numTeams, int numTurns) {
		this.numPlayers = numPlayers;
		this.numTeams = numTeams;
		
		for (int i = 0; i<(numTeams); i++) {
			Vector<Integer> seq = new Vector<Integer>();
			teamPlayer.add(seq);
		}
		
		this.threadTCPServer = new Thread(new TCPServer(connections, numTeams, numTurns));
		this.threadTCPServer.start();
		
		this.threadTCPServerSend = new Thread(new TCPServerSend(connections));
		this.threadTCPServerSend.start();
	}

	
	public void serverTCPConnect(String ip, Handler handler) {
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

	public void serverUDPStart(Handler handler) {
		// If a previous server is already running, stop it
		serverUDPStop();

		this.threadUDPServer = new UDPServer(UDPServer.MODE_SERVER, handler);
		this.threadUDPServer.start();
		L.d("Its running!");
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
						//return inetAddress.getHostAddress().toString();
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
}
