package com.punventure.punadventure;

import roboguice.activity.RoboFragmentActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.punventure.punadventure.event.LocationAvailableEvent;
import com.punventure.punadventure.event.NoteSelectedEvent;
import com.punventure.punadventure.model.OttoBus;
import com.punventure.punadventure.svc.NoteRetrievalService;
import com.squareup.otto.Subscribe;

/**
 * An activity representing a list of Notes. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link NoteDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link NoteListFragment} and the item details (if present) is a
 * {@link NoteDetailFragment}.
 * <p>
 * This activity also implements the required {@link NoteListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class NoteListActivity extends RoboFragmentActivity implements ServiceConnection {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        OttoBus.register(this);
        
        Intent service = new Intent(this, NoteRetrievalService.class);
        bindService(service, this, Context.BIND_AUTO_CREATE);
        
        if (findViewById(R.id.note_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((NoteListFragment) getSupportFragmentManager().findFragmentById(
                    R.id.note_list)).setActivateOnItemClick(true);
        }

        
    }
    
    public void startAct(View v) {
        startActivityForResult(new Intent(this, VoiceRecordActivity.class), 1);    	
    }
    
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_CANCELED) {
    		Log.d("Return", "Canceled");
    	} else if (resultCode == RESULT_OK) {
    		Log.d("Return", "Clear");
    		Log.d("Return", data.getStringExtra("fileName"));
    	}
    }

    /**
     * Callback method from {@link NoteListFragment.Callbacks} indicating that
     * the item with the given ID was selected.
     */
    @Subscribe public void onNoteSelected(NoteSelectedEvent event) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(NoteDetailFragment.ARG_ITEM_ID, event.getNote().getId());
            NoteDetailFragment fragment = new NoteDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.note_detail_container, fragment).commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, NoteDetailActivity.class);
            detailIntent.putExtra(NoteDetailFragment.ARG_ITEM_ID, event.getNote().getId());
            startActivity(detailIntent);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
//        if (name.equals(NoteRetrievalService.class.getName())) {
//            
//        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        // TODO Auto-generated method stub
        
    }
    
    @Subscribe public void onLocationAvailabilityChanged(LocationAvailableEvent event) {
        if (!event.isEnabled()) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }
}
