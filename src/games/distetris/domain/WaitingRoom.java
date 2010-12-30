package games.distetris.domain;

import java.io.Serializable;
import java.util.Vector;

public class WaitingRoom implements Serializable {

	private static final long serialVersionUID = -1265117283783770369L;

	public String name;
	public Integer numTeams;
	public Integer numTurns;
	public Vector<WaitingRoomPlayer> players;

	// Dynamic configuration about the player
	// MUST be blank when sending from the server
	// It will be populated when received by the client before sending it to the UI 
	public Integer currentPlayerID;
	public Integer currentTeamID;

	public WaitingRoom() {

		CtrlDomain DOM = CtrlDomain.getInstance();
		CtrlNet NET = CtrlNet.getInstance();

		// Parse the information from the controllers

		this.name = DOM.getServerName();
		this.numTeams = DOM.getServerNumTeams();
		this.numTurns = DOM.getServerNumTurns();

		// And the players connected

		this.players = new Vector<WaitingRoomPlayer>(NET.serverTCPGetConnectedPlayersNum());

		Vector<String> players_name = NET.serverTCPGetConnectedPlayersName();
		Vector<Integer> players_team = NET.serverTCPGetConnectedPlayersTeam();

		for (int i = 0; i < NET.serverTCPGetConnectedPlayersNum(); i++) {
			WaitingRoomPlayer new_player = new WaitingRoomPlayer(players_name.get(i), players_team.get(i));
			this.players.add(new_player);
		}

	}

	public class WaitingRoomPlayer implements Serializable {

		private static final long serialVersionUID = 8440232224311331235L;

		public String name;
		public Integer team;

		public WaitingRoomPlayer(String name, Integer team) {
			super();
			this.name = name;
			this.team = team;
		}

	}

}
