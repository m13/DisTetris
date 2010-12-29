package games.distetris.domain;

import games.distetris.storage.DbHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class CtrlDomain {

	public static final int MODE_SERVER = 1;
	public static final int MODE_CLIENT = 2;

	// controllers
	private static CtrlDomain INSTANCE = null;
	private CtrlNet NET = null;
	private CtrlGame GAME = null;

	private Handler handlerDomain;
	private Handler handlerUI;

	// dynamic configuration
	private int mode = 0;

	// dynamic configuration (player)
	private Integer player = 0;
	private Integer team = 0;
	private Integer round = 0; //Nivel a partir de ronda
	private boolean myTurn = false; 
	private Integer myTurns = 0;

	// dynamic configuration (server)
	private String serverName;
	private Integer serverNumTeams;
	private Integer serverNumTurns;

	private CtrlDomain() {
		L.d("Created");

		this.handlerDomain = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.getData().containsKey("MSG")) {
					parserController(msg.getData().get("MSG").toString());
				}
			}
		};
	}

	public static CtrlDomain getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CtrlDomain();
			INSTANCE.NET = CtrlNet.getInstance();
			INSTANCE.GAME = CtrlGame.getInstance();
		}
		return INSTANCE;
	}

	public void setDbHelper(DbHelper dbHelper) {
		CtrlGame.getInstance().setDbHelper(dbHelper);
	}

	public int[][] getBoard() {
		return GAME.getBoard();
	}

	private void parserController(String str) {
		L.d(str);
		String[] actionContent = str.split(" ", 2);
		String[] args = actionContent[1].split(",");

		if (actionContent[0].equals("WAITING_ROOM")) {

			String[] players_array = (String[]) unserialize(args[0]);

			// Update the client UI
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString("type", "WAITING_ROOM");
			b.putStringArray("players", players_array);
			msg.setData(b);
			handlerUI.sendMessage(msg);

		} else if (actionContent[0].equals("WAITING")) {
			// 1: assigned idPlayer
			// 2: number of teams
			// 3: num of turns
			player = (player == -1) ? player : Integer.valueOf(args[0]);
			team = GAME.windowChoiceTeam(Integer.valueOf(args[1]));
			myTurns = Integer.valueOf(args[2]);
			NET.sendSignal("JOIN " + String.valueOf(player) + "," + String.valueOf(team));
		} else if (actionContent[0].equals("JOIN")) {
			// 1: idPlayer == element position @ vector connections
			// 2: chosen team
			NET.registerPlayer(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
			// TODO: fix it?
			/*
			if (freeSlots == 0) {
				NET.sendSignals("START " + serialize(GAME.getBoard()));
				NET.nextPlayer();
			}
			*/
		} else if (actionContent[0].equals("START")) {
			// 1: serialized Board
			round++;
			GAME.setBoard(unserialize(actionContent[1]));
			L.d("START");
		} else if (actionContent[0].equals("CONTINUE")) {
			// NULL
			String result = "DO";
			myTurn = true;
			for (int i = myTurns; i > 0; i--) {
				do {
					result = GAME.playPiece();
					NET.sendSignal("PING " + serialize(GAME.getPiece()));

				} while (result.equals("DO"));
			}

			// testing!
			if (round > 3)
				result = "END";

			myTurn = false;
			NET.sendSignal("FINISHED " + result);
		} else if (actionContent[0].equals("PING")) {
			// 1: serialized Piece
			NET.sendSignals("PONG " + actionContent[1]);
		} else if (actionContent[0].equals("PONG")) {
			// 1: serialized Piece
			if (!myTurn) {
				GAME.setPiece(unserialize(actionContent[1]));
			}
		} else if (actionContent[0].equals("FINISHED")) {
			// 1: String result
			// 2: (if error) String error
			if (args[0].equals("END")) {
				NET.sendSignals("END " + serialize(GAME.getBoard()));
			} else if (args[0].equals("NEXT")) {
				GAME.setPiece(args[0]);
				NET.sendSignals("START " + serialize(GAME.getBoard()));
				NET.nextPlayer();
			} else if (args[0].equals("ERROR")) {
				NET.sendSignals("ERROR " + args[1]);
			} else {
				// nothing
			}
		} else if (actionContent[0].equals("END")) {
			// 1: serialized Board
			GAME.setBoard(unserialize(actionContent[1]));
			// TODO: fix it?
			// GAME.saveScore(numPlayers==numTeams);
		} else if (actionContent[0].equals("ERROR")) {
			// 1: String error
			GAME.showError(actionContent[1]);
		}
	}
	
	public Cursor getScoreInd() {
		return GAME.getScoreInd();
	}
	
	public Cursor getScoreTeam() {
		return GAME.getScoreTeam();
	}

	// move?
	private String serialize(Object object) {
		byte[] result = null;

		try {
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bs);
			os.writeObject(object);
			os.close();
			result = bs.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return new String();
		}

		return games.distetris.domain.Base64.encodeToString(result, games.distetris.domain.Base64.NO_WRAP);
	}

	// move?
	private Object unserialize(String str) {
		Object object = null;
		byte[] bytes = games.distetris.domain.Base64.decode(str, games.distetris.domain.Base64.NO_WRAP);

		try {
			ByteArrayInputStream bs = new ByteArrayInputStream(bytes);
			ObjectInputStream is = new ObjectInputStream(bs);
			object = (Object) is.readObject();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			return new String();
		}
		return object;
	}

	public void serverUDPStart() {
		NET.serverUDPStart();
	}

	public void serverUDPFind(Handler handler) {
		NET.serverUDPFind(handler);
	}

	public void serverUDPStop() {
		NET.serverUDPStop();
	}

	public void setWifiManager(WifiManager systemService) {
		NET.setWifiManager(systemService);
	}

	public String getPlayerName() {
		return GAME.getPlayerName();
	}
	
	public void setPlayerName(String name) {
		GAME.setPlayerName(name);
	}

	public void serverConfigure(String name, int numTeams, int numTurns) {
		this.serverName = name;
		this.serverNumTeams = numTeams;
		this.serverNumTurns = numTurns;
	}

	public String getServerName() {
		return this.serverName;
	}

	public Integer getServerNumTeams() {
		return this.serverNumTeams;
	}

	public Integer getServerNumTurns() {
		return this.serverNumTurns;
	}

	public void serverTCPStart() throws IOException {
		this.mode = MODE_SERVER;
		NET.serverTCPStart(serverNumTeams, serverNumTurns);
	}

	public void serverTCPStop() {
		this.mode = 0;
		NET.serverTCPStop();
	}

	public void serverTCPConnect(String serverIP, int serverPort) throws IOException {
		this.mode = MODE_CLIENT;
		NET.serverTCPConnect(serverIP, serverPort);
	}

	public void setHandlerUI(Handler hand) {
		this.handlerUI = hand;
	}

	public void updatedPlayers() {

		//new_player.out("WAITING " + (players.size()) + "," + (numTeams - 1) + "," + numTurns);

		String[] players_array = NET.serverTCPGetConnectedPlayersInfo();

		// Send the info to all the connected clients
		NET.sendSignals("WAITING_ROOM " + serialize(players_array));
	}
	
	
	//GAME HOOKS
	public ArrayList<Integer> cleanBoard(){
		return this.GAME.cleanBoard();
	}
	
	public void setNewRandomPiece(){
		this.GAME.setNewRandomPiece();
		
	}
	
	public Piece getCurrentPiece(){
		if(this.GAME.getCurrentPiece() == null){
			this.setNewRandomPiece();
		}
		return this.GAME.getCurrentPiece();
	}
	
	public void gameStep(){
		this.GAME.gameStep();
	}
	
	public boolean currentPieceCollision(){
		return this.GAME.currentPieceCollision();
	}
	
	public boolean nextStepPieceCollision(){
		return this.GAME.nextStepPieceCollision();
	}
	
	public void currentPieceRotateLeft(){
		this.GAME.rotateLeft();
	}
	
	public void currentPieceRotateRight(){
		this.GAME.rotateRight();
	}
	
	public boolean currentPieceOffsetCollision(int offset){
		return this.GAME.currentPieceOffsetCollision(offset);
	}
	
	public void addCurrentPieceToBoard(){
		this.GAME.addCurrentPieceToBoard();
	}
	
	public Piece getNextPiece(){
		return this.GAME.getNextPiece();
	}
	
	public boolean isGameOver(){
		return this.GAME.isGameOver();
	}

	public Handler getHandlerDomain() {
		return this.handlerDomain;
	}

}
