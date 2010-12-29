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
		super();

		try {
			this.ip = ip;
			this.port = port;

			this.socket = new Socket(ip, port);

			this.keepRunning = true;

		} catch (IOException e) {
			e.printStackTrace();
			CtrlNet.getInstance().removeConnection(this);
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
				L.e("REMOVING ACTIVE CONNECTION");
				CtrlNet.getInstance().removeConnection(this);
			} finally {
				CtrlNet.getInstance().removeConnection(this);
				socket.close();
			}
		} catch (Exception e) {
			L.e("REMOVING ACTIVE CONNECTION");
			CtrlNet.getInstance().removeConnection(this);
		}

		L.d("Thread TCPConnection ended the run()");

	}

	public String in() throws Exception {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String result = in.readLine();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			CtrlNet.getInstance().removeConnection(this);
			throw new Exception();
		}
	}

	public void out(String content) throws Exception {
		PrintWriter out;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
			CtrlNet.getInstance().removeConnection(this);
			throw new Exception();
		}
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
		try {
			this.keepRunning = false;
			this.socket.shutdownInput();
			this.socket.close();
		} catch (IOException e) {
		}
	}


}
