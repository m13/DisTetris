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
	private int[][] board = new int[ROWS][COLS];
	
	
	/**
	 * Create a new empty board
	 */
	public Board() {
		for(int r=0;r<ROWS;r++){
			for(int c=0;c<COLS;c++){
				board[r][c] = FREEBLOCK;
			}
		}
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
		for(int r=0,br = p.x;r<PIECESIZE;r++,br++){
			for(int c=0, bc = p.y;c<PIECESIZE;c++,bc++){
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
	public void killLine(int line){
		for(int r=line;r>0;r++){
			for(int c=0;c<COLS;c++){
				board[r][c] = board[r-1][c];
			}
		}
	}
	
	public void cleanBoard(){
		
	}
	
	public Map<String,Integer> getPlayersScore() {
		return playersScore;
	}
	
	
	
	//GETTERS AND SETTERS
	public int[][] getBoard() {
		board[0][0] = Color.parseColor("#FFFF0000");
		board[0][1] = Color.parseColor("#FFFF0000");
		board[0][2] = Color.parseColor("#FFFF0000");
		board[1][0] = Color.parseColor("#FFFF0000");
		
		board[1][1] = Color.parseColor("#FF0000FF");
		board[1][2] = Color.parseColor("#FF0000FF");
		board[2][1] = Color.parseColor("#FF0000FF");
		board[2][2] = Color.parseColor("#FF0000FF");
		
		return board;
	}
	
}
