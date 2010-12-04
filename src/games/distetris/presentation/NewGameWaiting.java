package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.L;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NewGameWaiting extends Activity implements NewGameListener {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L.d("Start");

		setContentView(R.layout.newgamewaiting);

		L.d("ContentView setted");

		CtrlDomain.getInstance().createServerUDP(this);

		L.d("End");
	}

	@Override
	public void addEvent(String str) {
		TextView tv = ((TextView) findViewById(R.id.TextView01));
		tv.setText(tv.getText() + "\n" + str);
	}


}
