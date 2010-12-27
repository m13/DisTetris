package games.distetris.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbHelper {
	
	private SQLiteDatabase db;


	public DbHelper(Context context) {
		db = (new DatabaseHelper(context)).getWritableDatabase();
	}
	
	public void close() {
		db.close();
	}

	public void insertValues(int type, String name, Integer puntuation, Long date) {
		String[] types = new String[] {
				"individual",
				"team" };
		
		String sql = "INSERT INTO "+types[type]+" (name, score, date)" +
				"VALUES (?, " +
				String.valueOf(puntuation) + ", " + 
				String.valueOf(date)+")";
		db.execSQL(sql, new String[]{ name });
	}
	
	public Cursor getScoreInd() {
		String sql = "SELECT name, score, date FROM individual ORDER BY score DESC LIMIT 10";
		Cursor cr = db.rawQuery(sql, null);
		cr.moveToFirst();
		return cr;
	}
	
	public Cursor getScoreTeam() {
		String sql = "SELECT name, score, date FROM team ORDER BY score DESC LIMIT 10";
		Cursor cr = db.rawQuery(sql, null);
		cr.moveToFirst();
		return cr;
	}

	public String getPlayerName() {
		String sql = "SELECT value FROM config WHERE key='playername' LIMIT 1";
		Cursor cr = db.rawQuery(sql, null);
		String str = (cr.moveToFirst()) ? cr.getString(cr.getColumnIndex("value")) : null;
		cr.close();
		return str;
	}
	
	public void setPlayerName(String name) {
		String sql = "UPDATE config SET value='"+name+"' WHERE key='playername'";
		db.execSQL(sql);
	}
}
