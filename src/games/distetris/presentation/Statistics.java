package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.L;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Statistics extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		L.d("Start");

        setContentView(R.layout.statistics);
        Button b = (Button) findViewById(R.id.back);
        
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });

		L.d("End");
    }
    
	
	@Override
	protected void onResume() {
		super.onResume();
		L.d("onResume");
		
		Cursor cr = CtrlDomain.getInstance().getScoreInd();
		LinearLayout ll = (LinearLayout) findViewById(R.id.lli);
		addStats(cr, ll);
		
		cr = CtrlDomain.getInstance().getScoreTeam();
		ll = (LinearLayout) findViewById(R.id.llt);
		addStats(cr, ll);
	}
	
	private void addStats(Cursor cr, LinearLayout ll) {
		if (cr.moveToFirst()) {
			int color = 0;
			do {
				View child = getLayoutInflater().inflate(R.layout.row_stats, null);
				
				String name = cr.getString(cr.getColumnIndex("name"));
				Integer score = cr.getInt(cr.getColumnIndex("score"));
				Long date = cr.getLong(cr.getColumnIndex("date"));
				
				Calendar cal = Calendar.getInstance();
				cal.setTime( new Date(date) );
				String strDate = String.format("%02d:%02d %02d/%02d",
						cal.get(Calendar.HOUR_OF_DAY),
						cal.get(Calendar.MINUTE),
						cal.get(Calendar.DATE),
						cal.get(Calendar.MONTH));
				
				((TextView)child.findViewById(R.id.name)).setText(name);
				((TextView)child.findViewById(R.id.score)).setText(String.valueOf(score));
				((TextView)child.findViewById(R.id.date)).setText(strDate);
				
				color = (color==0) ? 1 : 0;
				child.setBackgroundColor(0xEEEEEEE * color);
				
				ll.addView(child);
			} while(cr.moveToNext());
		}
		cr.close();
	}
}
