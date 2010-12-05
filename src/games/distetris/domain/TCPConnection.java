package games.distetris.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPConnection {

	private Socket socket;
	PrintWriter out;
	BufferedReader in;
	String name;

	public TCPConnection(Socket socket) {
		try {
			this.socket = socket;
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void out(String content) {
		out.println(content);
	}

	public String in() throws IOException {
		return in.readLine();
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return this.name;
	}

}
