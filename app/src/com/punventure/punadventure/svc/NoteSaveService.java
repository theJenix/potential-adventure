package com.punventure.punadventure.svc;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import com.punventure.punadventure.event.SaveNoteEvent;
import com.punventure.punadventure.model.Note;
import com.punventure.punadventure.model.OttoBus;
import com.squareup.otto.Subscribe;

public class NoteSaveService extends Service {

    public class NoteSaveServiceBinder extends Binder implements ServiceBinder<NoteSaveService> {

        @Override
        public NoteSaveService getService() {
            return NoteSaveService.this;
        }
    }

    private static final String TAG = NoteSaveService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        OttoBus.register(this);
        return new NoteSaveServiceBinder();
    }
    
    
    @Subscribe public void onSaveNote(final SaveNoteEvent event) {
        
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                GsonClient client = new GsonClient();
                Note note = event.getNote();
                client.post(note);
                if (note.getAudio_path() != null) {
                    client.postAudio(note.getId(), note.getAudio_path());                    
                }
                if (note.getImage_path() != null) {
                    client.postImage(note.getId(), note.getImage_path());                    
                }
                return null;
            }
            
        }.execute();
    }
    
}
