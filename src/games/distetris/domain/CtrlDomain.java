package games.distetris.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;

public class CtrlDomain {

	public static final int MODE_SERVER = 1;
	public static final int MODE_CLIENT = 2;

	// controllers
	private static CtrlDomain INSTANCE = null;
	private CtrlNet NET = null;
	private CtrlGame GAME = null;

	private Handler handler;
	private Handler handlerUI;

	// dynamic configuration
	private int mode = 0;

	// dynamic configuration (player)
	private Integer player = 0;
	private Integer team = 0;
	private Integer round = 0;
	private boolean myTurn = false;
	private Integer myTurns = 0;

	// dynamic configuration (server)
	private String name;
	private Integer numPlayers = 1;
	private Integer freeSlots = 1;
	private Integer numTeams = 2;
	private Integer numTurns = 2;

	private CtrlDomain() {
		L.d("Created");

		this.handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.getData().containsKey("MSG")) {
					parserController(msg.getData().get("MSG").toString());
				}
			}
		};
	}

	public static CtrlDomain getInstance(Context ctx) {
		if (INSTANCE == null) {
			INSTANCE = new CtrlDomain();
			INSTANCE.NET = CtrlNet.getInstance();
			INSTANCE.GAME = CtrlGame.getInstance(ctx);
		}
		return INSTANCE;
	}
	
	public static CtrlDomain getInstance() {
		return INSTANCE;
	}

	public void startNet() {
		NET.serverTCPStart(numTeams, numPlayers, numTurns, handler);
		NET.serverTCPConnect("10.0.2.2", CtrlNet.PORT, handler);
	}

	public int[][] getBoard() {
		return GAME.getBoard();
	}

	private void parserController(String str) {
		L.d(str);
		String[] actionContent = str.split(" ", 2);
		String[] args = actionContent[1].split(",");

		if (actionContent[0].equals("WAITING")) {
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
			freeSlots = NET.registerPlayer(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
			if (freeSlots == 0) {
				NET.sendSignals("START " + serialize(GAME.getBoard()));
				NET.nextPlayer();
			}
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
			GAME.saveScore(numPlayers==numTeams);
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

		return Base64.encodeToString(result, Base64.NO_WRAP);
	}

	// move?
	private Object unserialize(String str) {
		Object object = null;
		byte[] bytes = Base64.decode(str, Base64.NO_WRAP);

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

	public String[] serverTCPGetConnectedUsers() {
		return NET.serverTCPGetConnectedUsers();
	}

	public String getPlayerName() {
		return GAME.getPlayerName();
	}
	
	public void setPlayerName(String name) {
		GAME.setPlayerName(name);
	}

	public void serverConfigure(String name, int numTeams, int numPlayers, int numTurns) {
		this.name = name;
		this.numTeams = numTeams;
		this.numPlayers = numPlayers;
		this.numTurns = numTurns;
	}

	public String getName() {
		return this.name;
	}

	public Integer getNumTeams() {
		return this.numTeams;
	}

	public Integer getNumPlayers() {
		return this.numPlayers;
	}

	public Integer getNumTurns() {
		return this.numTurns;
	}

	public void serverTCPStart() {
		this.mode = MODE_SERVER;
		NET.serverTCPStart(numTeams, numPlayers, numTurns, handler);
	}

	public void serverTCPStop() {
		this.mode = 0;
		NET.serverTCPStop();
	}

	public void serverTCPConnect(String serverIP, int serverPort) {
		this.mode = MODE_CLIENT;
		NET.serverTCPConnect(serverIP, serverPort, handler);
	}

	public void setHandlerUI(Handler hand) {
		this.handlerUI = hand;
	}

	public void updatedPlayers() {
		handlerUI.sendEmptyMessage(0);
	}
	
	
	//GAME HOOKS
	public void cleanBoard(){
		this.GAME.cleanBoard();
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
	
	public boolean currentPieceOffsetCollision(int offset){
		return this.GAME.currentPieceOffsetCollision(offset);
	}
	
	public void addCurrentPieceToBoard(){
		this.GAME.addCurrentPieceToBoard();
	}

}
