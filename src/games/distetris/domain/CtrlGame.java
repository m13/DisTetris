package games.distetris.domain;

import games.distetris.storage.DbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map.Entry;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

public class CtrlGame {

	private static CtrlGame INSTANCE = null;
	private DbHelper db = null;
	Board board;

	private CtrlGame() {
		L.d("Created");

		// TODO: Close db
	}

	public static CtrlGame getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CtrlGame();
		}
		return INSTANCE;
	}

	public void setDbHelper(DbHelper dbHelper) {
		INSTANCE.db = dbHelper;
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

	public ArrayList<Integer> cleanBoard(){
		return this.board.cleanBoard();
	}
	
	/**
	 * Creates a new board initializing the
	 * new random pieces
	 */
	public void createNewCleanBoard(){
		this.board = new Board();
		this.setNewRandomPiece();
		this.setNewRandomPiece();
	}
	
	public Board getBoardToSend(){
		return board;
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
	
	public void setBoard(Board object) {
		this.board = object;
	}
	
	
	/**
	 * it returns all the players
	 * @return Vector of Bundle
	 */
	public HashMap<String,Data> getPlayers() {
		return this.board.getPlayers();
	}

	/**
	 * Initialize the players of the boards
	 * @param teams Vector of the team-name
	 * @param names Vector of the names
	 */
	public void setPlayers(Vector<Integer> teams, Vector<String> names){
		for (int i=0; i<teams.size(); i++) {
			int team = teams.get(i).intValue();
			Data data = new Data(team);
			String name = names.get(i).toString();
			this.board.setPlayer(name, data);
		}
	}

	/**
	 * Saves all the scores into the database
	 */
	public void saveScore() {
		// TODO: call @ isGameOver()
		HashMap<String,Data> playerData = this.board.getPlayers();
		
		int scoresSize = playerData.size();
		int type = (scoresSize==1) ? 0 : 1;
		Long date = System.currentTimeMillis();
		
		for (Entry<String, Data> player : playerData.entrySet()) {
			db.insertValues(type, player.getKey(), player.getValue().getScore(), date);
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

	public Bundle getConfCreate() {
		return db.getConfCreate();
	}

	public void setConfCreate(Bundle b) {
		db.setConfCreate(b);
	}
	

	/**
	 * A step in the game
	 */
	public void gameStep() {

		// FIXME (Ponemos el color que toca en la pieza) (Revisalo y borra el FIX)
		this.board.getCurrentpiece().color = this.board.color();
		this.board.getNextpiece().color = Color.BLACK; // unknown
		// --
		
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

	public boolean currentPieceCollisionRC(int row, int col) {
		Piece pf = new Piece(getCurrentPiece().getType(),getCurrentPiece().getRotation());
		pf.x = row;
		pf.y = col;
		return !this.board.isMovementPossible(pf);
	}

	/**
	 * Drop the current piece to the bottom
	 * And prepare the new pieces
	 */
	public void currentPieceFastFall() {
		this.board.currentPieceFastFall();
		setNewRandomPiece();
	}
}
