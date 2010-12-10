package games.distetris.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper {

	private static final String DATABASE_NAME = "DISTETRIS";
	private static final int DATABASE_VERSION = 1;

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
		
		String sql = "INSERT INTO "+types[type]+" (name, puntuation, date)" +
				"VALUES (?, " +
				String.valueOf(puntuation) + ", " + 
				String.valueOf(date)+")";
		db.execSQL(sql, new String[]{ name });
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
					"CREATE TABLE config (key TEXT, value INT)",
					"INSERT INTO config (key, value) VALUES ('FirstTime', 1)",

					"CREATE TABLE individual (name TEXT, puntuation INT, date INT)",
					"CREATE TABLE team (name TEXT, puntuation INT, date INT)"
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
