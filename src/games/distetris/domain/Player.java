package games.distetris.domain;

import java.io.IOException;

public class Player {

	private String name;
	private TCPConnection connection;

	public Player(TCPConnection connection) throws IOException {
		this.connection = connection;
		this.name = connection.in();
	}

	public String getName() {
		return name;
	}

	public String in() throws IOException {
		return connection.in();
	}

	public void out(String content) {
		connection.out(content);
	}

}
