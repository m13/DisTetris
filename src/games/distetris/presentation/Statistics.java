package games.distetris.presentation;

import games.distetris.domain.L;
import android.app.Activity;
import android.os.Bundle;

public class Statistics extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		L.d("Start");

        setContentView(R.layout.statistics);

		L.d("End");
    }
}
