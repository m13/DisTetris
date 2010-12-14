package games.distetris.domain;

import games.distetris.storage.DbHelper;

import java.util.Map.Entry;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class CtrlGame {

	private static CtrlGame INSTANCE = null;
	private DbHelper db = null;

	Board board = new Board();

	private CtrlGame() {
		L.d("Created");

		// TODO: Close db
	}

	public static CtrlGame getInstance(Context ctx) {
		if (INSTANCE == null) {
			INSTANCE = new CtrlGame();
			INSTANCE.db = new DbHelper(ctx);
		}
		return INSTANCE;
	}
	
	public static CtrlGame getInstance() {
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
		return new Piece(1,1);
	}

	public void setPiece(Object object) {
		// check if it's different of the last piece. Then
		// it means that it's another turn of a same player...
		// A bit odd... I know. Check the logic of CtrlNet!
	}

	public void cleanBoard(){
		this.board.cleanBoard();
	}
	
	public int[][] getBoard() {
		return board.getBoard();
	}

	public void setBoard(Object object) {
	}


	// save the score in the DB
	public void saveScore(boolean ratioType) {
		int type = (ratioType) ? 0 : 1;
		Long date = System.currentTimeMillis();
		
		for ( Entry<String, Integer> x : board.getPlayersScore().entrySet()) {
			String name = String.valueOf(x.getKey());
			Integer puntuation = x.getValue();

			db.insertValues(type, name, puntuation, date);
		}
	}
	
	public Cursor getScoreInd() {
		return db.getScoreInd();
	}
	
	public Cursor getScoreTeam() {
		return db.getScoreTeam();
	}

	public String getPlayerName() {
		return db.getPlayerName();
	}
	
	public void setPlayerName(String name) {
		db.setPlayerName(name);
	}
}
