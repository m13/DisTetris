package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.WaitingRoom;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewGameWaiting extends Activity {
	
	private WaitingRoom room;

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
				Toast.makeText(getBaseContext(), "The server was closed", Toast.LENGTH_SHORT).show();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.newgamewaiting);

		Button b = (Button) findViewById(R.id.Start);

		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startGameButton();
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();

		CtrlDomain.getInstance().setHandlerUI(handler);

		try {
			CtrlDomain.getInstance().serverTCPStart();
			CtrlDomain.getInstance().serverUDPStart();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Couldn't create the server", Toast.LENGTH_SHORT).show();
			finish();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();

		CtrlDomain.getInstance().serverUDPStop();
		CtrlDomain.getInstance().serverTCPStop();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			CtrlDomain.getInstance().serverTCPDisconnectClients();
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Parse a newly received WaitingRoom class
	 * 
	 * @param b
	 *            Bundle containing the WaitingRoom class
	 */
	private void updateWaitingRoom(Bundle b) {

		this.room = (WaitingRoom) b.getSerializable("room");

		String str = "";
		str += "Name: " + this.room.name + "\n";
		str += "Number of teams: " + this.room.numTeams + "\n";
		str += "Number of turns: " + this.room.numTurns + "\n";
		str += "\n";
		str += "PlayerID: " + this.room.currentPlayerID + "\n";
		str += "TeamID: " + this.room.currentTeamID + "\n";
		str += "\n";
		str += "Players:\n";

		for (int i = 0; i < this.room.players.size(); i++) {
			str += "[" + this.room.players.get(i).team + "] " + this.room.players.get(i).name + "\n";
		}

		TextView tv = ((TextView) findViewById(R.id.TextView01));
		tv.setText(str);

	}

	/**
	 * Called by the button listener. The server is going to send the signal
	 * about the start of the game.
	 */
	private void startGameButton() {

		if (this.room != null && this.room.players.size() > 1) {
			CtrlDomain.getInstance().startGame();
		} else {
			Toast.makeText(getBaseContext(), "Minimum 2 players required to play!", Toast.LENGTH_SHORT).show();
		}
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
