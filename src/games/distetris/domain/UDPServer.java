package games.distetris.domain;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.InvalidParameterException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UDPServer extends Thread {

	public static Integer MODE_SERVER = 1;
	public static Integer MODE_CLIENT = 2;
	private Handler handler;
	private DatagramSocket socket;
	private Boolean keepRunning;
	private int mode;

	/**
	 * The mode parameter tells the UDPServer if is acting as a SERVER (1) or
	 * CLIENT (2)
	 * 
	 * SERVER: will answer all incoming packets with a reply packet
	 * 
	 * CLIENT: will send a broadcast packet and update the UI with all the
	 * answers (in case there are multiple servers)
	 */
	public UDPServer(int mode, Handler handler) {

		if (mode != MODE_SERVER && mode != MODE_CLIENT) {
			throw new InvalidParameterException();
		}

		this.handler = handler;
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

				L.d("Remote IP: " + remoteIP);

				// Ignore if the packet received is sent by us
				// (this happens because broadcast traffic is sent to us too)
				if (remoteIP.equals(CtrlNet.getInstance().getLocalAddress()))
					continue;

				String content = new String(packet.getData(), 0, packet.getLength());
				L.d("Content: " + content);

				if (mode == 1) {

					// SERVER
					L.d("Mode server entered");

					// Send an answer to the client
					sendIP(remoteIP, CtrlDomain.getInstance().getName() + "|" + CtrlNet.PORT);

					L.d("Sent answer to client");

				} else if (mode == 2) {

					// CLIENT
					L.d("Mode client entered");

					sendUIServer(content.split("\\|")[0], remoteIP, content.split("\\|")[1]);

				}

				L.d("Finished work");

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

	private void sendUIServer(String name, InetAddress remoteIP, String port) {
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString("NAME", name);
		data.putString("IP", remoteIP.getHostAddress());
		data.putString("PORT", port);
		msg.setData(data);
		handler.sendMessage(msg);
	}

}
