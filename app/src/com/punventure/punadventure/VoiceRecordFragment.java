package com.punventure.punadventure;

import java.io.File;
import java.io.IOException;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

public class VoiceRecordFragment extends RoboFragment {

    @InjectView(R.id.record_time_display) Chronometer counter;
    @InjectView(R.id.start_playback) ImageView playbackButton;
    @InjectView(R.id.end_playback) ImageView playbackStopButton;
    @InjectView(R.id.start_recording_button) Button recButton;
    @InjectView(R.id.end_recording_button) Button recStopButton;
    @InjectView(R.id.accept_button) ImageView addVoiceButton;
    @InjectView(R.id.cancel_button) ImageView cancelVoiceButton;

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private String fileName = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_voice_record, container,
                false);

        return rootView;
    }
    
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playbackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startPlayback(view);
            }
        });
        
        playbackStopButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                stopPlayback(view);
            }
            
        });
        
        recButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startRecording(view);
            }
        });
        
        recStopButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                stopRecording(view);
            }
        });
        
        addVoiceButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finalizeRecording(view);
            }
        });
        
        cancelVoiceButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelRecording(view);
            }
        });
    }

    public void startRecording(View v) {
        if (mRecorder == null) {
            try {
                if (fileName != null) {
                    (new File(fileName)).delete();
                }
                fileName = File.createTempFile("voice", ".mp4").getAbsolutePath();
            } catch (IOException e) {
                Log.e("AUDIO", "file preparation failed");
            }
            
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

    public void stopRecording(View v) {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        counter.stop();
    }
    
    public void startPlayback(View v) {
        if (mRecorder == null && fileName != null) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(fileName);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                Log.e("AUDIO", "player prepare() failed");
            }
        }
    }

    public void stopPlayback(View v) {
        if (mRecorder == null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
    
    public void finalizeRecording(View v) {
        Activity act = this.getActivity();
        if (fileName == null) {
            new AlertDialog.Builder(act)
                    .setTitle("No Recording")
                    .setMessage("No recording was made. Either record a message or cancel.")
                    .setCancelable(true)
                    .show();
        } else {
            Intent i = act.getIntent();
            act.setResult(Activity.RESULT_OK, i);
            i.putExtra("fileName", fileName);
            act.finish();
        }
    }
    
    public void cancelRecording(View v) {
        Activity act = this.getActivity();
        Intent i = act.getIntent();
        act.setResult(Activity.RESULT_CANCELED, i);
        act.finish();
    }


}
