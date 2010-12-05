package games.distetris.presentation;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class JoinGameWaiting extends Activity {

	private Handler udpHandler = new Handler() {
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
		String serverIp = bundle.getString("IP");
		int serverPort = bundle.getInt("NAME");
		Toast.makeText(getBaseContext(), "Received " + serverName + " " + serverIp + " " + serverPort, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

}
