package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.L;
import games.distetris.domain.WaitingRoom;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
		L.d("Start");

		setContentView(R.layout.gamewaiting);

		Button b;
		b = (Button) findViewById(R.id.Start);
		b.setVisibility(View.VISIBLE);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startGameButton();
			}
		});
		
		b = (Button) findViewById(R.id.Back);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				CtrlDomain.getInstance().serverTCPDisconnectClients();
				finish();
			}
		});

		L.d("End");
	}

	@Override
	protected void onStart() {
		super.onStart();
		L.d("Start");

		CtrlDomain.getInstance().setHandlerUI(handler);

		try {
			CtrlDomain.getInstance().serverTCPStart();
			CtrlDomain.getInstance().serverUDPStart();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Couldn't create the server", Toast.LENGTH_SHORT).show();
			finish();
		}

		L.d("End");

	}

	@Override
	protected void onStop() {
		super.onStop();
		L.d("Start");

		CtrlDomain.getInstance().serverUDPStop();
		CtrlDomain.getInstance().serverTCPStop();

		L.d("End");
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
	 * @param b Bundle containing the WaitingRoom class
	 */
	private void updateWaitingRoom(Bundle b) {

		this.room = (WaitingRoom) b.getSerializable("room");

		TextView tv;
		tv = (TextView) findViewById(R.id.WaitingServername);
		tv.setText(String.valueOf(room.name));
		tv = (TextView) findViewById(R.id.WaitingNumTeams);
		tv.setText(String.valueOf(room.numTeams));
		tv = (TextView) findViewById(R.id.WaitingNumTurns);
		tv.setText(String.valueOf(room.numTurns));

		tv = (TextView) findViewById(R.id.PlayerID);
		tv.setText(String.valueOf(room.currentPlayerID));
		tv = (TextView) findViewById(R.id.TeamID);
		tv.setText(String.valueOf(room.currentTeamID));
	
		LinearLayout ll = (LinearLayout) findViewById(R.id.Players);
		ll.removeAllViews();
		
		for (int i = 0; i < room.players.size(); i++) {
			View child = getLayoutInflater().inflate(R.layout.row_player, null);

			tv = (TextView) child.findViewById(R.id.Team);
			tv.setText(String.valueOf(room.players.get(i).team));
			tv = (TextView) child.findViewById(R.id.Player);
			tv.setText(String.valueOf(room.players.get(i).name));
			ll.addView(child);
		}
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
