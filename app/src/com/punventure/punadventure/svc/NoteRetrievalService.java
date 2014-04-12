package com.punventure.punadventure.svc;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import com.punventure.punadventure.event.NotesEvent;
import com.punventure.punadventure.model.Note;
import com.punventure.punadventure.model.OttoBus;
import com.squareup.otto.Subscribe;

public class NoteRetrievalService extends Service {

    public class NoteRetrievalServiceBinder extends Binder implements ServiceBinder<NoteRetrievalService> {

        @Override
        public NoteRetrievalService getService() {
            return NoteRetrievalService.this;
        }
    }

    private List<Note> allNotes = new ArrayList<Note>();
    
    @Override
    public IBinder onBind(Intent intent) {
        OttoBus.register(this);
        return new NoteRetrievalServiceBinder();
    }

    @Subscribe void onLocationChanged(Location location) {
        new NoteRetrievalTask().execute(location);
    }

    private class NoteRetrievalTask extends AsyncTask<Location, Void, List<Note>> {

        
        @Override
        protected List<Note> doInBackground(Location... params) {
            //params[0]
            //send to server
            Location location = params[0];
            List<Note> notes = new ArrayList<Note>();
            notes.addAll(allNotes);
            return filterNotes(allNotes, location);
        }
        
        @Override
        protected void onPostExecute(List<Note> result) {
            List<Note> newNotes = new ArrayList<Note>();
            //add all the new notes to the list, and then filter by location, and post the results
            allNotes = newNotes;
            OttoBus.publish(new NotesEvent(allNotes));
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
