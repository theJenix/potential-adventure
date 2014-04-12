package com.punventure.punadventure.model;

import com.squareup.otto.Bus;

public class OttoBus {

    private static Bus _instance = new Bus();

    public static void register(Object listener) {
        _instance.register(listener);
    }
    
    public static void publish(Object event) {
        _instance.post(event);
    }
}
