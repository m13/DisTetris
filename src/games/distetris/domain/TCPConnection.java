package games.distetris.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Bundle;
import android.os.Message;

public class TCPConnection extends Thread {

	private Socket socket;

	private Boolean keepRunning;

	public TCPConnection(String ip, int port) {
		super();

		try {

			this.socket = new Socket(ip, port);

			this.keepRunning = true;

		} catch (IOException e) {
			// TODO: error when establishing a connection?
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

			String received;
			while (keepRunning && (received = in()) != null) {
				sendMsg("MSG", received);
			}

			if (keepRunning) {
				L.e("CONNECTION READ NULL");
				CtrlDomain.getInstance().disconnectionDetected(this);
			}

		} catch (Exception e) {
			L.e("CONNECTION READ EXCEPTION");
			CtrlDomain.getInstance().disconnectionDetected(this);
		}

		L.d("Thread TCPConnection ended the run()");

	}

	public String in() throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String result = in.readLine();
		return result;
	}

	public void out(String content) throws Exception {
		PrintWriter out;
		out = new PrintWriter(socket.getOutputStream(), true);
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

		L.d("Closing TCPConnection");

		this.keepRunning = false;

		try {
			this.socket.shutdownInput();
		} catch (IOException e) {
		}

		try {
			this.socket.close();
		} catch (IOException e) {
		}
	}


}
