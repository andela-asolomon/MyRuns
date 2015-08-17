package com.example.ayoolasolomon.myruns;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ayoolasolomon on 8/17/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_EXERCISES = "exercise";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_INPUT_TYPE = "input_type";
  public static final String COLUMN_ACTIVITY_TYPE = "activity_type";
  public static final String COLUMN_DATE_TIME = "date_time";
  public static final String COLUMN_DURATION = "duration";
  public static final String COLUMN_DISTANCE = "distance";
  public static final String COLUMN_AVG_PACE = "avg_pace";
  public static final String COLUMN_AVG_SPEED = "avg_speed";
  public static final String COLUMN_CALORIES = "calories";
  public static final String COLUMN_CLIMB = "climb";
  public static final String COLUMN_HEARTRATE = "heartrate";
  public static final String COLUMN_PRIVACY = "privacy";
  public static final String COLUMN_COMMENT = "comment";
  public static final String COLUMN_GPS_DATA = "gps_data";

  private static final String DATABASE_NAME = "exercise.db";
  private static final int DATABASE_VERSION = 1;

  private static final String
      DATABASE_CREATE         = "CREATE TABLE "
      + TABLE_EXERCISES       + "("
      + COLUMN_ID             + "INTEGER PRIMARY KEY AUTOINCREMENT"
      + COLUMN_INPUT_TYPE     + "INTEGER NOT NULL"
      + COLUMN_ACTIVITY_TYPE  + "INTEGER NOT NULL"
      + COLUMN_DATE_TIME      + "DATETIME NOT NULL"
      + COLUMN_DURATION       + "INTEGER NOT NULL"
      + COLUMN_DISTANCE       + "FLOAT"
      + COLUMN_AVG_PACE       + "FLOAT"
      + COLUMN_AVG_SPEED      + "FLOAT"
      + COLUMN_CALORIES       + "INTEGER"
      + COLUMN_CLIMB          + "FLOAT"
      + COLUMN_HEARTRATE      + "INTEGER"
      + COLUMN_COMMENT        + "TEXT"
      + COLUMN_PRIVACY        + "INTEGER"
      + COLUMN_GPS_DATA       + "BLOB"
                              + ");";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
    onCreate(db);
  }
}
