package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.L;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewGame extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		L.d("Start");

        setContentView(R.layout.newgame);
        
        final Button button = (Button) findViewById(R.id.Create);
        final Button cancel = (Button) findViewById(R.id.Back);
        
        Bundle b = CtrlDomain.getInstance().getConfCreate();
        
		final EditText textNameServer = (EditText) findViewById(R.id.NewGameServername);
		textNameServer.setText(b.getString("servername"));
		final EditText textNumTeams = (EditText) findViewById(R.id.NewGameNumTeams);
		textNumTeams.setText(b.getString("numteams"));
		final EditText textNumTurns = (EditText) findViewById(R.id.NewGameNumTurns);
		textNumTurns.setText(b.getString("numturns"));

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Bundle b = new Bundle();
				b.putString("servername", textNameServer.getText().toString());
				b.putString("numteams", textNumTeams.getText().toString());
				b.putString("numturns", textNumTurns.getText().toString());
				CtrlDomain.getInstance().setConfCreate(b);

                Intent i = new Intent();
				i.setClass(v.getContext(), NewGameWaiting.class);
    			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			startActivity(i);
				finish();
            }
        });
        
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });

		L.d("End");
    }
}
