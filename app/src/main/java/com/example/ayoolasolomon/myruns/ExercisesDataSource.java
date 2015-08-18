package com.example.ayoolasolomon.myruns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Created by ayoolasolomon on 8/17/15.
 */
public class ExercisesDataSource {

  ExerciseEntry mEntry;

  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;

  private String[] allColumns = {
      MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_GPS_DATA,
      MySQLiteHelper.COLUMN_PRIVACY, MySQLiteHelper.COLUMN_COMMENT,
      MySQLiteHelper.COLUMN_ACTIVITY_TYPE, MySQLiteHelper.COLUMN_AVG_PACE,
      MySQLiteHelper.COLUMN_AVG_SPEED, MySQLiteHelper.COLUMN_CALORIES,
      MySQLiteHelper.COLUMN_CLIMB, MySQLiteHelper.COLUMN_DATE_TIME,
      MySQLiteHelper.COLUMN_DISTANCE, MySQLiteHelper.COLUMN_DURATION,
      MySQLiteHelper.COLUMN_HEARTRATE, MySQLiteHelper.COLUMN_INPUT_TYPE
  };

  public ExercisesDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    database.close();
  }

  public void insertEntry(ExerciseEntry entry) {

    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_ACTIVITY_TYPE, entry.getmActivityType());
    values.put(MySQLiteHelper.COLUMN_INPUT_TYPE, entry.getmInputType());
    values.put(MySQLiteHelper.COLUMN_DURATION, entry.getmDuration());
    values.put(MySQLiteHelper.COLUMN_DISTANCE, entry.getmDistance());
    values.put(MySQLiteHelper.COLUMN_DURATION, entry.getmDuration());
    values.put(MySQLiteHelper.COLUMN_COMMENT, entry.getmComment());
    values.put(MySQLiteHelper.COLUMN_DATE_TIME, entry.getmDateTime().toString());
    values.put(MySQLiteHelper.COLUMN_HEARTRATE, entry.getmHeartRate());
    values.put(MySQLiteHelper.COLUMN_CALORIES, entry.getmCalories());

    long id = database.insert(MySQLiteHelper.TABLE_EXERCISES, null, values);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_EXERCISES, allColumns, MySQLiteHelper.COLUMN_ID
        + " + " + id, null, null, null, null);
    cursor.moveToFirst();
    database.close();
    Log.d("id", "Id: " + cursor);
  }

  public ExerciseEntry fetchEntryByIndex(long id) {

    String[] ID = { String.valueOf(id)};
    Cursor cursor = database.query(MySQLiteHelper.TABLE_EXERCISES, allColumns, MySQLiteHelper.COLUMN_ID
        + "=?", ID, null, null, null, null);

    if (cursor != null) {
      cursor.moveToFirst();
    }

    ExerciseEntry entry = new ExerciseEntry();
    entry.setmActivityType(Integer.parseInt(cursor.getString(2)));

    return entry;
  }

  public void removeEntry(ExerciseEntry entry) {
    long id = mEntry.getId();
    database.delete(MySQLiteHelper.TABLE_EXERCISES, MySQLiteHelper.COLUMN_ID + " + " + id, null);
  }


  public Cursor fetchEntries() {
    List<ExerciseEntry> entriesList = new ArrayList<ExerciseEntry>();

    String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_EXERCISES;
    Cursor cursor = database.rawQuery(selectQuery, null);

//    if (cursor.moveToFirst()) {
//      do {
//        ExerciseEntry entry = new ExerciseEntry();
//        entry.setId(Long.parseLong(cursor.getString(0)));
//
//        entriesList.add(entry);
//      } while (cursor.moveToNext());
//    }

    return cursor;
  }


}
