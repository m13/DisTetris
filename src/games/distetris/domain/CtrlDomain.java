package games.distetris.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;


public class CtrlDomain {
	
	// controllers
	private static CtrlDomain INSTANCE = null;
	private CtrlNet NET = null;
	private CtrlGame GAME = null;
	
	private Listener listener;
	private Handler handler;
	
	
	// dynamic configuration (player)
	private Integer player = 0;
	private Integer team = 0;
	private Integer round = 0;
	private boolean myTurn = false;
	private Integer myTurns = 0;
	
	// dynamic configuration (server)
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
	
	public static CtrlDomain getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CtrlDomain();
			INSTANCE.NET = CtrlNet.getInstance();
			INSTANCE.GAME = CtrlGame.getInstance();
		}
		return INSTANCE;
	}
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	public void startNet() {
		NET.serverTCPStart(numPlayers, numTeams, numTurns);
		NET.serverTCPConnect("10.0.2.2", handler);
	}
	
	public int[][] getBoard(){
		return GAME.getBoard();
	}
	

	private void parserController(String str) {
		Log.d("PARSER", str);
		String[] actionContent = str.split(" ", 2);
		String[] args = actionContent[1].split(",");

		if (actionContent[0].equals("WAITING"))
		{
			// 1: assigned idPlayer
			// 2: number of teams
			// 3: num of turns
			player = (player == -1) ? player : Integer.valueOf(args[0]);
			team = GAME.windowChoiceTeam(Integer.valueOf(args[1]));
			myTurns = Integer.valueOf(args[2]);
			NET.sendSignal("JOIN " + String.valueOf(player) + "," + String.valueOf(team));
		}
		else if (actionContent[0].equals("JOIN"))
		{
			// 1: idPlayer == element position @ vector connections
			// 2: chosen team
			freeSlots = NET.registerPlayer(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
			if (freeSlots==0) {
				NET.sendSignals("START " + serialize(GAME.getBoard()));
				NET.nextPlayer();
			}
		}
		else if (actionContent[0].equals("START"))
		{
			// 1: serialized Board
			round++;
			GAME.setBoard(unserialize(actionContent[1]));
			Log.d("START", "");
		}
		else if (actionContent[0].equals("CONTINUE"))
		{
			// NULL
			String result = "DO";
			myTurn = true;
			for (int i = myTurns; i>0; i--) {
				do {
					result = GAME.playPiece();
					NET.sendSignal("PING " + serialize(GAME.getPiece()));
					
				} while (result.equals("DO"));
			}
			
			// testing!
			if (round>3) result = "END";
			
			myTurn = false;
			NET.sendSignal("FINISHED " + result);
		}
		else if (actionContent[0].equals("PING"))
		{
			// 1: serialized Piece
			NET.sendSignals("PONG "+actionContent[1]);
		}
		else if (actionContent[0].equals("PONG"))
		{
			// 1: serialized Piece
			if (!myTurn) {
				GAME.setPiece(unserialize(actionContent[1]));
			}
		}
		else if (actionContent[0].equals("FINISHED"))
		{
			// 1: String result
			// 2: (if error) String error
			if (args[0].equals("END")) {
				NET.sendSignals("END " + serialize(GAME.getBoard()));
			} else if (args[0].equals("NEXT")) {
				GAME.setPiece(args[0]);
				NET.sendSignals("START " + serialize(GAME.getBoard()));
				NET.nextPlayer();
			} else if (args[0].equals("ERROR")) {
				NET.sendSignals("ERROR "+args[1]);
			} else {
				// nothing
			}
		}
		else if (actionContent[0].equals("END"))
		{
			// 1: serialized Board
			GAME.setBoard(unserialize(actionContent[1]));
			GAME.saveScore();
		}
		else if (actionContent[0].equals("ERROR"))
		{
			// 1: String error
			GAME.showError(actionContent[1]);
		}
	}
	
	
	// move?
	private String serialize(Object object) {
		byte[] result = null;
		
		try {
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream (bs);
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

	public void serverUDPStart(Handler handler) {
		NET.serverUDPStart(handler);
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

}
