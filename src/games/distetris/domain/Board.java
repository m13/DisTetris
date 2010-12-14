package games.distetris.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;

public class Board implements Serializable {
	private static final long serialVersionUID = 1L;
	private static int FREEBLOCK = 0;
	
	private int ROWS = 20;
	private int COLS = 10;
	private int PIECESIZE = 5;
	

	// players
	Map<String,Integer> playersScore = new HashMap<String,Integer>();
	
	// board
	private int[][] board = new int[ROWS][COLS]; 	//[0][0] is the top left board
	private Piece currentpiece;
	
	private void testboard(){
		board[ROWS-3][0] = Color.parseColor("#FFFF0000");
		board[ROWS-3][1] = Color.parseColor("#FFFF0000");
		board[ROWS-3][2] = Color.parseColor("#FFFF0000");
		board[ROWS-4][0] = Color.parseColor("#FFFF0000");
		
		board[ROWS-4][1] = Color.parseColor("#FF0000FF");
		board[ROWS-4][2] = Color.parseColor("#FF0000FF");
		board[ROWS-5][1] = Color.parseColor("#FF0000FF");
		board[ROWS-5][2] = Color.parseColor("#FF0000FF");
		
		for(int c=0;c<COLS;c++) board[ROWS-1][c] = Color.parseColor("#FF00FF00");
		for(int c=0;c<COLS;c++) board[ROWS-2][c] = Color.parseColor("#FF00FFF0");
	}
	
	/**
	 * Create a new empty board
	 */
	public Board() {
		for(int r=0;r<ROWS;r++){
			for(int c=0;c<COLS;c++){
				board[r][c] = FREEBLOCK;
			}
		}
		
		testboard();
	}

	/**
	 * Add a specific piece in the board.
	 * 
	 * Impmementation Note:
	 * Iterates over the piece array 5x5 storing the block values
	 * with the color definition of the piece
	 * @param p
	 */
	public void addPiece(Piece p){
		for(int r=0,br = p.x;br<p.x+PIECESIZE;r++,br++){
			for(int c=0, bc = p.y;bc<p.x+PIECESIZE;c++,bc++){
				if(p.getBlockType(r, c)!=PieceConstants.FREEBLOCK){
					board[br][bc] = p.color;
				}
			}
		}
		
	}
	
	/**
	 * Checks if there's a piece in the first row
	 * @return
	 */
	public boolean gameOver(){
		for(int i=0;i<COLS;i++){
			if(board[0][i] != FREEBLOCK) return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the Position is free
	 * @param x X to check
	 * @param y Y to check
	 * @return
	 */
	public boolean isFreeBlock(int x, int y){
		return (board[x][y] == FREEBLOCK) ?  true : false;
	}
	
	/**
	 * Deletes a line of the Board
	 * 
	 * Shifts all the upper lines down
	 * @param line
	 */
	private void killLine(int line){
		for(int r=line;r>0;r--){
			for(int c=0;c<COLS;c++){
				board[r][c] = board[r-1][c];
			}
		}
	}
	
	
	/**
	 * Checks the board for 'deletable' lines.
	 * 
	 * If a line can be deleted this function removes it.
	 */
	public void cleanBoard(){
		for(int r=0;r<ROWS;r++){
			int c = 0;
			for(c=0;c<COLS;c++){
				if(board[r][c] == FREEBLOCK) break;
			}
			if(c >= COLS-2) killLine(r);
		}
	}

	/**
	 * Given a piece, it checks if it collides with the actual board
	 * @param p
	 * @return false if collides (not possible movement) true otherwise
	 */
	public boolean isMovementPossible(Piece p){
		for(int pr=0,br = p.x;br<p.x+PieceConstants.PIECESIZE;pr++,br++){
			for(int pc=0, bc = p.y;bc<p.y+PieceConstants.PIECESIZE;pc++,bc++){
				//Limit collision check
				if( pc < 0 ||
					pc > COLS -1 ||
					pr > ROWS -1){
						if(p.getBlockType(pr, pc)!=PieceConstants.FREEBLOCK) return false; //collides with board limits
				}
				
				//Board Collision Check
				if(bc >= 0){
					if(p.getBlockType(pr, pc)!=PieceConstants.FREEBLOCK
							&& !this.isFreeBlock(br, bc)){
						return false;
					}
				}
			}
		}
		
		//The piece does not collide
		return true;
	}
	
	public Map<String,Integer> getPlayersScore() {
		return playersScore;
	}
	

	
	
	//GETTERS AND SETTERS
	public int[][] getBoard() {
		
		return board;
	}

	public void setCurrentpiece(Piece currentpiece) {
		this.currentpiece = currentpiece;
	}

	public Piece getCurrentpiece() {
		return currentpiece;
	}
	
}
