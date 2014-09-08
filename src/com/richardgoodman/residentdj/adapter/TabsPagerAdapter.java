package com.richardgoodman.residentdj.adapter;

import com.richardgoodman.residentdj.DevicesFragment;
import com.richardgoodman.residentdj.HomeFragment;
import com.richardgoodman.residentdj.PlaylistFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Playlist fragment activity
			return new PlaylistFragment();
		case 1:
			// Home fragment activity
			return new HomeFragment();
		case 2:
			// Devices fragment activity
			return new DevicesFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
