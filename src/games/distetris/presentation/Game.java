package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.L;
import android.app.Activity;
import android.os.Bundle;
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
		v.invalidate();
		return true;
	}
    
	
	private void gameLoop(){
		dc.cleanBoard();
		dc.gameStep();
		//if current piece collides
		if(dc.currentPieceCollision()){
			//
			dc.addCurrentPieceToBoard();
			dc.setNewRandomPiece();
		}
	}
    
}
