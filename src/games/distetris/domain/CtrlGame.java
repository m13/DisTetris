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
		return "0";
		// return
		// piece == next player
		// "DO" == continue playing
		// "END" == end
	}

	public Piece getPiece() {
		return new Piece();
	}

	public void setPiece(Object object) {
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
