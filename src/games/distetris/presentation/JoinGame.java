package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class JoinGame extends Activity {
	
	private Handler udpHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			// This function will be called several times in case there are multiple server
			// The only information returned is the NAME, IP and PORT of the found server
			// Must update the UI accordingly to allow the user choose a server
			addLogText(msg.getData().getString("NAME") + " " + msg.getData().getString("IP") + " " + msg.getData().getString("PORT"));
		}
	};

	/**
	 * Reference: http://developer.android.com/images/activity_lifecycle.png
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.joingame);

	}

	/**
	 * Reference: http://developer.android.com/images/activity_lifecycle.png
	 */
	@Override
	protected void onStart() {
		super.onStart();

		CtrlDomain.getInstance().serverUDPFind(udpHandler);

	}

	/**
	 * Reference: http://developer.android.com/images/activity_lifecycle.png
	 */
	@Override
	protected void onStop() {
		super.onStop();

		CtrlDomain.getInstance().serverUDPStop();

	}

	public void addLogText(String str) {
		TextView tv = ((TextView) findViewById(R.id.TextView01));
		tv.setText(tv.getText() + "\n" + str);
	}

}
