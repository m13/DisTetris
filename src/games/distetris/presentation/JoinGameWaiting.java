package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.WaitingRoom;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

public class JoinGameWaiting extends Activity {

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Bundle b = msg.getData();
			String type = b.getString("type");

			if (type.equals("WAITINGROOM")) {
				updateWaitingRoom(b);
			} else if (type.equals("STARTGAME")) {
				startGame();
			} else if (type.equals("SHUTDOWN")) {
				Toast.makeText(getBaseContext(), "The server closed the connection", Toast.LENGTH_SHORT).show();
				finish();
			}

		}
	};

	/**
	 * Reference: http://developer.android.com/images/activity_lifecycle.png
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.joingamewaiting);

		Bundle bundle = this.getIntent().getExtras();
		String serverName = bundle.getString("NAME");
		String serverIP = bundle.getString("IP");
		int serverPort = bundle.getInt("PORT");


		
		try {
			CtrlDomain.getInstance().setHandlerUI(handler);
			CtrlDomain.getInstance().serverTCPConnect(serverIP, serverPort);
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Couldn't connect to the server", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		CtrlDomain.getInstance().serverTCPDisconnect();

	}

	/**
	 * Parse a newly received WaitingRoom class
	 * 
	 * @param b
	 *            Bundle containing the WaitingRoom class
	 */
	protected void updateWaitingRoom(Bundle b) {

		WaitingRoom room = (WaitingRoom) b.getSerializable("room");

		String str = "";
		str += "Name: " + room.name + "\n";
		str += "Number of teams: " + room.numTeams + "\n";
		str += "Number of turns: " + room.numTurns + "\n";
		str += "\n";
		str += "PlayerID: " + room.currentPlayerID + "\n";
		str += "TeamID: " + room.currentTeamID + "\n";
		str += "\n";
		str += "Players:\n";

		for (int i = 0; i < room.players.size(); i++) {
			str += "[" + room.players.get(i).team + "] " + room.players.get(i).name + "\n";
		}

		TextView tv = ((TextView) findViewById(R.id.TextView01));
		tv.setText(str);
	}

	/**
	 * Change the view to Game because the server started the game
	 */
	protected void startGame() {
		Intent i = new Intent();
		i.setClass(getBaseContext(), Game.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
	}
}
