package com.example.ayoolasolomon.myruns;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ayoolasolomon on 8/13/15.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

  int mNumOfTabs;

  public PagerAdapter(FragmentManager fm, int mNumOfTabs) {
    super(fm);
    this.mNumOfTabs = mNumOfTabs;
  }


  @Override
  public Fragment getItem(int position) {

    switch (position) {
      case 0:
        StartFragment startFragment = new StartFragment();
        return startFragment;
      case 1:
        HistoryFragment historyFragment = new HistoryFragment();
        return historyFragment;
      case 2:
        return SettingsFragment.Instantiate();
      default:
        return null;
    }
  }

  @Override
  public int getCount() {
    return mNumOfTabs;
  }
}
