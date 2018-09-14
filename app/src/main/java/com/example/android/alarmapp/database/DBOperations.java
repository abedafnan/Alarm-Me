package com.example.android.alarmapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.android.alarmapp.Alarm;

import java.util.ArrayList;

/**
 * Created by Afnan A. A. Abed on 9/8/2018.
 */

public class DBOperations {

    private DBHelper dbHelper;

    public DBOperations(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long addAlarm(Alarm alarm) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_TIME, alarm.getTime());
        values.put(DBHelper.COL_IS_ENABLED, alarm.isEnabled());

        return database.insert(DBHelper.TABLE_NAME, null, values);
    }

    public int deleteAlarm(Alarm alarm) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long id = alarm.getId();
        final String where = DBHelper._ID + "=?";
        final String[] whereArgs = new String[] { Long.toString(id) };

        return database.delete(DBHelper.TABLE_NAME, where, whereArgs);
    }

    public ArrayList<Alarm> readAllAlarms() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        ArrayList<Alarm> alarmsArrayList = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.TABLE_NAME, null,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            long alarmTime = cursor.getLong(cursor.getColumnIndex(DBHelper.COL_TIME));
            int alarmIsEnabled = cursor.getInt(cursor.getColumnIndex(DBHelper.COL_IS_ENABLED));
            long alarmId = cursor.getLong(cursor.getColumnIndex(DBHelper._ID));

            Alarm alarm = new Alarm(alarmTime, alarmIsEnabled, alarmId);
            alarmsArrayList.add(alarm);
        }

        cursor.close();
        return alarmsArrayList;
    }
}
