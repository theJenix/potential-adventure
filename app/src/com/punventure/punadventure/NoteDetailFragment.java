package com.punventure.punadventure;

import roboguice.inject.InjectView;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.punventure.punadventure.model.Note;
import com.punventure.punadventure.model.Settings;
import com.punventure.punadventure.svc.NoteRetrievalService;
import com.punventure.punadventure.svc.NoteRetrievalService.NoteRetrievalServiceBinder;

/**
 * A fragment representing a single Note detail screen. This fragment is either
 * contained in a {@link NoteListActivity} in two-pane mode (on tablets) or a
 * {@link NoteDetailActivity} on handsets.
 */
public class NoteDetailFragment extends Fragment implements ServiceConnection {
	
    @InjectView(R.id.play_audio_button) ImageView audioButton;
    @InjectView(R.id.show_photo_button) ImageView photoButton;
    
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private Note note;
    private long mNoteId;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoteDetailFragment() {
        this.note = new Note();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent service = new Intent(this.getActivity(), NoteRetrievalService.class);
        this.getActivity().bindService(service, this, Context.BIND_AUTO_CREATE);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mNoteId = getArguments().getLong(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_detail,
                container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        audioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }); 
        
        photoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }); 
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.note = ((NoteRetrievalServiceBinder)service).getService().findNoteById(mNoteId);
        // Show the dummy content as text in a TextView.
        if (this.note != null) {
            ((TextView) getView().findViewById(R.id.title_display))
                    .setText(note.getTitle());
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }
}
