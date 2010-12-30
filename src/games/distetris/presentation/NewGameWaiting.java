package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.WaitingRoom;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

public class NewGameWaiting extends Activity {
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Bundle b = msg.getData();
			String type = b.getString("type");

			if (type.equals("WAITING_ROOM")) {
				updateConnectedClients(b);
			} else if (type.equals("SHUTDOWN")) {
				Toast.makeText(getBaseContext(), "The server was closed", Toast.LENGTH_SHORT).show();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.newgamewaiting);

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

	private void updateConnectedClients(Bundle b) {

		WaitingRoom room = (WaitingRoom) b.getSerializable("room");

		String str = "";
		str += "Name: " + room.name + "\n";
		str += "Number of teams: " + room.numTeams + "\n";
		str += "Number of turns: " + room.numTurns + "\n";
		str += "\n";
		str += "Players:\n";

		for (int i = 0; i < room.players.size(); i++) {
			str += "[" + room.players.get(i).team + "] " + room.players.get(i).name + "\n";
		}

		TextView tv = ((TextView) findViewById(R.id.TextView01));
		tv.setText(str);

	}

}
