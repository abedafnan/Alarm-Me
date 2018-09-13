package com.example.android.alarmapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Afnan A. A. Abed on 9/8/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "alarms.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static final String TABLE_NAME = "alarms";
    public static final String _ID = "_id";
    public static final String COL_TIME = "time";
    public static final String COL_IS_ENABLED = "is_enabled";

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TIME + " INTEGER NOT NULL, " +
                COL_IS_ENABLED + " INTEGER NOT NULL" +
                ");";

        db.execSQL(CREATE_ALARMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
