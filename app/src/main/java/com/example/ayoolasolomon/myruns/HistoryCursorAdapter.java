package com.example.ayoolasolomon.myruns;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

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

    String comment = cursor.getString(cursor.getColumnIndexOrThrow("comment"));
    int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));

    Log.d("TAG", "Aws" + comment + duration);

    tvBody.setText(comment);
    tvPriority.setText(String.valueOf(duration));

  }
}
