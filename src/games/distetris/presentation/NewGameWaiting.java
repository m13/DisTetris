package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class NewGameWaiting extends Activity {
	
	private Handler udpHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
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

		CtrlDomain.getInstance().serverUDPStart(udpHandler);

	}

	@Override
	protected void onStop() {
		super.onStop();

		CtrlDomain.getInstance().serverUDPStop();

	}

}
