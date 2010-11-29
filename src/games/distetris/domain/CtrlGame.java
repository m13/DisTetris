package games.distetris.domain;

import android.util.Log;

public class CtrlGame {

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

	
	public Board getBoard() {
		return new Board();
	}

	public void setBoard(Object object) {		
	}


	// save the score in the DB
	public void saveScore() {
	}
}
