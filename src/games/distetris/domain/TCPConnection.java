package games.distetris.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class TCPConnection extends Thread {

	private String ip;
	private int port;
	private Socket socket;

	private PrintWriter out;
	private BufferedReader in;

	private Handler handler;

	private Boolean keepRunning;

	public TCPConnection(String ip, int port, Handler handler) {
		try {
			this.ip = ip;
			this.port = port;

			this.socket = new Socket(ip, port);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream(), true);

			this.handler = handler;
			this.keepRunning = true;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TCPConnection(Socket socket) {
		try {
			this.socket = socket;
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream(), true);

			this.handler = null;
			this.keepRunning = true;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
		super.run();

		try {
			try {

				String received;
				while (keepRunning && (received = in()) != null) {
					sendMsg("MSG", received);
				}

			} catch (Exception e) {
				L.e("S: Error");
			} finally {
				socket.close();
			}
		} catch (Exception e) {
			L.e("C: Error");
		}
	}

	public String in() throws IOException {
		return in.readLine();
	}

	public void out(String content) {
		out.println(content);
	}

	private void sendMsg(String type, String content) {
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString(type, content);
		msg.setData(data);
		handler.sendMessage(msg);
	}

	public void close() {
		try {
			this.keepRunning = false;
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
