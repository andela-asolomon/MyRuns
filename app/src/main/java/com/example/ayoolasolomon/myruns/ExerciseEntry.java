package com.example.ayoolasolomon.myruns;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ayoolasolomon on 8/17/15.
 */
public class ExerciseEntry {

  private Long id;
  private String  mInputType;        // Manual, GPS or automatic
  private String mActivityType;     // Running, cycling etc.
  private Date mDateTime;    // When does this entry happen
  private int mDuration;         // Exercise duration in seconds
  private double mDistance;      // Distance traveled. Either in meters or feet.
  private double mAvgPace;       // Average pace
  private double mAvgSpeed;      // Average speed
  private int mCalories;          // Calories burnt
  private double mClimb;         // Climb. Either in meters or feet.
  private int mHeartRate;        // Heart rate
  private String mComment;       // Comments
  private ArrayList<LatLng> mLocationList; // Location list

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getmInputType() {
    return mInputType;
  }

  public void setmInputType(String mInputType) {
    this.mInputType = mInputType;
  }

  public String getmActivityType() {
    return mActivityType;
  }

  public void setmActivityType(String mActivityType) {
    this.mActivityType = mActivityType;
  }

  public Date getmDateTime() {
    return mDateTime;
  }

  public void setmDateTime(Date mDateTime) {
    this.mDateTime = mDateTime;
  }

  public int getmDuration() {
    return mDuration;
  }

  public void setmDuration(int mDuration) {
    this.mDuration = mDuration;
  }

  public double getmDistance() {
    return mDistance;
  }

  public void setmDistance(double mDistance) {
    this.mDistance = mDistance;
  }

  public double getmAvgPace() {
    return mAvgPace;
  }

  public void setmAvgPace(double mAvgPace) {
    this.mAvgPace = mAvgPace;
  }

  public double getmAvgSpeed() {
    return mAvgSpeed;
  }

  public void setmAvgSpeed(double mAvgSpeed) {
    this.mAvgSpeed = mAvgSpeed;
  }

  public int getmCalories() {
    return mCalories;
  }

  public void setmCalories(int mCalories) {
    this.mCalories = mCalories;
  }

  public double getmClimb() {
    return mClimb;
  }

  public void setmClimb(double mClimb) {
    this.mClimb = mClimb;
  }

  public int getmHeartRate() {
    return mHeartRate;
  }

  public void setmHeartRate(int mHeartRate) {
    this.mHeartRate = mHeartRate;
  }

  public String getmComment() {
    return mComment;
  }

  public void setmComment(String mComment) {
    this.mComment = mComment;
  }

  public ArrayList<LatLng> getmLocationList() {
    return mLocationList;
  }

  public void setmLocationList(ArrayList<LatLng> mLocationList) {
    this.mLocationList = mLocationList;
  }
}
