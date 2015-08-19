package com.example.ayoolasolomon.myruns;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ayoolasolomon on 8/18/15.
 */
public class HistoryCursorAdapter extends CursorAdapter {

  public HistoryCursorAdapter(Context context, Cursor c, int flags) {
    super(context, c, flags);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(R.layout.history_cursor_text_view, parent, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {

    TextView tvBody = (TextView)view.findViewById(R.id.tvBody);
    TextView tvPriority = (TextView)view.findViewById(R.id.tvPriority);

    String activity_type = cursor.getString(cursor.getColumnIndexOrThrow("activity_type"));
    String  date_time = cursor.getString(cursor.getColumnIndexOrThrow("date_time"));


    double distance = cursor.getDouble(cursor.getColumnIndexOrThrow("distance"));
    int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));

    tvBody.setText(activity_type + ", " + date_time);
    tvPriority.setText(String.valueOf(distance) + " Miles, " + String.valueOf(duration) + "mins 0secs");

  }
}
