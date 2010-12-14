package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class NewGameWaiting extends Activity {
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			updateConnectedClients();
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

		CtrlDomain.getInstance().serverTCPStart();
		CtrlDomain.getInstance().serverUDPStart();

	}

	@Override
	protected void onStop() {
		super.onStop();

		CtrlDomain.getInstance().serverUDPStop();
		CtrlDomain.getInstance().serverTCPStop();

	}

	private void updateConnectedClients() {
		String[] users = CtrlDomain.getInstance().serverTCPGetConnectedUsers();
		String str = "";
		for (int i = 0; i < users.length; i++) {
			str += users[i] + "\n";
		}
		TextView tv = ((TextView) findViewById(R.id.TextView01));
		tv.setText("Usuarios conectados:\n" + str);
	}

}
