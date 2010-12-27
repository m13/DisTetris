package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.L;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Configure extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		L.d("Start");

        setContentView(R.layout.configure);
        Button b;
        
        b = (Button) findViewById(R.id.Back);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });
        
        b = (Button) findViewById(R.id.Save);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	EditText ev = (EditText) findViewById(R.id.PlayerName);
            	CtrlDomain.getInstance().setPlayerName(ev.getText().toString());
            	finish();
            }
        });
        
        EditText et = (EditText) findViewById(R.id.PlayerName);
        et.setText(CtrlDomain.getInstance().getPlayerName());
        
		L.d("End");
    }
}
