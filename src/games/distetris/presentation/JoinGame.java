package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.L;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class JoinGame extends Activity implements NewGameListener {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		L.d("Start");

		setContentView(R.layout.joingame);

		CtrlDomain.getInstance().findServersUDP(this);

		L.d("End");
    }

	@Override
	public void addEvent(String str) {
		TextView tv = ((TextView) findViewById(R.id.TextView01));
		tv.setText(tv.getText() + "\n" + str);
	}
}
