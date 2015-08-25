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

import java.util.ArrayList;

public class MapDisplayActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

  private GoogleMap mMap;
  private GoogleApiClient mGoogleApiClient;
  private LocationRequest mLocationRequest;
  private Marker start;
  private Marker end;
  protected Boolean mRequestingLocationUpdates;

  Polyline polyline;
  PolylineOptions rectOptions;

  private ArrayList<LatLng> mLatLngList;

  public static final String TAG = "Map";

  private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

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

    String activityType = getIntent().getStringExtra("activity");
    TextView activity = (TextView) findViewById(R.id.type_stats);
    activity.append(activityType);

    mLatLngList = new ArrayList<>(100000);
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

    start = mMap.addMarker(new MarkerOptions()
        .position(latLng));
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

  public void saveBtn(View view) {
    finish();
    stopNotification();
  }

  public void cancelMap(View view) {
    finish();
    stopNotification();
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
}
