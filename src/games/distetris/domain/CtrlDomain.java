package games.distetris.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;


public class CtrlDomain {
	
	// controllers
	private static CtrlDomain INSTANCE = null;
	private CtrlNet NET;
	private CtrlGame GAME;
	
	private Listener listener;
	private Handler handler;
	
	
	// dynamic configuration (player)
	private Integer player = 0;
	private Integer team = 0;
	private Integer round = 0;
	
	// dynamic configuration (server)
	private Integer numPlayers = 1;
	private Integer freeSlots = 1;
	private Integer numTeams = 2;
	
	
	CtrlDomain () {
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
			INSTANCE.NET = new CtrlNet();
			INSTANCE.GAME = new CtrlGame();
		}
		return INSTANCE;
	}
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	public void startNet() {
		NET.createServer(numPlayers, numTeams);
		NET.connectServer("10.0.2.2", handler);
	}
	
	

	private void parserController(String str) {
		Log.d("", str);
		String[] actionContent = str.split(" ", 2);
		String[] args = actionContent[1].split(",");

		if (actionContent[0].equals("WAITING"))
		{
			// 1: assigned idPlayer
			// 2: number of teams
			player = (player == -1) ? player : Integer.valueOf(args[0]);
			team = GAME.windowChoiceTeam(Integer.valueOf(args[1]));
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
			do {
				result = GAME.playPiece();
				NET.sendSignal("PING " + serialize(GAME.getPiece()));
				
			} while (result.equals("DO"));
			
			// testing!
			if (round>3) result = "END";
			
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
			GAME.setPiece(unserialize(actionContent[1]));
		}
		else if (actionContent[0].equals("FINISHED"))
		{
			// 1: String result
			if (args[0].equals("END")) {
				NET.sendSignals("END " + serialize(GAME.getBoard()));
			} else {
				GAME.setPiece(args[0]);
				NET.sendSignals("START " + serialize(GAME.getBoard()));
				NET.nextPlayer();
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
}
