package games.distetris.presentation;

import games.distetris.domain.CtrlDomain;
import games.distetris.domain.Listener;
import games.distetris.domain.Piece;
import games.distetris.domain.PieceConstants;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class GameView extends View implements Listener {
	private final int SQSIZE = 20;
	private final int PADDING = 5;
	
	private Paint fillpaint;
	private Paint strokepaint;
	private CtrlDomain dc;
	private int boardw;
	private int boardh;
	public boolean gameover = false;
	
	
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
		
		int[][] board = dc.getBoard();
		boardw = board[0].length;
		boardh = board.length;
		
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
		drawInfoZone(canvas,board,dc.getNextPiece());
		drawBoard(canvas,board);
		drawPiece(canvas);
		
		if(gameover) drawGameOverLayer(canvas);
	}

	/**
	 * Draw the GameOver Screen
	 * @param canvas
	 */
	private void drawGameOverLayer(Canvas canvas) {
		int top = 0;
		int left = 0;
		int bottom = getHeight();
		int right = getWidth();
		
		fillpaint.setColor(getResources().getColor(R.color.GAMEOVERSCREEN));
		canvas.drawRect(left, top, right, bottom, fillpaint);
		
		strokepaint.setColor(Color.WHITE);
		strokepaint.setTextSize(12);
		canvas.drawText("GAME OVER", left + right/3, top + bottom/2, strokepaint);
	}

	/**
	 * Draws a piece in its location
	 * @param canvas
	 */
	private void drawPiece(Canvas canvas) {
		Piece p 			= dc.getCurrentPiece();
		byte[][] pmatrix 	= p.getPieceMatrix();
		int psize 			= PieceConstants.PIECESIZE;
		
		strokepaint.setColor(getResources().getColor(R.color.PIECESTROKE));
		strokepaint.setStrokeWidth(1);
		fillpaint.setColor(p.color);
		
		for(int r = p.x,pr = 0;r<p.x+psize;r++,pr++){
			for(int c = p.y,pc = 0;c<p.y+psize;c++,pc++){
				if(p.getBlockType(pr, pc)!=PieceConstants.FREEBLOCK) drawSquare(canvas,r,c);
			}
		}

		
	}

	/**
	 * Draws a piece or board square in screen at the correct position
	 * @param canvas
	 */
	private void drawSquare(Canvas canvas,int r,int c){
		int linezero = getHeight() - boardh*SQSIZE - SQSIZE;
		int left = c*SQSIZE;
		int top = linezero + r*SQSIZE;
		int right = c*SQSIZE+SQSIZE;
		int bottom = linezero + r*SQSIZE + SQSIZE;
		
		canvas.drawRect(left, top, right, bottom, fillpaint);
		canvas.drawRect(left, top, right, bottom, strokepaint);
	}
	
	private void drawBoard(Canvas canvas, int[][] board) {
		
		strokepaint.setColor(getResources().getColor(R.color.PIECESTROKE));
		strokepaint.setStrokeWidth(1);
		
		int linezero = getHeight() - boardh*SQSIZE - SQSIZE;
		
		//the board is draw from left bottom to right top
		for(int r=0;r<boardh;r++){
			for(int c=0;c<boardw;c++){
				//if it's null there's nothing to draw
				if(board[r][c] != 0){
					fillpaint.setColor(board[r][c]);
					drawSquare(canvas,r,c);
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
		
		int zone_start_left = boardw*SQSIZE + PADDING;
		int zone_start_top = getHeight() - boardh*SQSIZE - SQSIZE;
		int zone_start_right = getWidth() - PADDING;
		int zone_start_bottom = getHeight();
		
		strokepaint.setColor(getResources().getColor(R.color.INFOSQSTROKE));
		strokepaint.setStrokeWidth(3);
		
		int piecesq_left = zone_start_left;
		int piecesq_top = zone_start_top;
		int piecesq_right = zone_start_right;
		int piecesq_bottom = zone_start_top + 2*PADDING + 4*SQSIZE;
		RectF piecesq = new RectF(piecesq_left,piecesq_top,piecesq_right,piecesq_bottom);

		canvas.drawRoundRect(piecesq, 10, 10, strokepaint);
		drawNextPiece(piecesq,canvas,p);
		
		drawScores(canvas,piecesq_bottom,zone_start_left);
		
		
	}

	/**
	 * Draw the scores on screen
	 * @param canvas
	 * @param top top position where toTimerTask example start drawing
	 * @param left left position where to start drawing
	 */
	private void drawScores(Canvas canvas, int top, int left) {
		
		//foreach Player draw its score
		strokepaint.setColor(Color.YELLOW);
		strokepaint.setStrokeWidth(1);
		canvas.drawText("3553", left, top + PADDING, strokepaint);
	}

	/**
	 * Draws the next piece inside a Rectangle
	 * @param piecesq
	 * @param canvas
	 * @param p
	 */
	private void drawNextPiece(RectF piecesq, Canvas canvas, Piece p) {
		int left  = (int) piecesq.left;
		int top = (int) piecesq.top;
		int psize = PieceConstants.PIECESIZE;
		
		strokepaint.setColor(getResources().getColor(R.color.PIECESTROKE));
		strokepaint.setStrokeWidth(1);
		fillpaint.setColor(p.color);
		
		for(int r = p.x,pr = 0;r<p.x+psize;r++,pr++){
			for(int c = p.y,pc = 0;c<p.y+psize;c++,pc++){
				if(p.getBlockType(pr, pc)!=PieceConstants.FREEBLOCK){
					int pleft 	= left + pc*SQSIZE;
					int ptop 	= top + pr*SQSIZE;
					int pright 	= left + pc*SQSIZE + SQSIZE;
					int pbott 	= top + pr*SQSIZE + SQSIZE;
					canvas.drawRect(pleft, ptop, pright, pbott, fillpaint);
					canvas.drawRect(pleft, ptop, pright, pbott, strokepaint);
				}
			}
		}
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
		
		int left = 0; 
		int top = getHeight() - boardh*SQSIZE - SQSIZE;
		int right = boardw*SQSIZE;
		int bottom = getHeight();
		
		fillpaint.setColor(getResources().getColor(R.color.BG));
		strokepaint.setColor(getResources().getColor(R.color.PLAYZONESTROKE));
		strokepaint.setStrokeWidth(1);
		
		canvas.drawRect(left, top, right, bottom, fillpaint);
		canvas.drawRect(left, top, right, bottom, strokepaint);
	}


	/**
	 * Set the current Piece new position from
	 * touched screen position
	 * @param x
	 * @param y
	 */
	public void setPieceBoardPosFromScreenPos(float x, float y){
		int bc = calcBoardRowFromScreenX(x);
		if(bc==-1) return;
		dc.getCurrentPiece().y = bc;
	}

	/**
	 * Calculate Board column from screen x
	 * @param x
	 * @return
	 */
	private int calcBoardRowFromScreenX(float x) {
		int limitright = boardw*SQSIZE + SQSIZE;
		
		if(x>limitright){
			return -1;
		}
		for(int c=0;c<boardw;c++){
			int left = c*SQSIZE;
			int right = c*SQSIZE+SQSIZE;
			if(x<right && x>left){
				return c;
			}
		}
		return -1;
	}
	
}

