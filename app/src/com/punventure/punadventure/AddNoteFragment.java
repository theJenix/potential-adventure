package com.punventure.punadventure;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.punventure.punadventure.event.SaveNoteEvent;
import com.punventure.punadventure.model.Note;
import com.punventure.punadventure.model.OttoBus;
import com.punventure.punadventure.model.Settings;
import com.punventure.punadventure.svc.LocationService;
import com.punventure.punadventure.svc.LocationService.LocationServiceBinder;

public class AddNoteFragment extends RoboFragment implements ServiceConnection {

    @InjectView(R.id.title_entry) TextView titleView;
    @InjectView(R.id.message_text_entry) TextView textView;
    @InjectView(R.id.latitude_display) TextView latView;
    @InjectView(R.id.longitude_display) TextView lonView;
    @InjectView(R.id.accept_button) ImageView acceptButton;
    @InjectView(R.id.cancel_button) ImageView cancelButton;
    @InjectView(R.id.add_audio_button) ImageView addAudioButton;
    @InjectView(R.id.add_image_button) ImageView addImageButton;
    @InjectView(R.id.private_checkbox) CheckBox privateRadioBox;
    @InjectView(R.id.private_name_field) TextView privateNameView;
    
    private Note newNote;
    private Location location;
    // @InjectView(R.id.)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_note, container,
                false);
        this.newNote = new Note();
        OttoBus.register(this);
        getActivity().bindService(new Intent(getActivity(), LocationService.class), this, Context.BIND_AUTO_CREATE);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        acceptButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                buildAndSaveNote();
            }
        });

        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }

        });
        
        addAudioButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceActivity(v);
            }
        });
        
        addImageButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startCameraActivity(v);
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        LocationService service = ((LocationServiceBinder) binder).getService();
        this.location = service.getCurrentLocation();
        if (this.latView != null && location != null) {
            this.latView.setText((location.getLatitude() >= 0 ? "N" : "S")
                    + formatLatLon(Math.abs(location.getLatitude())));
        }

        if (this.lonView != null && location != null) {
            this.lonView.setText((location.getLongitude() >= 0 ? "E" : "W")
                    + formatLatLon(Math.abs(location.getLongitude())));
        }
    }

    private String formatLatLon(double latlon) {
        String str = Location.convert(latlon, Location.FORMAT_MINUTES);
        if (str.indexOf(".") >= 0) {
            str = str.substring(0, str.indexOf(".") + 4);
        }
        return str.replace(":", " ");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
    
    private void buildAndSaveNote() {
    	if (titleView.getText().toString().equals("") || textView.getText().toString().equals("")
    			|| location == null || Settings.instance().getName().equals("")
    			|| (privateRadioBox.isChecked() && privateNameView.getText().toString().equals(""))) {
    		new AlertDialog.Builder(this.getActivity())
    			.setTitle("Invalid Input")
    			.setMessage("Please check the information in each field.")
    			.setCancelable(true)
    			.show();
    	} else {
	        newNote.setTitle(titleView.getText().toString());
	        newNote.setNote(textView.getText().toString());
	        newNote.setLatitude(location.getLatitude());
	        newNote.setLongitude(location.getLongitude());
	        newNote.setSender(Settings.instance().getName());
	        if (privateRadioBox.isChecked()) {
	            newNote.setRecipient(privateNameView.getText().toString());            
	        } else {
	            newNote.setRecipient("");
	        }
	        newNote.setVisible_range(50); //default 50 meters
	        OttoBus.publish(new SaveNoteEvent(newNote));
    	}
    }

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private static final int CAPTURE_AUDIO_ACTIVITY_REQUEST_CODE = 2;

    
    public void startVoiceActivity(View v) {
        startActivityForResult(new Intent(getActivity(), VoiceRecordActivity.class), CAPTURE_AUDIO_ACTIVITY_REQUEST_CODE);       
    }
    
    Uri cameraFileUri = null;
    public void startCameraActivity(View v) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        cameraFileUri = OutputMedia.getOutputMediaFileUri(OutputMedia.MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("Return", "Canceled");
        } else if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                Log.d("Return", cameraFileUri.toString());
                newNote.setImage_path(cameraFileUri.getPath());
                break;
            case CAPTURE_AUDIO_ACTIVITY_REQUEST_CODE:
                Log.d("Return", "Clear");
                Log.d("Return", data.getStringExtra("fileName"));
                newNote.setAudio_path(data.getStringExtra("fileName"));
                break;
            }
        }
    }
}