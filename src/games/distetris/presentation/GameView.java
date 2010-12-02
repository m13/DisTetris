package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.Listener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class GameView extends View implements Listener {
	private final int SQSIZE = 20;
	
	Paint fillpaint;
	Paint strokepaint;
	CtrlDomain dc;
	
	
	public GameView(Context context) {
		super(context);
		fillpaint = new Paint();
		strokepaint = new Paint();
		strokepaint.setStrokeWidth(2);
		strokepaint.setColor(Color.GRAY);
		strokepaint.setStrokeCap(Paint.Cap.BUTT);
		strokepaint.setStrokeJoin(Paint.Join.ROUND);
		strokepaint.setStyle(Paint.Style.STROKE);
		fillpaint.setStyle(Paint.Style.FILL);
		
		dc = CtrlDomain.getInstance();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		int[][] board = dc.getBoard();
		int screenwidth =  getWidth();
		int screenheight = getHeight();
		
		drawSquarePlayerZoneBackground(canvas,board);
		drawAppBackground(canvas);

		
		//the board is draw from left bottom to right top
		for(int r=0;r<board.length;r++){
			for(int c=0;c<board[0].length;c++){
				//if it's null there's nothing to draw
				if(board[r][c] != 0){
					fillpaint.setColor(board[r][c]);
					int left = c*SQSIZE;
					int top = screenheight - r*SQSIZE - SQSIZE;
					int right = c*SQSIZE+SQSIZE;
					int bottom = screenheight - r*SQSIZE;
					canvas.drawRect(left, top, right, bottom, fillpaint);
					canvas.drawRect(left, top, right, bottom, strokepaint);
				}
			}
		}
		
		
		
	}

	/**
	 * Draws the application playing background
	 * @param canvas
	 */
	private void drawAppBackground(Canvas canvas) {
		this.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
	}

	/**
	 * Draws on screen the playable area
	 * 
	 * @param canvas
	 * @param board
	 */
	private void drawSquarePlayerZoneBackground(Canvas canvas, int[][] board) {
		int bw = board[0].length;
		int bh = board.length;
		int screenheight = getHeight();
		
		int left = 0; 
		int top = screenheight - bh*SQSIZE - SQSIZE;
		int right = bw*SQSIZE+SQSIZE;
		int bottom = screenheight;
		fillpaint.setColor(getResources().getColor(R.color.BGCOLOR));
		canvas.drawRect(left, top, right, bottom, fillpaint);
	}

	
	
}
