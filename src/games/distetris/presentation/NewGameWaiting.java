package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

public class NewGameWaiting extends Activity {
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			updateConnectedClients(msg);
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
		} catch (IOException e) {
			Toast.makeText(getBaseContext(), "Couldn't create the server", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();

		CtrlDomain.getInstance().serverUDPStop();
		CtrlDomain.getInstance().serverTCPStop();

	}

	private void updateConnectedClients(Message msg) {

		// Each string contains the name of the player and the "id" of the team separated by |
		// For example user viciado playing in team 3 would result in the string viciado|3

		Bundle b = msg.getData();
		String[] users = b.getStringArray("players");

		String str = "";
		for (int i = 0; i < users.length; i++) {

			String name = users[i].split("\\|")[0];
			Integer team_id = Integer.parseInt(users[i].split("\\|")[1]);
			str += "[" + team_id + "] " + name + "\n";

		}
		TextView tv = ((TextView) findViewById(R.id.TextView01));
		tv.setText("Usuarios conectados:\n" + str);
	}

}
