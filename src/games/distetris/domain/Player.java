package games.distetris.domain;


public class Player {

	private String name;
	private Integer numPlayer;
	private Integer numTeam;
	private TCPConnection connection;

	public Player(TCPConnection connection, Integer numPlayer, Integer numTeam) throws Exception {
		this.connection = connection;
		// The first thing we receive is the player name
		this.name = in();
		this.numPlayer = numPlayer;
		this.numTeam = numTeam;

		// The first thing we send is the player id and the team id assigned
		out(this.numPlayer.toString() + "|" + this.numTeam.toString());

		this.connection.setName("TCP server " + this.name);
		this.connection.start();
	}

	public String getName() {
		return name;
	}

	public String in() throws Exception {
		return connection.in();
	}

	public void out(String content) throws Exception {
		connection.out(content);
	}

	public Integer getTeam() {
		return this.numTeam;
	}

	public TCPConnection getConnection() {
		return connection;
	}

	public void close() {
		if (this.connection != null && this.connection.isAlive()) {
			this.connection.close();
			while (this.connection.isAlive()) {
				;
			}
		}
		this.connection = null;
	}

}
