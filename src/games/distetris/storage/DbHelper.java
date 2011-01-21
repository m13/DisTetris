package games.distetris.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class DbHelper {
	
	private SQLiteDatabase db;


	public DbHelper(Context context) {
		db = (new DatabaseHelper(context)).getWritableDatabase();
	}
	
	public void close() {
		db.close();
	}

	/**
	 * Saves a game into the database
	 * @param type 1 means single game and 0 means team game
	 * @param name This is the player name
	 * @param score This is the points of a player
	 * @param tscore This is the points of the team of this player
	 * @param date The date of this game
	 */
	public void insertValues(int type, String name, Integer score, Integer tscore, Long date) {
		String[] types = new String[] {
				"individual",
				"team" };
		
		String sql = "INSERT INTO "+types[type]+" (name, score, tscore, date)" +
				"VALUES (?, " +
				String.valueOf(score) + ", " + 
				String.valueOf(tscore) + ", " + 
				String.valueOf(date)+")";
		db.execSQL(sql, new String[]{ name });
	}
	
	/**
	 * Retrieves the score of all the last 10 single games
	 * @return Cursor with scores
	 */
	public Cursor getScoreInd() {
		String sql = "SELECT name, score, tscore, date FROM individual ORDER BY score DESC LIMIT 10";
		Cursor cr = db.rawQuery(sql, null);
		cr.moveToFirst();
		return cr;
	}
	
	/**
	 * Retrieves the score of all the last 10 team games
	 * @return Cursor with scores
	 */
	public Cursor getScoreTeam() {
		String sql = "SELECT name, score, tscore, date FROM team ORDER BY score DESC LIMIT 10";
		Cursor cr = db.rawQuery(sql, null);
		cr.moveToFirst();
		return cr;
	}

	/**
	 * Retrieves the local player name
	 * @return A string
	 */
	public String getPlayerName() {
		String sql = "SELECT value FROM config WHERE key='playername' LIMIT 1";
		Cursor cr = db.rawQuery(sql, null);
		String str = (cr.moveToFirst()) ? cr.getString(cr.getColumnIndex("value")) : null;
		cr.close();
		return str;
	}
	
	/**
	 * Sets the local player name
	 * @param A string
	 */
	public void setPlayerName(String name) {
		String sql = "UPDATE config SET value='"+name+"' WHERE key='playername'";
		db.execSQL(sql);
	}
	
	/**
	 * Retrieves all the configuration
	 * @return A bundle with the servername, the number of teams and the number of turns
	 */
	public Bundle getConfCreate() {
		Bundle b = new Bundle();
		String[] requests = new String[]{ "servername", "numteams", "numturns" };
		for (String request : requests) {
			String sql = "SELECT value FROM config WHERE key='"+request+"' LIMIT 1";
			Cursor cr = db.rawQuery(sql, null);
			b.putString(request, 
					(cr.moveToFirst()) ? cr.getString(cr.getColumnIndex("value")) : ""
				);
			cr.close();
		}		
		return b; 
	}
	
	/**
	 * Sets all the configuration
	 * @param b A bundle with the servername, the number of teams and the number of turns
	 */
	public void setConfCreate(Bundle b) {
		String[] requests = new String[]{ "servername", "numteams", "numturns" };
		for (String request : requests) {
			String sql = "UPDATE config SET value='"+b.getString(request)+"' WHERE key='"+request+"'";
			db.execSQL(sql);
		}
	}
}
