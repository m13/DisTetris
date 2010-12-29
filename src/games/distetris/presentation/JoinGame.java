package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JoinGame extends Activity {

	private LinearLayout ll;
	private int color = 0x0F000000;
	
	private Handler udpHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			// This function will be called several times in case there are multiple server
			// The only information returned is the NAME, IP and PORT of the found server
			// Must update the UI accordingly to allow the user choose a server
			View child = getLayoutInflater().inflate(R.layout.row_join, null);
			child.setTag( (Bundle) msg.getData() );

			TextView tv;
			tv = ((TextView) child.findViewById(R.id.Name));
			tv.setText( msg.getData().getString("NAME") );
			tv.setBackgroundColor(color);
			tv = ((TextView) child.findViewById(R.id.Ip));
			tv.setText( msg.getData().getString("IP") );
			tv = ((TextView) child.findViewById(R.id.Port));
			tv.setText( String.valueOf(msg.getData().getInt("PORT")) );

			child.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent i = new Intent();
					i.setClass(view.getContext(), JoinGameWaiting.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtras( (Bundle)view.getTag() );
					startActivity(i);
					finish();
				}
			});
			
			ll.addView( child );
		}
	};


	/**
	 * Reference: http://developer.android.com/images/activity_lifecycle.png
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.joingame);
		
		ll = ((LinearLayout) findViewById(R.id.Root));

		Button button;
		button = (Button) findViewById(R.id.Refresh);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ll.removeAllViews();
				CtrlDomain.getInstance().serverUDPFind(udpHandler);
			}
		});
		
		button = (Button) findViewById(R.id.Back);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * Reference: http://developer.android.com/images/activity_lifecycle.png
	 */
	@Override
	protected void onStart() {
		super.onStart();
		ll.removeAllViews();
		CtrlDomain.getInstance().serverUDPFind(udpHandler);
	}

	/**
	 * Reference: http://developer.android.com/images/activity_lifecycle.png
	 */
	@Override
	protected void onStop() {
		super.onStop();
		CtrlDomain.getInstance().serverUDPStop();
	}
}
