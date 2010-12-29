package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;

import java.io.IOException;

import android.app.Activity;
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

			if (type.equals("WAITING_ROOM")) {
				updateConnectedClients(b);
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
		Toast.makeText(getBaseContext(), "Received " + serverName + " " + serverIP + ":" + String.valueOf(serverPort), Toast.LENGTH_SHORT).show();

		CtrlDomain.getInstance().setHandlerUI(handler);
		try {
			CtrlDomain.getInstance().serverTCPConnect(serverIP, serverPort);
		} catch (IOException e) {
			Toast.makeText(getBaseContext(), "Couldn't connect to the server", Toast.LENGTH_SHORT).show();
		}
	}

	protected void updateConnectedClients(Bundle b) {

		// Each string contains the name of the player and the "id" of the team separated by |
		// For example user viciado playing in team 3 would result in the string viciado|3

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
