package com.example.ayoolasolomon.myruns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EntryDetails extends AppCompatActivity {

  private EditText mActivityTypeEdit, mDateTimeEdit, mDurationEdit, mDistanceEdit;
  private ExerciseEntry mDetails;
  private ExercisesDataSource dataSource;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_entry_details);

    mDetails = (ExerciseEntry) getIntent().getSerializableExtra("DETAILS");
    dataSource = new ExercisesDataSource(this);

    try {
      dataSource.open();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    setUpUI(mDetails);
  }

  private void setUpUI(ExerciseEntry details) {
    mActivityTypeEdit = (EditText) findViewById(R.id.activity_type);
    mDateTimeEdit = (EditText) findViewById(R.id.date_time);
    mDistanceEdit = (EditText) findViewById(R.id.distance);
    mDurationEdit = (EditText) findViewById(R.id.duration);

    mActivityTypeEdit.setText(details.getmActivityType());
    mDateTimeEdit.setText(dateFormat(details.getmDateTime().getTime()));
    mDurationEdit.setText(String.valueOf(details.getmDuration()) + " secs");
    mDistanceEdit.setText(String.valueOf(details.getmDistance()) + " Miles");
  }

  private String dateFormat(Long dateTime) {
    DateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss");
    return dateFormat.format(dateTime);
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
    if (id == R.id.delete) {
      dataSource.removeEntry(mDetails.getId());
      Toast.makeText(this, "Entry #" + mDetails.getId() + " deleted", Toast.LENGTH_SHORT).show();
      finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
