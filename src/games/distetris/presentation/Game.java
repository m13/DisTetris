package games.distetris.presentation;

import java.util.Timer;
import java.util.TimerTask;

import games.distetris.domain.CtrlDomain;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;


/**
 * Game activity. Player's zone
 * 
 * @author Jordi Castells
 *
 */
public class Game extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
	private GameView v;
	private CtrlDomain dc;
	private TimerTask gamelooptask;
	private TimerTask refreshviewtask;
	private final Handler handler = new Handler();
	private Timer gamelooptimer = new Timer();
	private Timer refreshviewtimer = new Timer();
	private int mseconds_actualize = 500;
	private int mseconds_viewactualize = 10;
	private boolean movepiece = false;
	private GestureDetector gestureScanner;
	private static int threshold_vy = 800;
	private static int threshold_vx = 500;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        v = new GameView(getBaseContext());
        setContentView(v);
        
        gestureScanner = new GestureDetector(this);
		
		dc = CtrlDomain.getInstance();
		//called twice. One for the first piece, next for the second piece
		//dc.setNewRandomPiece();
		//dc.setNewRandomPiece();
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		int action = event.getAction();

		if(action == MotionEvent.ACTION_DOWN){	
			if(v.touchedInPlayPiece(event.getX(),event.getY())){
				this.movepiece = true;
			}
		}
		else if(action == MotionEvent.ACTION_MOVE){			
			if(this.movepiece){
				int piece_col = v.calcBoardColFromScreenX(event.getX());
				
				if(piece_col!=-1 && !dc.currentPieceCollisionRC(dc.getCurrentPiece().x,piece_col)){
					dc.getCurrentPiece().y = piece_col;	
				}
				else{
					this.movepiece = false;
				}
			}
		}
		else if(action == MotionEvent.ACTION_UP){
			this.movepiece = false;
		}
		
		return gestureScanner.onTouchEvent(event);
	}
    
	/**
	 * Main Game Loop executed every x seconds
	 */
	private void gameLoop(){
		if(dc.isMyTurn()){
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

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			CtrlDomain.getInstance().serverTCPDisconnectClients();
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


	public void onLongPress(MotionEvent e)
	{

	}

	@Override
	public boolean onDown(MotionEvent e) {

		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(velocityY>threshold_vy){
			dc.currentPieceFastFall();
		}
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		int x = (int) e.getX();
		
		if(x<50){
			if(!dc.currentPieceOffsetCollision(-1)){
				dc.getCurrentPiece().y = dc.getCurrentPiece().y - 1;
			}
		}
		else if(x>v.getWidth()-50){
			if(!dc.currentPieceOffsetCollision(+1)){
				dc.getCurrentPiece().y = dc.getCurrentPiece().y + 1;
			}
		}
		else{
			dc.currentPieceRotateLeft();	
		}
		
		
		return false;
	}



    
}
