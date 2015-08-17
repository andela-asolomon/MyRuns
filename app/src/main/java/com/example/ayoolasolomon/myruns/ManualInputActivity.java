package com.example.ayoolasolomon.myruns;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;

public class ManualInputActivity extends AppCompatActivity {

  ExerciseEntry mEntry;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manual_input);

    setUpUI();
  }

  private void setUpUI() {

    String activityType = getIntent().getStringExtra("activity");
    Log.d("Lol", "nice: " + activityType);
//    mEntry.setmActivityType(Integer.parseInt(activityType));
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
    String date = Integer.toString(year) + "/" + Integer.toString(month) + "/" + Integer.toString(day);
    Log.d("Date: ", date);
  }

  public void onTimeSet(int hour, int minute) {
    String time = Integer.toString(hour) + ":" + Integer.toString(minute);
    Log.d("Time", time);
  }

  public void onDuration(String value) {
    Log.d("Duration", value);
  }

  public void onDistance(String value) {
    Log.d("Distance", value);
  }

  public void onCalories(String value) {
    Log.d("Calories", value);
  }

  public void onHeartRate(String value) {
    Log.d("Heart Rate", value);
  }

  public void onComment(String comment) {
    Log.d("Comment", comment);
  }

}
