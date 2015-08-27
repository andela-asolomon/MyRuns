package com.example.ayoolasolomon.myruns;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MapDisplayActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

  private GoogleMap mMap;
  private GoogleApiClient mGoogleApiClient;
  private LocationRequest mLocationRequest;
  private Marker start;
  private Marker end;
  protected Boolean mRequestingLocationUpdates;

  private ExerciseEntry mEntry;
  private ExercisesDataSource dataSource;

  Polyline polyline;
  PolylineOptions rectOptions;

  private ArrayList<LatLng> mLatLngList;
  private ArrayList<Location> mLocationList;

  public static final String TAG = "Map";
  private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
  private final static double MILLISECONDS_TO_HOUR = 2.7778e-7;
  private final static double KILO = 1000;
  private final static double SECONDS_IN_HOUR = 3600;
  private final static double SECONDS = 60;
  private final static double KM_TO_MILE = 1.609344;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map_display);

    setUpMapIfNeeded();

    startService();

    mRequestingLocationUpdates = false;

    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();

    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(10000)
        .setFastestInterval(5000)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    mLatLngList = new ArrayList<>(100000);
    mLocationList = new ArrayList<>(100000);

    dataSource = new ExercisesDataSource(this);
    mEntry = new ExerciseEntry();
    try {
      dataSource.open();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    String activityType = getIntent().getStringExtra("activity");
    mEntry.setmActivityType(activityType);
    mEntry.setmInputType("GPS");
    TextView activity = (TextView) findViewById(R.id.type_stats);
    activity.append(activityType);

  }

  private void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the map.
    if (mMap == null) {
      // Try to obtain the map from the SupportMapFragment.
      mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
          .getMap();
      // Check if we were successful in obtaining the map.
      if (mMap != null) {
      }
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mGoogleApiClient.connect();
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (mGoogleApiClient.isConnected()) {
      LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
      mGoogleApiClient.disconnect();
    }
  }

  @Override
  public void onConnected(Bundle bundle) {
    Log.i(TAG, "Location services connected.");

    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    if (location != null) {
      handleNewLocation(location);
    }

    if (mRequestingLocationUpdates) {
      startLocationUpdates();
    }
  }

  private void startLocationUpdates() {
    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
  }

  private void handleNewLocation(Location location) {

    mRequestingLocationUpdates = true;
    double currentLatitude = location.getLatitude();
    double currentLongitude = location.getLongitude();
    LatLng latLng = new LatLng(currentLatitude, currentLongitude);
    mLatLngList.add(latLng);
    mLocationList.add(location);

    start = mMap.addMarker(new MarkerOptions().position(latLng));
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
  }

  @Override
  public void onConnectionSuspended(int i) {
    Log.i(TAG, "Location services suspended. Please reconnect.");
    mGoogleApiClient.connect();
  }

  @Override
  public void onLocationChanged(Location location) {
    startRecording(location);
  }

  private void startRecording(Location location) {

    mLocationList.add(location);
    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

    Log.d(TAG, "Latlng: " + latLng);

    if (end != null)
      end.remove();

    mLatLngList.add(latLng);

    end = mMap.addMarker(new MarkerOptions()
        .position((LatLng) mLatLngList.get(mLatLngList.size() - 1)));
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

    if (polyline != null) {
      polyline.remove();
      polyline = null;
    }

    rectOptions = new PolylineOptions();
    rectOptions.addAll(mLatLngList);
    rectOptions.color(Color.GREEN);
    polyline = mMap.addPolyline(rectOptions);

    conversion();

  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    if (connectionResult.hasResolution()) {
      try {
        connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
      } catch (IntentSender.SendIntentException e) {
        e.printStackTrace();
      }
    }
  }

  private void stopNotification() {
    Intent intent = new Intent();
    intent.setAction(TrackingService.ACTION);
    intent.putExtra(TrackingService.STOP_SERVICE_BROADCAST_KEY, TrackingService.RQS_STOP_SERVICE);
    sendBroadcast(intent);
  }

  public void startService() {
    Intent intent = new Intent(this, TrackingService.class);
    startService(intent);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    stopNotification();
  }

  protected void conversion() {

    GregorianCalendar calendar = new GregorianCalendar();
    mEntry.setmDateTime(calendar.getTime());
    DecimalFormat format = new DecimalFormat("#.##");

    TextView textViewDistance = (TextView) findViewById(R.id.distance_map);
    TextView avgSpeed = (TextView) findViewById(R.id.avg_speed);
    TextView curSpeed = (TextView) findViewById(R.id.cur_speed);
    TextView calorie = (TextView) findViewById(R.id.calorie);
    TextView climb = (TextView) findViewById(R.id.climb);

    long startTime =  mLocationList.get(0).getTime();
    long endTIme = mLocationList.get(mLocationList.size() - 1).getTime();
    double durationInHour = (double)Math.abs(endTIme - startTime) * MILLISECONDS_TO_HOUR;
    double durationInMinutes = durationInHour * SECONDS;
    mEntry.setmDuration((int) durationInMinutes);

    double distanceInKM = SphericalUtil.computeLength(mLatLngList) / KILO;
    mEntry.setmDistance(Double.parseDouble(format.format(distanceInKM)));

    double avg_speed;
    if (durationInHour > 0) {
      avg_speed = distanceInKM / durationInHour;
      mEntry.setmAvgSpeed(avg_speed);
    } else {
      avg_speed = .0;
      mEntry.setmAvgSpeed(avg_speed);
    }

    double cur_speed = mLocationList.get(mLocationList.size() - 1).hasSpeed() ? mLocationList.get(mLocationList.size() - 1).getSpeed() : .0;
    double rv = cur_speed / KILO * SECONDS_IN_HOUR / KM_TO_MILE;
    mEntry.setmAvgPace(rv);

    double calOnMap = (distanceInKM / 15) * KILO;
    mEntry.setmCalories((int)calOnMap);

    double prev = mLocationList.get(0).getAltitude();
    double curr = mLocationList.get(mLocationList.size() - 1).getAltitude();
    double clm = Math.abs(curr - prev) / KILO;
    mEntry.setmClimb(clm);

    textViewDistance.setText("Distance: " + format.format(distanceInKM) + " Kilometers");
    avgSpeed.setText("Avg Speed: " + format.format(avg_speed) + " km/h");
    curSpeed.setText("Cur Speed: " + format.format(rv));
    calorie.setText("Calorie: " + format.format(calOnMap));
    climb.setText("Climb: " + format.format(clm));

    fromLocationArrayToByteArray(mLocationList);
  }

  public void saveBtn(View view) {

    dataSource.insertEntry(mEntry);
    finish();
    stopNotification();
    Toast.makeText(this, "Entry Saved", Toast.LENGTH_SHORT ).show();
  }

  public void cancelMap(View view) {
    finish();
    stopNotification();
  }

  private void fromLocationArrayToByteArray(ArrayList<Location> locations) {

    int[] intArray = new int[locations.size() * 2];

    for (int i = 0; i < locations.size(); i++) {
      intArray[i * 2] = (int) (locations.get(i).getLatitude() * 1E6);
      intArray[(i * 2) + 1] = (int) (locations.get(i).getLongitude() * 1E6);
    }

    ByteBuffer byteBuffer = ByteBuffer.allocate(intArray.length * Integer.SIZE);
    IntBuffer intBuffer = byteBuffer.asIntBuffer();
    intBuffer.put(intArray);

    mEntry.setmLocationList(byteBuffer.array());

  }
}
