package games.distetris.presentation;

import android.app.Activity;
import android.os.Bundle;

public class Game extends Activity {
	GameView v;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = new GameView(getBaseContext());
        setContentView(v);
    }
}
