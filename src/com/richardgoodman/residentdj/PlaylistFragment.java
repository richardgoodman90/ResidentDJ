package com.richardgoodman.residentdj;

//Import Statements
import info.androidhive.tabsswipe.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlaylistFragment extends Fragment {
	
	//Variable Initialisation
	LazyAdapter adapter, previousAdapter;
	ListView playlistListView, previousPlayedListView;
	TextView listTextView;
	String thumbnailString;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//Inflate the view
		View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);
		
		//Set up the play lists and their empty views
		final ListView playlistListView = (ListView) rootView.findViewById(R.id.playlistListView);
		playlistListView.setEmptyView(rootView.findViewById(R.id.emptyCurrentPlaylistTextView));
		final ListView previousPlayedListView = (ListView) rootView.findViewById(R.id.previousPlaylistListView);
		previousPlayedListView.setEmptyView(rootView.findViewById(R.id.emptyPreviouslyPlayedPlaylistTextView));
		
		adapter = new LazyAdapter(getActivity(), MainActivity.currentPlaylist.returnPlaylist());
        playlistListView.setAdapter(adapter);
		previousAdapter = new LazyAdapter(getActivity(), MainActivity.previousPlaylist.returnPlaylist());
        previousPlayedListView.setAdapter(previousAdapter);
        
        //Set on-click listener for the current play list
        playlistListView.setOnItemClickListener(new ListView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView listTextView = (TextView) parent.getChildAt(position - playlistListView.getFirstVisiblePosition()).findViewById(R.id.listViewText);
	            
	            String[] titleAndID = listTextView.getText().toString().split("\n");
	            final int pos = position;
	            final String title = titleAndID[0];
	            final String videoID = titleAndID[1];
	            
	            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
	            alertDialog.setCancelable(true);
                // Setting Dialog Title
                alertDialog.setTitle("Add item to playlist");
                // Setting Dialog Message
                alertDialog.setMessage("Would like to add " + title + " to the playlist again or remove it?");
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.dj_icon);
                // Setting Positive "Add Again" Button
                alertDialog.setPositiveButton("Add Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                    	dialog.dismiss();
                    	thumbnailString = "http://img.youtube.com/vi/" + videoID + "/default.jpg";
                    	if (MainActivity.currentPlaylist.addToPlayList(videoID, "YouTube", title, thumbnailString)) {
                            Toast.makeText(getActivity(), "Added " + title + " to the Playlist", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                    	}
                    }
                });
                alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                // Setting Negative "Remove" Button
                alertDialog.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (MainActivity.currentPlaylist.removeFromPlayListAtLocation(pos)) {
                        	Toast.makeText(getActivity(), "Removed " + title + " from the Playlist", Toast.LENGTH_SHORT).show();
                        	adapter.notifyDataSetChanged();
                        	if (pos == 0) {
                        		
                        	}
                        }
                    }
                });

                // Showing Alert Message
                alertDialog.show();
		    }
		});
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
		previousAdapter.notifyDataSetChanged();
	}
	
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    // Make sure that we are currently visible
	    if (this.isVisible()) {
	    	adapter.notifyDataSetChanged();
	    	previousAdapter.notifyDataSetChanged();
	    }
	}
}
