package com.example.ayoolasolomon.myruns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    values.put(MySQLiteHelper.COLUMN_INPUT_TYPE, entry.getmInputType());
    values.put(MySQLiteHelper.COLUMN_DURATION, entry.getmDuration());
    values.put(MySQLiteHelper.COLUMN_DISTANCE, entry.getmDistance());
    values.put(MySQLiteHelper.COLUMN_DURATION, entry.getmDuration());
    values.put(MySQLiteHelper.COLUMN_COMMENT, entry.getmComment());
    values.put(MySQLiteHelper.COLUMN_DATE_TIME, entry.getmDateTime().getTime());
    values.put(MySQLiteHelper.COLUMN_HEARTRATE, entry.getmHeartRate());
    values.put(MySQLiteHelper.COLUMN_CALORIES, entry.getmCalories());

    long id = database.insert(MySQLiteHelper.TABLE_EXERCISES, null, values);
    database.close();
  }

  public ExerciseEntry fetchEntry(Cursor c) {
    long id = c.getLong(c.getColumnIndexOrThrow("_id"));

    String query = "SELECT * FROM ENTRIES WHERE _id = " + id;

    Cursor cursor = database.rawQuery(query, null);
    cursor.moveToFirst();

    ExerciseEntry entry = cursorDetails(cursor);
    cursor.close();
    return entry;
  }


  public Cursor fetchEntries() {
    String selectQuery = "SELECT * FROM " + MySQLiteHelper.TABLE_EXERCISES;
    Cursor cursor = database.rawQuery(selectQuery, null);
    return cursor;
  }

  private ExerciseEntry cursorDetails(Cursor cursor) {
    ExerciseEntry entry = new ExerciseEntry();
    entry.setId(cursor.getLong(0));
    entry.setmActivityType(cursor.getString(2));
    entry.setmDateTime(setDateTime(cursor.getLong(cursor.getColumnIndexOrThrow("date_time"))));
    entry.setmDuration(cursor.getInt(4));
    entry.setmDistance(cursor.getDouble(5));
    return entry;
  }

  public Date setDateTime(long timeInMS) {
    return new Date(timeInMS);
  }

  public void removeEntry(Long id) {
    database.delete(MySQLiteHelper.TABLE_EXERCISES, MySQLiteHelper.COLUMN_ID + " = " + id, null);
  }
}
