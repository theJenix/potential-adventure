package com.punventure.punadventure;

import java.io.IOException;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

public class VoiceRecordActivity extends RoboActivity {
	
	@InjectView(R.id.record_time_display) Chronometer counter;
//	@InjectView(R.id.start_recording_button) Button startRecordButton;
//	@InjectView(R.id.end_recording_button) Button stopRecordButton;
	
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private static final String fileName = "tempVoiceFile.mp4";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_record);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.voice_record, menu);
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
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_voice_record,
					container, false);
			return rootView;
		}
	}

    private void startRecording() {
    	if (mRecorder == null) {
	        mRecorder = new MediaRecorder();
	        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	        mRecorder.setOutputFile(fileName);
	        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	
	        try {
	            mRecorder.prepare();
	        } catch (IOException e) {
	        	Log.e("AUDIO", "recorder prepare() failed");
	        }
	
	        mRecorder.start();
	        
	        counter.setBase(0);
	        counter.start();
    	}
    }

    private void stopRecording() {
    	if (mRecorder != null) {
    		mRecorder.stop();
    		mRecorder.release();
    		mRecorder = null;

    		counter.stop();
    	}
    }
    
    private void startPlayback() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
        	Log.e("AUDIO", "player prepare() failed");
        }
    }

    private void stopPlaying() {
    	mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }


}
