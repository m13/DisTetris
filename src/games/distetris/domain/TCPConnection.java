package games.distetris.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Bundle;
import android.os.Message;

public class TCPConnection extends Thread {

	private String ip;
	private int port;
	private Socket socket;

	private Boolean keepRunning;

	public TCPConnection(String ip, int port) {
		try {
			this.ip = ip;
			this.port = port;

			this.socket = new Socket(ip, port);

			this.keepRunning = true;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TCPConnection(Socket socket) {
		this.socket = socket;

		this.keepRunning = true;
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
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		return in.readLine();
	}

	public void out(String content) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println(content);
	}

	private void sendMsg(String type, String content) {
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString(type, content);
		msg.setData(data);
		CtrlDomain.getInstance().getHandlerDomain().sendMessage(msg);
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
