package games.distetris.presentation;

import games.distetris.domain.L;
import android.app.Activity;
import android.os.Bundle;

public class Configure extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		L.d("Start");

        setContentView(R.layout.configure);

		L.d("End");
    }
}
