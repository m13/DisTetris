package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.L;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Game extends Activity {
	GameView v;
	CtrlDomain dc;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		L.d("Start");

        v = new GameView(getBaseContext());
        setContentView(v);

		L.d("End");
		dc = CtrlDomain.getInstance();
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
			dc.getCurrentPiece().y = dc.getCurrentPiece().y - 1;
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			dc.getCurrentPiece().y = dc.getCurrentPiece().y + 1;
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			dc.currentPieceRotateLeft();
			break;
		}

		v.invalidate();
		return true;
	}
	
	
    
}
