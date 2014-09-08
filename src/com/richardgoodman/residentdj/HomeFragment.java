package com.richardgoodman.residentdj;

//Import Statements
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.androidhive.tabsswipe.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.richardgoodman.residentdj.PlayItem;
import com.richardgoodman.residentdj.Search;

public class HomeFragment extends Fragment{

	//Variable Initialisation
	protected static final int REQUEST_OK = 1;
	String presentSearchTerm, spokenPhrase, thumb, wordList;
	String[] words;
	LazyAdapter adapter;
	List<String> individualWords;
	Button buttonOne, searchAgainButton;
	ListView listView1;
	TextView textView1;
	YouTubeFragment youtubeFragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		//Inflate the view and set the on-click listener for the buttons
		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		final Button buttonOne = (Button) rootView.findViewById(R.id.speakButtonVR);
		final Button searchAgainButton = (Button) rootView.findViewById(R.id.searchAgainButton);
		searchAgainButton.setVisibility(View.GONE);
 
		buttonOne.setOnClickListener(new Button.OnClickListener() {
		    @Override
			public void onClick(View v) {
				youtubeFragment = (YouTubeFragment) getChildFragmentManager().findFragmentById(R.id.youtube_fragment);
		    	startSpeechRecognition();
		    }
		});
		
		searchAgainButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
	            alertDialog.setCancelable(true);
                // Setting Dialog Title
                alertDialog.setTitle("Search Again");
                // Setting Dialog Message
                alertDialog.setMessage("Would you like to search for '" + wordList + "' again?");
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.dj_icon);
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	new CommunicateWithYouTubeTask().execute(wordList);
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                	public void onClick(DialogInterface dialog, int which) {
                		dialog.dismiss();
                	}
                });
                
                alertDialog.show();
			}
		});
		
		youtubeFragment = new YouTubeFragment();
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		transaction.add(R.id.youtube_fragment, youtubeFragment).commit();
				
		return rootView;
	}
	
	//Sets up the language to English British and starts the voice recognition
	public void startSpeechRecognition() {
		Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-GB");
		try {
			startActivityForResult(i, REQUEST_OK);
		} catch (Exception e) {

		}
	}
	
	//If this fragment is visible, play the video.
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    // Make sure that we are currently visible
	    if (this.isVisible()) {
			if (MainActivity.currentPlaylist.getCount() != 0) {
				if (youtubeFragment.videoId != MainActivity.currentPlaylist.returnVideoIds().get(0)) {
					textView1 = (TextView) getView().findViewById(R.id.nowPlayingTextView);
                    textView1.setText("Now Playing: " + MainActivity.currentPlaylist.returnVideoTitles().get(0));
					youtubeFragment.playNewVideo();
				}
			}
	    }
	}
	
	//Asynchronous Task to use the speech terms to search YouTube for a list of results
	class CommunicateWithYouTubeTask extends AsyncTask<String, Void, List<PlayItem>>
	{	
		Dialog searchListDialog;
		
		protected void onPreExecute() {
			searchListDialog = new Dialog(getActivity(), R.style.AppTheme);
			searchListDialog.setContentView(R.layout.dialog);
			searchListDialog.setTitle(presentSearchTerm);
			searchListDialog.setCancelable(true);
			searchListDialog.show();
			
			Button declineButton = (Button) searchListDialog.findViewById(R.id.declineButton);
			declineButton.setOnClickListener(new OnClickListener() {
			    @Override
				public void onClick(View v) {
			    	searchListDialog.dismiss();
			    }
			});
		}
		
		protected void onProgressUpdate(Void...values) {
			searchListDialog.setTitle("Gathering results...");
		}

		protected List<PlayItem> doInBackground(String... params) {
			String youtube = params[0];
			Search youtubeSearch = new Search();
			return youtubeSearch.main(youtube);
		}
		
		protected void onPostExecute(List<PlayItem> result) {
				searchAgainButton = (Button) getView().findViewById(R.id.searchAgainButton);
				searchAgainButton.setVisibility(View.VISIBLE);
				
				searchListDialog.setTitle(presentSearchTerm);
				listView1 = (ListView) searchListDialog.findViewById(R.id.resultsListView);
				adapter = new LazyAdapter(getActivity(), result);
				listView1.setAdapter(adapter);
	        
				listView1.setOnItemClickListener(new ListView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						TextView txt = (TextView) parent.getChildAt(position - listView1.getFirstVisiblePosition()).findViewById(R.id.listViewText);
			            
			            String[] titleAndID = txt.getText().toString().split("\n");
			            final String title = titleAndID[0];
			            final String videoID = titleAndID[1];
			            
			            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
			            alertDialog.setCancelable(true);
		                // Setting Dialog Title
		                alertDialog.setTitle("Add item to playlist");
		                // Setting Dialog Message
		                alertDialog.setMessage("Are you sure you would like to add " + title + " to the playlist?");
		                // Setting Icon to Dialog
		                alertDialog.setIcon(R.drawable.dj_icon);
		                // Setting Positive "Yes" Button
		                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog,int which) {
	                        	thumb = "http://img.youtube.com/vi/" + videoID + "/default.jpg";
	                        	if (MainActivity.currentPlaylist.addToPlayList(videoID, "YouTube", title, thumb)) {
		                            Toast.makeText(getActivity(), "Added to the Playlist", Toast.LENGTH_SHORT).show();
		                            if (MainActivity.currentPlaylist.getCount() == 1) {
			                            textView1 = (TextView) getView().findViewById(R.id.nowPlayingTextView);
			                            textView1.setText("Now Playing: " + title);
		                            }
		                            youtubeFragment = (YouTubeFragment) getChildFragmentManager().findFragmentById(R.id.youtube_fragment);
		                            youtubeFragment.playlistUpdated();
	                        	}
	                        }
	                    });
		                // Setting Negative "NO" Button
		                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int which) {
	                            dialog.cancel();
	                        }
	                    });
	
		                // Showing Alert Message
		                alertDialog.show();
				    }
				});
		}
	}
	
	//OnActivityResult: takes the Speech gained from the user and prepares it for sending to YouTube servers.
	//For example, cutting out the "Search YouTube for" part before the content.
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		youtubeFragment = (YouTubeFragment) getChildFragmentManager().findFragmentById(R.id.youtube_fragment);
		
		presentSearchTerm = "";
		wordList = "";
		
        if (requestCode==REQUEST_OK  && resultCode==-1) {
    		ArrayList <String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    		spokenPhrase = thingsYouSaid.get(0);
    		if (spokenPhrase.length() == 0) {
    			Toast.makeText(getActivity(), "You said nothing!", Toast.LENGTH_SHORT).show();
    		}
    		else {
	    		words = spokenPhrase.split(" ");
	    		individualWords = new ArrayList<String>(Arrays.asList(words));
	    		
	    		if (spokenPhrase.contains("youtube")) {
	    			individualWords.remove("youtube");
	    			presentSearchTerm = "YouTube search results for: ";
	    		}
	    		if (spokenPhrase.contains("search youtube for")) {
	    			individualWords.remove("search");
	    			individualWords.remove("youtube");
	    			individualWords.remove("for");
	    			presentSearchTerm = "YouTube search results for: ";
	    		}
	    		if (spokenPhrase.contains("on youtube")) {
	    			individualWords.remove("on");
	    			individualWords.remove("youtube");
	    			presentSearchTerm = "YouTube search results for: ";
	    		}
	    		if (spokenPhrase.contains("from youtube")) {
	    			individualWords.remove("from");
	    			individualWords.remove("youtube");
	    			presentSearchTerm = "YouTube search results for: ";
	    		}
	    		
	    		if (spokenPhrase.contains("last fm")) {
	    			individualWords.remove("last");
	    			individualWords.remove("fm");
	    			presentSearchTerm = "Last.fm search results for: ";
	    		}
	    		if (spokenPhrase.contains("search last fm for")) {
	    			individualWords.remove("search");
	    			individualWords.remove("last");
	    			individualWords.remove("fm");
	    			individualWords.remove("for");
	    			presentSearchTerm = "Last.fm search results for: ";
	    		}
	    		if (spokenPhrase.contains("on last fm")) {
	    			individualWords.remove("on");
	    			individualWords.remove("last");
	    			individualWords.remove("fm");
	    			presentSearchTerm = "Last.fm search results for: ";
	    		}
	    		if (spokenPhrase.contains("from last fm")) {
	    			individualWords.remove("from");
	    			individualWords.remove("last");
	    			individualWords.remove("fm");
	    			presentSearchTerm = "Last.fm search results for: ";
	    		}
	
	    		if (spokenPhrase.contains("spotify")) {
	    			individualWords.remove("spotify");
	    			presentSearchTerm = "Spotify search results for: ";
	    		}
	    		if (spokenPhrase.contains("search spotify for")) {
	    			individualWords.remove("search");
	    			individualWords.remove("spotify");
	    			individualWords.remove("for");
	    			presentSearchTerm = "Spotify search results for: ";
	    		}
	    		if (spokenPhrase.contains("on spotify")) {
	    			individualWords.remove("on");
	    			individualWords.remove("spotify");
	    			presentSearchTerm = "Spotify search results for: ";
	    		}
	    		if (spokenPhrase.contains("from spotify")) {
	    			individualWords.remove("from");
	    			individualWords.remove("spotify");
	    			presentSearchTerm = "Spotify search results for: ";
	    		}
	    			
	    		for (int x = 0; x < individualWords.size(); x++) {
	    			wordList += individualWords.get(x) + " ";
	    		}
	        
	        
		        if (presentSearchTerm == "") {
		        	presentSearchTerm = "Search results for: ";
		        }
		        
		        presentSearchTerm += wordList;
				
				new CommunicateWithYouTubeTask().execute(wordList);
    		}
	    }
	}
}
