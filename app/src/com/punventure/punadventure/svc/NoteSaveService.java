package com.punventure.punadventure.svc;

import android.app.Service;
import android.content.Intent;
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
    
    
    @Subscribe public void onSaveNote(SaveNoteEvent event) {
        
        GsonClient client = new GsonClient();
        Note note = event.getNote();
        client.postImage(1, note.getImagePath());
    }
    
}