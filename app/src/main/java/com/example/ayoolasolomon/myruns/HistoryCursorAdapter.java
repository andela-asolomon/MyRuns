package com.example.ayoolasolomon.myruns;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
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
    String input_type = cursor.getString(cursor.getColumnIndexOrThrow("input_type"));

    setDateTime(cursor.getLong(cursor.getColumnIndexOrThrow("date_time")));

    DateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm");
    String date_time = dateFormat.format(mEntry.getmDateTime().getTime());

    double distance = cursor.getDouble(cursor.getColumnIndexOrThrow("distance"));
    int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));

    tvBody.setText(activity_type + ", " + date_time);
    tvPriority.setText(String.valueOf(distance) + " Miles, " + String.valueOf(duration) + "mins 0secs");

    if (input_type.equals("Automatic") || input_type.equals("GPS")) {
      fromByteArrayToLocationArray(cursor.getBlob(cursor.getColumnIndexOrThrow("gps_data")));
    }

  }

  public void setDateTime(long timeInMS) {
    mEntry.setmDateTime(new Date(timeInMS));
  }

  private void fromByteArrayToLocationArray(byte[] bytes) {

    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    IntBuffer intBuffer = byteBuffer.asIntBuffer();

    int[] intArray = new int[bytes.length / Integer.SIZE];
    intBuffer.get(intArray);

    Location[] locations = new Location[intArray.length / 2];

    assert (locations != null);

    for (int i = 0; i < locations.length; i++) {
      locations[i] = new Location("");
      locations[i].setLatitude((double) intArray[i * 2] / 1E6F);
      locations[i].setLongitude((double) intArray[i * 2 + 1] / 1E6F);
    }
  }
}
