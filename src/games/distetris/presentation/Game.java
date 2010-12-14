package games.distetris.presentation;

import games.distetris.domain.L;
import android.app.Activity;
import android.os.Bundle;

public class Game extends Activity {
	GameView v;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		L.d("Start");

        v = new GameView(getBaseContext());
        setContentView(v);

		L.d("End");
    }
}
