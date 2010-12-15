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
	
	public Piece getCurrentPiece() {
		return this.board.getCurrentpiece();
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

	/**
	 * Sets a new random piece on the board
	 */
	public void setNewRandomPiece(){
		int pid = new Double(Math.random() * PieceConstants.npieces).intValue();
		Piece p = new Piece(pid,0);
		this.board.setCurrentpiece(this.board.getNextpiece());
		this.board.setNextpiece(p);
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

	/**
	 * A step in the game
	 */
	public void gameStep() {
		this.board.getCurrentpiece().x = this.board.getCurrentpiece().x + 1;
	}

	/**
	 * Checks if the current piece is colliding
	 * @return
	 */
	public boolean currentPieceCollision() {
		return !this.board.isMovementPossible(getCurrentPiece());
	}
	
	/**
	 * Checks if the current piece is colliding on next step
	 * @return
	 */
	public boolean nextStepPieceCollision() {
		Piece pf = new Piece(getCurrentPiece().getType(),getCurrentPiece().getRotation());
		pf.x = getCurrentPiece().x + 1;
		pf.y = getCurrentPiece().y;
		return !this.board.isMovementPossible(pf);
	}

	/**
	 * Adds the current piece to the board
	 */
	public void addCurrentPieceToBoard() {
		this.board.addPiece(getCurrentPiece());
	}

	/**
	 * Rotate the current piece left.
	 * If the rotation is incorrect rotate right again
	 */
	public void rotateLeft() {
		getCurrentPiece().rotateLeft();
		if(this.currentPieceCollision()) getCurrentPiece().rotateRight();
	}
	
	/**
	 * Rotate the current piece right.
	 * If the rotation is incorrect rotate left again
	 */
	public void rotateRight() {
		getCurrentPiece().rotateRight();
		if(this.currentPieceCollision()) getCurrentPiece().rotateLeft();
	}

	public boolean currentPieceOffsetCollision(int offset) {
		Piece pf = new Piece(getCurrentPiece().getType(),getCurrentPiece().getRotation());
		pf.x = getCurrentPiece().x;
		pf.y = getCurrentPiece().y + offset;
		return !this.board.isMovementPossible(pf);
	}

	public Piece getNextPiece() {
		return this.board.getNextpiece();
	}

	public boolean isGameOver() {
		return this.board.gameOver();
	}
}
