package games.distetris.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Distetris extends Activity {
	
    // Debugging
    private static final String TAG = "DISTETRIS";
    private static final boolean DEBUG = true;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG, "onPause");
        setContentView(R.layout.main);
        setButtons();
    }

	private void setButtons() {

        int[] allIdButtons = new int[] { R.id.mainButton01,
        		R.id.mainButton02, R.id.mainButton03, R.id.mainButton04 };
		
        for (int idButton : allIdButtons) {
	        Button button = (Button) findViewById(idButton);
	        button.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	Intent i = new Intent();
	                switch(v.getId()) {
		                case R.id.mainButton01:
		                	i.setClass(v.getContext(), NewGame.class);
		                	break;
		                case R.id.mainButton02:
		                	i.setClass(v.getContext(), JoinGame.class);
		                	break;
		                case R.id.mainButton03:
		                	i.setClass(v.getContext(), Statistics.class);
		                	break;
		                case R.id.mainButton04:
		                	i.setClass(v.getContext(), Configure.class);
		                	break;
	                }
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);
	            }
	        });
        }
       
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}
    
	
	
	
}