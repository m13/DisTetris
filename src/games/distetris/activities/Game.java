package games.distetris.activities;

import games.distetris.R;
import android.app.Activity;
import android.os.Bundle;

public class Game extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
    }
}
