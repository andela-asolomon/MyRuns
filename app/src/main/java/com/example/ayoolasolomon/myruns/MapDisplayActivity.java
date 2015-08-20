package com.example.ayoolasolomon.myruns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MapDisplayActivity extends AppCompatActivity {

  private GoogleMap mMap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map_display);

    setUpMapIfNeeded();

    String activityType = getIntent().getStringExtra("activity");
    TextView activity = (TextView) findViewById(R.id.type_stats);
    activity.setText(activityType);
  }

  private void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the map.
    if (mMap == null) {
      // Try to obtain the map from the SupportMapFragment.
      mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
          .getMap();
      // Check if we were successful in obtaining the map.
      if (mMap != null) {
//        mMap.setOnMapClickListener(this);
//        mMap.setOnMapLongClickListener(this);
      }
    }
  }

  public void saveBtn(View view) {

    finish();
  }

  public void cancelMap(View view) {

    finish();
  }
}
