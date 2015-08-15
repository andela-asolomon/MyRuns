package com.example.ayoolasolomon.myruns;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.github.machinarius.preferencefragment.PreferenceFragment;


public class SettingsFragment extends Fragment {

  public static PrefsFragment Instantiate() {
    return new PrefsFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

//    getFragmentManager().beginTransaction()
//        .replace(android.R.id.content, new PrefsFragment()).commit();
  }

  public static class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      addPreferencesFromResource(R.xml.preferences);
    }
  }
}
