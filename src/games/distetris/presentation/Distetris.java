package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.L;
import games.distetris.storage.DbHelper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class Distetris extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L.d("Start");

		// System Services only can be get from an activity, so the instance is passed to be used later
		CtrlDomain.getInstance().setWifiManager((WifiManager) getSystemService(Context.WIFI_SERVICE));
		CtrlDomain.getInstance().setDbHelper(new DbHelper(getBaseContext()));

        setContentView(R.layout.main);
        setButtons();
		//CtrlDomain.getInstance().startNet();
		L.d("End");
    }

    private void setButtons() {
        Gallery g = (Gallery) findViewById(R.id.gallery);
        g.setUnselectedAlpha(1f);
        g.setSpacing(1);
        g.setAdapter(new ImageAdapter(this));
        g.setSelection(1);

        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Intent i = new Intent();
            	i.setClass(v.getContext(), (Class<?>) v.getTag());
            	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
            }
        });
    }
    
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        private Integer[] mImageIds = {
                R.drawable.new_game,
                R.drawable.join_game,
                R.drawable.statistics,
                R.drawable.configure,
                R.drawable.test
        };

        private Class<?>[] mImageClass = {
        	NewGame.class, 
        	JoinGame.class,
        	Statistics.class,
        	Configure.class,
        	Game.class
        };

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
        	
            return position;
        }
        
		public float getScale(boolean focused, int offset) {
			L.d(":)");
			return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset)));
		}
		
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);
            i.setTag(mImageClass[position]);
            i.setImageResource(mImageIds[position]);
            i.setScaleType(ImageView.ScaleType.FIT_XY);
            i.setLayoutParams(new Gallery.LayoutParams(200, 200));
            return i;
        }
    }
}