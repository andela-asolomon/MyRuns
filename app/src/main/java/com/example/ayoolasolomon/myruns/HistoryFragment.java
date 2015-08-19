package com.example.ayoolasolomon.myruns;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;


public class HistoryFragment extends Fragment {

  private ExercisesDataSource dataSource;
  private ExerciseEntry mEntry;
  private HistoryCursorAdapter cursorAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    dataSource = new ExercisesDataSource(getActivity());
    mEntry = new ExerciseEntry();
    try {
      dataSource.open();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    View view = inflater.inflate(R.layout.fragment_history, container, false);

    ListView listView = (ListView) view.findViewById(R.id.entries_list);

    try {
      cursorAdapter = new HistoryCursorAdapter(getActivity(), dataSource.fetchEntries(), 0);
      listView.setAdapter(cursorAdapter);
    } catch (Exception e) {
      e.printStackTrace();
    }

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Cursor cursor = (Cursor) parent.getItemAtPosition(position);

        Intent intent = new Intent(getActivity(), EntryDetails.class);
        intent.putExtra("DETAILS", dataSource.fetchEntry(cursor));
        startActivity(intent);
      }
    });


    return view;
  }
}
