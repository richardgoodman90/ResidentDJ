package com.richardgoodman.residentdj;

//Import Statements
import info.androidhive.tabsswipe.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DevicesFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//Set view and inflate it
		View rootView = inflater.inflate(R.layout.fragment_devices, container, false);
		return rootView;
	}

}
