package com.punventure.punadventure;

import java.io.IOException;

import roboguice.activity.RoboFragmentActivity;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SoundPlayActivity extends RoboFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sound_play);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sound_play, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends RoboFragment {
		
	    @InjectView(R.id.play_button) ImageView playButton;
	    @InjectView(R.id.pause_button) ImageView pauseButton;
	    @InjectView(R.id.stop_button) ImageView stopButton;
	    @InjectView(R.id.reset_button) ImageView resetButton;
	    @InjectView(R.id.back_button) ImageView backButton;
	    
	    MediaPlayer mPlayer = null;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_sound_play,
					container, false);
			return rootView;
		}
		
	    public void onViewCreated(View view, Bundle savedInstanceState) {
	        super.onViewCreated(view, savedInstanceState);
	        
	        playButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                try {
	                    if (mPlayer != null && mPlayer.isPlaying()) {
	                        mPlayer.stop();
	                    }
	                    mPlayer = new MediaPlayer();
	                    
	                    mPlayer.setDataSource(getActivity().getIntent().getStringExtra("audioPath"));
	                    mPlayer.prepare();
	                    mPlayer.start();
	                } catch (IOException e) {
	                    Log.e("AUDIO", "player prepare() failed");
	                }
	            }
	        });
	        
	        pauseButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                if (mPlayer != null) {
	                    mPlayer.pause();
	                }
	            }
	        });
	        
	        stopButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                if (mPlayer != null) {
	                    mPlayer.stop();
	                }
	            }
	        });
	        
	        resetButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                if (mPlayer != null) {
	                    mPlayer.reset();
	                }
	            }
	        });
	        
	        backButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                if (mPlayer != null) {
	                    mPlayer.release();
	                }
	            	getActivity().finish();
	            }
	        });
	    }
	}

}
