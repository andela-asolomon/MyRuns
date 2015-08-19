package com.example.ayoolasolomon.myruns;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.SimpleFormatter;

public class ManualInputActivity extends AppCompatActivity {

  private ExerciseEntry mEntry;
  private ExercisesDataSource dataSource;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manual_input);
    setUpUI();
  }

  private void setUpUI() {

    mEntry = new ExerciseEntry();
    dataSource = new ExercisesDataSource(this);
    try {
      dataSource.open();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    String activityType = getIntent().getStringExtra("activity");
    mEntry.setmActivityType(activityType);
    ListView listView = (ListView) findViewById(R.id.manual_entry);

    String[] manualEntry = getResources().getStringArray(R.array.manual_entry);

    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, manualEntry);
    listView.setAdapter(arrayAdapter);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getItemAtPosition(position).toString();
        myRunsDialog(value);
      }
    });
  }

  public void saveManualEntry(View view) {

    dataSource.insertEntry(mEntry);

    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    finish();
  }

  public void cancelManualEntry(View view) {

    Toast.makeText(this, "Manual Entry Cancelled", Toast.LENGTH_SHORT).show();
    finish();
  }

  public void myRunsDialog(String value) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    MyRunsDialog runsDialog = MyRunsDialog.newInstance(value);
    runsDialog.show(fragmentManager, "dialog");
  }

  public void onDateSet(int year, int month, int day) {
      GregorianCalendar calendar = new GregorianCalendar(year, month, day);
      mEntry.setmDateTime(calendar.getTime());
  }

  public void onTimeSet(int hour, int minute) {

      GregorianCalendar cal = new GregorianCalendar();
      cal.setTime(mEntry.getmDateTime());

      int year = cal.get(Calendar.YEAR);
      int month = cal.get(Calendar.MONTH);
      int day = cal.get(Calendar.DAY_OF_MONTH);

      cal = new GregorianCalendar(year, month, day, hour, minute);
      mEntry.setmDateTime(cal.getTime());
  }

  public void onDuration(String value) {
    Log.d("Duration", value);

    try {
      int i;
      i = Integer.parseInt(value);
      mEntry.setmDuration(i);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onDistance(String value) {
    Log.d("Distance", value);

    try {
      double i;
      i = Double.parseDouble(value);
      mEntry.setmDistance(i);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onCalories(String value) {
    Log.d("Calories", value);

    try {
      int i;
      i = Integer.parseInt(value);
      mEntry.setmCalories(i);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onHeartRate(String value) {
    Log.d("Heart Rate", value);

    try {
      int i;
      i = Integer.parseInt(value);
      mEntry.setmHeartRate(i);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onComment(String comment) {
    Log.d("Comment", comment);
    mEntry.setmComment(comment);
  }

}
