package com.example.ayoolasolomon.myruns;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by ayoolasolomon on 8/17/15.
 */
public class ExercisesDataSource {

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
}
