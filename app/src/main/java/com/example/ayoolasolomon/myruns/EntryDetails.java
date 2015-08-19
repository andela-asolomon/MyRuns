package com.example.ayoolasolomon.myruns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class EntryDetails extends AppCompatActivity {

  private EditText mActivityTypeEdit, mDateTimeEdit, mDurationEdit, mDistanceEdit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_entry_details);

    ExerciseEntry mDetails = (ExerciseEntry) getIntent().getSerializableExtra("DETAILS");
    setUpUI(mDetails);
  }

  private void setUpUI(ExerciseEntry details) {
    mActivityTypeEdit = (EditText) findViewById(R.id.activity_type);
    mDateTimeEdit = (EditText) findViewById(R.id.date_time);
    mDistanceEdit = (EditText) findViewById(R.id.distance);
    mDurationEdit = (EditText) findViewById(R.id.duration);

    mActivityTypeEdit.setText(details.getmActivityType());
    mDateTimeEdit.setText(String.valueOf(details.getmDateTime()));
    mDurationEdit.setText(String.valueOf(details.getmDuration()));
    mDistanceEdit.setText(String.valueOf(details.getmDistance()));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_entry_details, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
