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

  private ExerciseEntry mEntry;

  public HistoryCursorAdapter(Context context, Cursor c, int flags) {
    super(context, c, flags);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(R.layout.history_cursor_text_view, parent, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {

    mEntry = new ExerciseEntry();

    TextView tvBody = (TextView)view.findViewById(R.id.tvBody);
    TextView tvPriority = (TextView)view.findViewById(R.id.tvPriority);

    String activity_type = cursor.getString(cursor.getColumnIndexOrThrow("activity_type"));
    setDateTime(cursor.getLong(cursor.getColumnIndexOrThrow("date_time")));

    DateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm");
    String string = dateFormat.format(mEntry.getmDateTime().getTime());

    double distance = cursor.getDouble(cursor.getColumnIndexOrThrow("distance"));
    int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));

    tvBody.setText(activity_type + ", " + string);
    tvPriority.setText(String.valueOf(distance) + " Miles, " + String.valueOf(duration) + "mins 0secs");

  }

  public void setDateTime(long timeInMS) {
    Log.d("LONG: ", "here: " + timeInMS);
    mEntry.setmDateTime(new Date(timeInMS));
  }
}
