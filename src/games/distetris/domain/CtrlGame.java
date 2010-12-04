package games.distetris.domain;

import android.util.Log;

public class CtrlGame {

	private static CtrlGame INSTANCE = null;

	Board board = new Board();

	private CtrlGame() {
		L.d("Created");
	}

	public static CtrlGame getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CtrlGame();
		}
		return INSTANCE;
	}

	// show pop-up where choose the team
	// return the team chosen
	public int windowChoiceTeam(int availableTeams) {
		return (availableTeams-1);
	}

	public void showError(String string) {
		Log.d("ERROR", string);
	}

	public String playPiece() {
		return "NEXT";
		// return
		// "ERROR" == send error to all
		// "NEXT" == next player
		// "DO" == continue playing
		// "END" == end
	}

	public Piece getPiece() {
		return new Piece();
	}

	public void setPiece(Object object) {
		// check if it's diferent of the last piece. Then
		// it means that it's another turn of a same player...
		// A bit odd... I know. Check the logic of CtrlNet!
	}

	
	public int[][] getBoard() {
		return board.getBoard();
	}

	public void setBoard(Object object) {		
	}


	// save the score in the DB
	public void saveScore() {
	}
}
