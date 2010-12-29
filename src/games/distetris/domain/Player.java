package games.distetris.domain;

import java.io.IOException;

public class Player {

	private String name;
	private Integer numTeam;
	private TCPConnection connection;

	public Player(TCPConnection connection, Integer numTeam) throws IOException {
		this.connection = connection;
		this.name = connection.in();
		this.numTeam = numTeam;
		this.connection.start();
	}

	public String getName() {
		return name;
	}

	public String in() throws IOException {
		return connection.in();
	}

	public void out(String content) throws IOException {
		connection.out(content);
	}

	public Integer getTeam() {
		return this.numTeam;
	}

}
