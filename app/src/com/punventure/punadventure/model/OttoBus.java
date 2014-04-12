package com.punventure.punadventure.model;

import android.os.Handler;

import com.punventure.punadventure.event.RequestNotesEvent;
import com.squareup.otto.Bus;

public class OttoBus {

    private static Bus _instance = new Bus();

    public static void register(Object listener) {
        _instance.register(listener);
    }
    
    public static void publish(Object event) {
        _instance.post(event);
    }

    public static void publishOnMain(Handler handler,
            final Object event) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                publish(event);
            }
        });
    }
}
