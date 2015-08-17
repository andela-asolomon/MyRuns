package com.example.ayoolasolomon.myruns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class StartFragment extends Fragment {

  Button mStart;
  Spinner mInputType, mActivityType;
  String inputType, activityType;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_start, container, false);
    setUpUI(view);
    return view;
  }

  private void setUpUI(View view) {

    mStart = (Button) view.findViewById(R.id.startButton);
    mInputType = (Spinner) view.findViewById(R.id.input_type);
    mActivityType = (Spinner) view.findViewById(R.id.activity_type);


    // inputType spinner
    mInputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        inputType = parent.getItemAtPosition(position).toString();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    // activityType Spinner
    mActivityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        activityType = parent.getItemAtPosition(position).toString();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    mStart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        callIntent(inputType, activityType);
      }
    });
  }

  public void callIntent(String inputType, String activityType) {
    if (inputType.equals("Manual")) {
      Intent intent = new Intent(getActivity(), ManualInputActivity.class);
      intent.putExtra("activity", activityType);
      startActivity(intent);
    } else if (inputType.equals("GPS") || inputType.equals("Automatic")) {
      Intent intent = new Intent(getActivity(), MapDisplayActivity.class);
      intent.putExtra("activity", activityType);
      startActivity(intent);
    }
  }
}
