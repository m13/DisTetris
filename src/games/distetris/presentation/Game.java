package games.distetris.presentation;

import java.util.Timer;
import java.util.TimerTask;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.L;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Game extends Activity {
	GameView v;
	CtrlDomain dc;
	TimerTask gamelooptask;
	final Handler handler = new Handler();
	Timer t = new Timer();
	int mseconds_actualize = 500;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		L.d("Start");

        v = new GameView(getBaseContext());
        setContentView(v);

		L.d("End");
		dc = CtrlDomain.getInstance();
		//called twice. One for the first piece, next for the second piece
		dc.setNewRandomPiece();
		dc.setNewRandomPiece();
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			gameLoop();
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE){
			v.setPieceBoardPosFromScreenPos(event.getX(), event.getY());
		}
		
		v.invalidate();
		return true;
	}
    
	
	private void gameLoop(){
		dc.cleanBoard();

		//if current piece collides
		if(dc.nextStepPieceCollision()){
			//

			//dc.setNewRandomPiece();
			Log.d("COLLIDES","COLLIDES");
			dc.addCurrentPieceToBoard();
			dc.setNewRandomPiece();
		}
		dc.gameStep();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
		case KeyEvent.KEYCODE_DPAD_DOWN:
			gameLoop();
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if(!dc.currentPieceOffsetCollision(-1)){
				dc.getCurrentPiece().y = dc.getCurrentPiece().y - 1;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if(!dc.currentPieceOffsetCollision(+1)){
				dc.getCurrentPiece().y = dc.getCurrentPiece().y + 1;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			dc.currentPieceRotateLeft();
			break;
		}

		v.invalidate();
		return true;
	}
	
	
	
	public void doInvalidateRepat(){

		gamelooptask = new TimerTask() {
		        public void run() {
		                handler.post(new Runnable() {
		                        public void run() {
		                         gameLoop();
		                         Log.d("TIMER", "Timer set off");
		                        }
		               });
		        }};
		    t.schedule(gamelooptask, 0, mseconds_actualize); 

		 }

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus) doInvalidateRepat();
		
	}

	@Override
	protected void onStop() {
		gamelooptask.cancel();
		super.onStop();
	}

	
    
}
