package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.L;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Distetris extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L.d("Start");

		// System Services only can be get from an activity, so the instance is passed to be used later
		Context ctx = getBaseContext();
		CtrlDomain.getInstance(ctx).setWifiManager((WifiManager) getSystemService(Context.WIFI_SERVICE));

        setContentView(R.layout.main);
        setButtons();
		//CtrlDomain.getInstance().startNet();
		L.d("End");
    }

	private void setButtons() {

        int[] allIdButtons = new int[] { R.id.mainButton01,
        		R.id.mainButton02, R.id.mainButton03, R.id.mainButton04,R.id.mainButton05 };
		
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
		                case R.id.mainButton05:
		                	i.setClass(v.getContext(), Game.class);
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
		L.d("onStart");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		L.d("onRestart");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		L.d("onResume");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		L.d("onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		L.d("onStop");
	}
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		L.d("onDestroy");
	}
    
	
	
	
}