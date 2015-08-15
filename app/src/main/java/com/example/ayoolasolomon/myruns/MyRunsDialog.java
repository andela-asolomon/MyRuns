package com.example.ayoolasolomon.myruns;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class MyRunsDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

  public static interface OnCompleteListener {
    void onComplete(String time);
  }

  private OnCompleteListener mListener;

  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      this.mListener = (OnCompleteListener)activity;
    }
    catch (final ClassCastException e) {
      throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
    }
  }


  public static MyRunsDialog newInstance(String value) {
    MyRunsDialog fragment = new MyRunsDialog();
    Bundle args = new Bundle();
    args.putString("dialog_type", value);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    String dialogType = getArguments().getString("dialog_type");

    switch (dialogType) {
      case "Date":
        return datePicker();
      case "Time":
        return timePicker();
      case "Duration":
        return durationPicker(dialogType + " (minutes):", true);
      case "Distance":
        return durationPicker(dialogType + " (Miles):", true);
      case "Calories":
        return durationPicker(dialogType + ":", true);
      case "Heart Rate":
        return durationPicker("Average Heart Rate (BPM):", true);
      case "Comment":
        return durationPicker(dialogType + ":", false);
      default:
        return null;
    }

  }

  private Dialog datePicker() {

    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    return new DatePickerDialog(getActivity(), this, year, month, day);
  }

  @Override
  public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    Log.d("Data", "Date: " + year + monthOfYear + dayOfMonth);

    String date = Integer.toString(year) + "/" + Integer.toString(monthOfYear) + "/" + Integer.toString(dayOfMonth);

    mListener.onComplete(date);
  }

  private Dialog timePicker() {
    final Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    return new TimePickerDialog(getActivity(), this, hour, minute, false);
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    String time = Integer.toString(hourOfDay) + ":" + Integer.toString(minute);
    mListener.onComplete(time);
  }

  private Dialog durationPicker(String dialogType, boolean inputType) {

    final EditText editText = new EditText(getActivity());
    editText.setId(R.id.distance_dialog);

    if (inputType) {
      editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    } else {
      editText.setInputType(InputType.TYPE_CLASS_TEXT);
      editText.setLines(3);
      editText.setHint("How did it go? Enter Notes here");
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder
        .setMessage(dialogType)
        .setView(editText)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            String value = editText.getText().toString();

            mListener.onComplete(value);
          }
        })
        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });

    return builder.create();
  }
}
