package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.Listener;
import games.distetris.domain.Piece;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class GameView extends View implements Listener {
	private final int SQSIZE = 20;
	private final int PADDING = 5;
	
	Paint fillpaint;
	Paint strokepaint;
	CtrlDomain dc;
	
	
	public GameView(Context context) {
		super(context);
		fillpaint = new Paint();
		strokepaint = new Paint();
		strokepaint.setAntiAlias(true);
		strokepaint.setStrokeWidth(2);
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
		
		drawSquarePlayerZoneBackground(canvas,board);
		drawGameBackground(canvas);
		drawInfoZone(canvas,board,null);
		drawBoard(canvas,board);
	}

	private void drawBoard(Canvas canvas, int[][] board) {
		int screenwidth =  getWidth();
		int screenheight = getHeight();
		int bw = board[0].length;
		int bh = board.length;
		
		strokepaint.setColor(getResources().getColor(R.color.PIECESTROKE));
		strokepaint.setStrokeWidth(1);
		
		int linezero = screenheight - bh*SQSIZE - SQSIZE;
		
		//the board is draw from left bottom to right top
		for(int r=0;r<bh;r++){
			for(int c=0;c<bw;c++){
				//if it's null there's nothing to draw
				if(board[r][c] != 0){
					fillpaint.setColor(board[r][c]);
					int left = c*SQSIZE;
					int top = linezero + r*SQSIZE;
					int right = c*SQSIZE+SQSIZE;
					int bottom = linezero + r*SQSIZE + SQSIZE;
					canvas.drawRect(left, top, right, bottom, fillpaint);
					canvas.drawRect(left, top, right, bottom, strokepaint);
				}
			}
		}		
	}

	/**
	 * Draws the Info zone of the game, containing
	 *  - Next piece
	 *  - Player's colors and score
	 * @param canvas
	 * @param board
	 */
	private void drawInfoZone(Canvas canvas, int[][] board,Piece p) {
		int screenheight = getHeight();
		int screenwidth = getWidth();
		int bw = board[0].length;
		int bh = board.length;
		
		int zone_start_left = bw*SQSIZE+SQSIZE + PADDING;
		int zone_start_top = screenheight - bh*SQSIZE - SQSIZE;
		int zone_start_right = screenwidth - PADDING;
		int zone_start_bottom = screenheight;
		
		strokepaint.setColor(getResources().getColor(R.color.INFOSQSTROKE));
		strokepaint.setStrokeWidth(3);
		
		int piecesq_left = zone_start_left;
		int piecesq_top = zone_start_top;
		int piecesq_right = zone_start_right;
		int piecesq_bottom = zone_start_top + 2*PADDING + 2*SQSIZE;
		RectF piecesq = new RectF(piecesq_left,piecesq_top,piecesq_right,piecesq_bottom);
		
		
		canvas.drawRoundRect(piecesq, 10, 10, strokepaint);

		strokepaint.setColor(Color.YELLOW);
		strokepaint.setStrokeWidth(1);
		canvas.drawText("3553", piecesq_left, piecesq_top + 100, strokepaint);
		
	}


	/**
	 * Draws the application playing background
	 * @param canvas
	 */
	private void drawGameBackground(Canvas canvas) {
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
		
		fillpaint.setColor(getResources().getColor(R.color.BG));
		strokepaint.setColor(getResources().getColor(R.color.PLAYZONESTROKE));
		strokepaint.setStrokeWidth(1);
		
		canvas.drawRect(left, top, right, bottom, fillpaint);
		canvas.drawRect(left, top, right, bottom, strokepaint);
	}

	
	
}

