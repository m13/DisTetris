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
        
        final Button button = (Button) findViewById(R.id.NewGameButton01);
        final Button cancel = (Button) findViewById(R.id.NewGameButton02);
        
		final EditText textNameServer = (EditText) findViewById(R.id.NewGameEditText01);
		final EditText textNumTeams = (EditText) findViewById(R.id.NewGameEditText02);
		final EditText textNumPlayers = (EditText) findViewById(R.id.NewGameEditText03);
		final EditText textNumTurns = (EditText) findViewById(R.id.NewGameEditText04);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

				String nameServer = textNameServer.getText().toString();
				Integer numTeams = Integer.parseInt(textNumTeams.getText().toString());
				Integer numPlayers = Integer.parseInt(textNumPlayers.getText().toString());
				Integer numTurns = Integer.parseInt(textNumTurns.getText().toString());

				CtrlDomain.getInstance().serverConfigure(nameServer, numTeams, numPlayers, numTurns);

                Intent i = new Intent();
				i.setClass(v.getContext(), NewGameWaiting.class);
    			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			startActivity(i);
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
