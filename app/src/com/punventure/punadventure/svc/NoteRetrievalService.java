package com.punventure.punadventure.svc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.punventure.punadventure.event.LocationEvent;
import com.punventure.punadventure.event.NotesEvent;
import com.punventure.punadventure.event.RequestLocationEvent;
import com.punventure.punadventure.event.RequestNotesEvent;
import com.punventure.punadventure.model.Note;
import com.punventure.punadventure.model.OttoBus;
import com.punventure.punadventure.notify.NotificationClient;
import com.squareup.otto.Subscribe;

public class NoteRetrievalService extends Service {

    public class NoteRetrievalServiceBinder extends Binder implements ServiceBinder<NoteRetrievalService> {

        @Override
        public NoteRetrievalService getService() {
            return NoteRetrievalService.this;
        }
    }

    private static final String TAG = NoteRetrievalService.class.getSimpleName();
    private boolean notifyDelta = false;
    private List<Note> allNotes = new ArrayList<Note>();
    private Location location;
    
    @Override
    public IBinder onBind(Intent intent) {
        OttoBus.register(this);
        
        //FIXME TEST DATA
//        allNotes.add(new Note("note 1"));
//        allNotes.add(new Note("note 2"));
//        allNotes.add(new Note("note 3"));
//        OttoBus.publish(new NotesEvent(allNotes));

        OttoBus.publish(new RequestLocationEvent());
        return new NoteRetrievalServiceBinder();
    }

    @Subscribe public void onLocationChanged(LocationEvent event) {
        this.location = event.getLocation();
        new NoteRetrievalTask().execute(this.location);
    }

    @Subscribe public void onRequestNotes(RequestNotesEvent event) {
        new NoteRetrievalTask().execute(this.location);
    }

    private class NoteRetrievalTask extends AsyncTask<Location, Void, List<Note>> {

        @Override
        protected List<Note> doInBackground(Location... aparams) {
            List<Note> notes;
            Location location = aparams[0];
            try {
                GsonClient client = new GsonClient();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("lat", location.getLatitude());
                params.put("lon", location.getLongitude());
                notes = new ArrayList<Note>(client.list(Note.class, params));
            } catch (IOException e) {
                Log.wtf(TAG, e);
                //if we get an io exception, just reuse the existing notes
                notes = allNotes; //new ArrayList<Note>();
            }
            
            return notes; //filterNotes(notes, location);
        }
        
        @Override
        protected void onPostExecute(List<Note> result) {
            if (notifyDelta && isDelta(result, allNotes)) {
                new NotificationClient(NoteRetrievalService.this).notifyNewNotes();
            }
            //add all the new notes to the list, and then filter by location, and post the results
            allNotes = result;
            OttoBus.publish(new NotesEvent(allNotes));
            //all new changes, notify
            notifyDelta = true;
        }

        private boolean isDelta(List<Note> result, List<Note> allNotes) {
            List<Note> intermediate = new ArrayList<Note>(result);
            intermediate.removeAll(allNotes);
            return result.size() != allNotes.size() || !intermediate.isEmpty();
        }
        
    }
    private List<Note> filterNotes(List<Note> notes, Location location) {
        List<Note> filtered = new ArrayList<Note>();
        
        for (Note note : notes) {
            if (withinRange(note, location)) {
                filtered.add(note);
            }
        }
        
        return filtered;
    }

    private boolean withinRange(Note note, Location location) {
        
        //TODO:
        return true;
    }

    public Note findNoteById(long noteId) {
        Note found = null;
        for (Note note : this.allNotes) {
            if (note.getId() == noteId) {
                found = note;
                break;
            }
        }
        return found;
    }
}
