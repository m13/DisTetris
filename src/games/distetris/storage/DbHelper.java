package games.distetris.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper {

	private static final String DATABASE_NAME = "DISTETRIS";
	private static final int DATABASE_VERSION = 4;

	private SQLiteDatabase db;


	// -PUBLIC-

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

	
	// -CLASS-

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d("DB", "onCreate");
			String[] sqlBlock = new String[] {
					"CREATE TABLE config (key TEXT, value TEXT)",
					"INSERT INTO config (key, value) VALUES ('FirstTime', 1)",
					"INSERT INTO config (key, value) VALUES ('playername', 'Viciado')",

					"CREATE TABLE individual (name TEXT, score INT, date INT)",
					"CREATE TABLE team (name TEXT, score INT, date INT)"
			};

			for (String sql : sqlBlock) { db.execSQL(sql); }
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d("DB", "onUpgrade");
			String[] sqlBlock = new String[] {
					"DROP TABLE IF EXISTS config",
					"DROP TABLE IF EXISTS individual",
					"DROP TABLE IF EXISTS team"
			};

			for (String sql : sqlBlock) { db.execSQL(sql); }
			onCreate(db);
		}
	}
}
