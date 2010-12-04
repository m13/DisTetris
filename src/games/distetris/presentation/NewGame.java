package games.distetris.presentation;

import games.distetris.domain.L;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewGame extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		L.d("Start");

        setContentView(R.layout.newgame);
        
        final Button button = (Button) findViewById(R.id.newgameButton01);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();
				i.setClass(v.getContext(), NewGameWaiting.class);
    			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			startActivity(i);
            }
        });

		L.d("End");
    }
}
