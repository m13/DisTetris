package games.distetris.presentation;

import java.util.Timer;
import java.util.TimerTask;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.L;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Game extends Activity {
	private GameView v;
	private CtrlDomain dc;
	private TimerTask gamelooptask;
	private TimerTask refreshviewtask;
	private final Handler handler = new Handler();
	private Timer gamelooptimer = new Timer();
	private Timer refreshviewtimer = new Timer();
	private int mseconds_actualize = 500;
	private int mseconds_viewactualize = 10;

	
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
		
		return true;
	}
    
	/**
	 * Main Game Loop executed every x seconds
	 */
	private void gameLoop(){
		
		
		//if current piece collides
		if(dc.nextStepPieceCollision()){
			if(dc.isGameOver()) {
				v.gameover = true;
				this.gamelooptimer.cancel();
			}
			dc.addCurrentPieceToBoard();
			v.deletelines = dc.cleanBoard();
			dc.setNewRandomPiece();
		}
		dc.gameStep();
	}

	/**
	 * Hook to control onKeyDown
	 * 
	 * @param keyCode	The keyCode pressed
	 * @param event		The event referring the keycode
	 */
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
	
	
	/**
	 * Start Game Loop timer
	 */
	public void doGameLoop(){
		gamelooptask = new TimerTask() {
		        public void run() {
		                handler.post(new Runnable() {
		                        public void run() {
		                         gameLoop();
		                        }
		               });
		        }};
		    gamelooptimer.schedule(gamelooptask, 0, mseconds_actualize); 

		 }
	
	/**
	 * Start view invalidate timer
	 */
	public void doViewInvalidate(){
		this.refreshviewtask = new TimerTask() {
		        public void run() {
		                handler.post(new Runnable() {
		                        public void run() {
		                    		v.invalidate();
		                        }
		               });
		        }};
		    this.refreshviewtimer.schedule(refreshviewtask, 0, this.mseconds_viewactualize); 

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus) doGameLoop();
		
	}

	@Override
	protected void onStop() {
		gamelooptask.cancel();
		super.onStop();
	}

	
    
}
