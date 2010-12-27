package games.distetris.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "DISTETRIS";
	private static final int DATABASE_VERSION = 4;
	
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
