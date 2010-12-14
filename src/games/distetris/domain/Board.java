package games.distetris.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;

public class Board implements Serializable {
	private static final long serialVersionUID = 1L;

	// players
	Map<String,Integer> playersScore = new HashMap<String,Integer>();

	public Map<String,Integer> getPlayersScore() {
		return playersScore;
	}
	
	// board
	private int[][] board = new int[20][10];
	
	public int[][] getBoard() {
		board[1][0] = Color.parseColor("#FFFF0000");
		board[1][1] = Color.parseColor("#FF00FF00");
		board[1][2] = Color.parseColor("#FF00F0F0");
		
		board[2][0] = Color.parseColor("#FF00F000");
		board[3][1] = Color.parseColor("#FFFFFF00");
		board[4][2] = Color.parseColor("#FFFF00FF");
		return board;
	}
	
	
	public void setTestBoard(){
		board[1][0] = Color.parseColor("#FFFF0000");
		board[1][1] = Color.parseColor("#FFFF0000");
		board[1][2] = Color.parseColor("#FFFF0000");
		board[1][3] = Color.parseColor("#FFFF0000");
		board[1][4] = Color.parseColor("#FFFF0000");
		board[1][5] = Color.parseColor("#FFFF0000");
		board[1][6] = Color.parseColor("#FFFF0000");
		board[1][7] = Color.parseColor("#FFFF0000");
		board[1][8] = Color.parseColor("#FFFF0000");
		board[1][9] = 0;
		board[1][10] = Color.parseColor("#FFFF0000");
		
	}
}
