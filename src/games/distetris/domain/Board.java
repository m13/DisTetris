package games.distetris.domain;

import java.io.Serializable;

import android.graphics.Color;

public class Board implements Serializable {
	private static final long serialVersionUID = 1L;
	private int[][] board = new int[20][10];



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
