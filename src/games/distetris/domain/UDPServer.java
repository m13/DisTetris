package games.distetris.domain;

import games.distetris.presentation.NewGameListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.InvalidParameterException;

import android.util.Log;

public class UDPServer extends Thread {

	public static Integer MODE_SERVER = 1;
	public static Integer MODE_CLIENT = 2;
	private NewGameListener listener;
	private DatagramSocket socket;
	private Boolean keepRunning;
	private int mode;

	/**
	 * The mode parameter tells the UDPServer if is acting as a SERVER (1) or
	 * CLIENT (2)
	 * 
	 * SERVER: will answer all incoming packets
	 * 
	 * CLIENT: will update the UI with all incoming packets (in case there are
	 * multiple servers)
	 */
	public UDPServer(int mode, NewGameListener listener) {

		if (mode != 1 && mode != 2) {
			throw new InvalidParameterException();
		}

		this.listener = listener;
		this.keepRunning = true;
		this.mode = mode;

		// Special socket (catches broadcast) on specified port
		try {
			socket = new DatagramSocket(CtrlNet.PORT);
			socket.setBroadcast(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			byte[] buf = new byte[1024];

			//Listen on socket to receive messages 
			while (keepRunning) {

				L.d("Inside while");

				// Wait for a new packet 
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				L.d("Datagram received");

				InetAddress remoteIP = packet.getAddress();
				if (remoteIP.equals(CtrlNet.getInstance().getLocalAddress()))
					continue;

				String content = new String(packet.getData(), 0, packet.getLength());
				Log.d("UDPServer", "Received response " + content);

				if (mode == 1) {

					// SERVER
					L.d("Mode server");

					// Send the obtained bytes to the UI Activity
					listener.addEvent("[C] " + remoteIP + " " + content);

					// Send an answer to the client
					sendIP(remoteIP, "PONG");

				} else if (mode == 2) {

					// CLIENT
					L.d("Mode client");

					listener.addEvent("[S] " + remoteIP + " " + content);

				}


			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void close() {
		this.keepRunning = false;
		socket.close();
	}

	public void sendBroadcast(String data) {
		try {
			DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), CtrlNet.getInstance().getBroadcastAddress(), CtrlNet.PORT);
			socket.send(packet);
			L.d("Datagram sent");
		} catch (Exception e) {
			Log.e("UDPServer", "Exception during sendBroadcast", e);
		}
	}

	public void sendIP(InetAddress ip, String data) {
		try {
			DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), ip, CtrlNet.PORT);
			socket.send(packet);
			L.d("Datagram sent");
		} catch (Exception e) {
			Log.e("UDPServer", "Exception during sendIP", e);
		}
	}

}
