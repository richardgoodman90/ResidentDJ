package com.richardgoodman.residentdj;

//Import Statements
import java.util.Random;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.richardgoodman.residentdj.DeveloperKey;
import info.androidhive.tabsswipe.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class YouTubeFragment extends YouTubePlayerSupportFragment implements OnInitializedListener, PlaybackEventListener, PlayerStateChangeListener {

	//Variable Initialisation
	public YouTubePlayer player;
	Random randomGenerator;
	String state = "";
	String thumb = "";
	String title = "";
	String videoId = "";
	int playTime = 0;
	int randomNumber;
	TextView nowPlayingTextView;

	public static YouTubeFragment newInstance() {
		return new YouTubeFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		randomGenerator = new Random();
		initialize(DeveloperKey.DEVELOPER_KEY, this);
	}

	@Override
	public void onDestroy() {
		if (player != null) {
			player.release();
		}
		
		super.onDestroy();
	}

	@Override
	public void onInitializationSuccess(Provider provider, final YouTubePlayer player, boolean restored) {
  
		this.player = player;
		player.setPlayerStyle(PlayerStyle.MINIMAL);
		
		player.setPlaybackEventListener(new PlaybackEventListener() {
			
			@Override
			public void onBuffering(boolean arg0) {
				state = "buffering";
			}

			@Override
			public void onPaused() {
				state = "paused";
			}

			@Override
			public void onPlaying() {
				state = "playing";
				playTime = player.getCurrentTimeMillis();
			}

			@Override
			public void onSeekTo(int arg0) {
				
			}

			@Override
			public void onStopped() {
				state = "stopped";
			}
	    });
		
		player.setPlayerStateChangeListener(new PlayerStateChangeListener() {

			@Override
			public void onAdStarted() {
				
			}

			@Override
			public void onError(ErrorReason arg0) {
				
			}

			@Override
			public void onLoaded(String arg0) {

			}

			@Override
			public void onLoading() {
				
			}

			@Override
			public void onVideoEnded() {
				if (MainActivity.currentPlaylist.getCount() > 0) {
					title = MainActivity.currentPlaylist.returnVideoTitles().get(0);
					videoId = MainActivity.currentPlaylist.returnVideoIds().get(0);
					thumb = "http://img.youtube.com/vi/" + videoId + "/default.jpg";
					MainActivity.previousPlaylist.addToPlayList(videoId, "YouTube", title, thumb);
				}
				
				if (MainActivity.currentPlaylist.removeFromPlayList()) {
					if (MainActivity.currentPlaylist.getCount() > 0) {
						videoId = MainActivity.currentPlaylist.returnVideoIds().get(0);
						title = MainActivity.currentPlaylist.returnVideoTitles().get(0);
						player.loadVideo(videoId);
						nowPlayingTextView = (TextView) getActivity().findViewById(R.id.nowPlayingTextView);
						nowPlayingTextView.setText("Now Playing: " + title);
						Toast.makeText(getActivity(), "Now Playing: " + title, Toast.LENGTH_SHORT).show();
					}
				}
				if (MainActivity.previousPlaylist.getCount() > 0 && MainActivity.currentPlaylist.getCount() == 0){
					randomNumber = randomGenerator.nextInt(MainActivity.previousPlaylist.getCount());
					videoId = MainActivity.previousPlaylist.returnVideoIds().get(randomNumber);
					title = MainActivity.previousPlaylist.returnVideoTitles().get(randomNumber);
					player.loadVideo(videoId);
					nowPlayingTextView = (TextView) getActivity().findViewById(R.id.nowPlayingTextView);
					nowPlayingTextView.setText("Now Playing: " + title);
					Toast.makeText(getActivity(), "Now Playing: " + title, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onVideoStarted() {

			}
			
		});
		
	}

	@Override
	public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
		this.player = null;
		Toast.makeText(getActivity(), "Failure to initialize YouTube Player", Toast.LENGTH_SHORT).show();
	}
	
	public void playlistUpdated() {
		if (MainActivity.currentPlaylist.getCount() == 1) {
			videoId = MainActivity.currentPlaylist.returnVideoIds().get(0);
			player.loadVideo(videoId);
			Toast.makeText(getActivity(), "Now Playing: " + MainActivity.currentPlaylist.returnVideoTitles().get(0), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void playNewVideo() {
		videoId = MainActivity.currentPlaylist.returnVideoIds().get(0);
		player.loadVideo(videoId);
	}

	@Override
	public void onBuffering(boolean arg0) {

		
	}

	@Override
	public void onPaused() {

		
	}

	@Override
	public void onPlaying() {
		
	}

	@Override
	public void onSeekTo(int arg0) {
		
	}

	@Override
	public void onStopped() {
		
	}

	@Override
	public void onAdStarted() {
		
	}

	@Override
	public void onError(ErrorReason arg0) {

	}

	@Override
	public void onLoaded(String arg0) {
		
	}

	@Override
	public void onLoading() {
		
	}

	@Override
	public void onVideoEnded() {
		
	}

	@Override
	public void onVideoStarted() {
		
	}
}