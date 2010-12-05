package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class JoinGame extends Activity {

	private ArrayAdapter<String> serverList;
	ListView lv;
	
	private Handler udpHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			// This function will be called several times in case there are multiple server
			// The only information returned is the NAME, IP and PORT of the found server
			// Must update the UI accordingly to allow the user choose a server
			String str = msg.getData().getString("NAME") + " | " + msg.getData().getString("IP") + " | " + msg.getData().getString("PORT");

			serverList.add(str);
		}
	};


	/**
	 * Reference: http://developer.android.com/images/activity_lifecycle.png
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.joingame);

		this.serverList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

		this.lv = ((ListView) findViewById(R.id.ListView01));
		this.lv.setAdapter(serverList);
		this.lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(view.getContext(), "Connecting to " + serverList.getItem(position).split(" \\| ")[1] + ":" + serverList.getItem(position).split(" \\| ")[2], Toast.LENGTH_SHORT).show();

				Bundle b = new Bundle();
				b.putString("NAME", serverList.getItem(position).split(" \\| ")[0]);
				b.putString("IP", serverList.getItem(position).split(" \\| ")[1]);
				b.putInt("PORT", Integer.parseInt(serverList.getItem(position).split(" \\| ")[2]));

				Intent i = new Intent();
				i.setClass(view.getContext(), JoinGameWaiting.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtras(b);
				startActivity(i);

			}

		});

		Button button = (Button) findViewById(R.id.Button01);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				serverList.clear();
				CtrlDomain.getInstance().serverUDPFind(udpHandler);
			}
		});

	}

	/**
	 * Reference: http://developer.android.com/images/activity_lifecycle.png
	 */
	@Override
	protected void onStart() {
		super.onStart();

		serverList.clear();
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
