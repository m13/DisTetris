package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class JoinGameWaiting extends Activity {

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
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
		CtrlDomain.getInstance().serverTCPConnect(serverIP, serverPort);
	}
}
