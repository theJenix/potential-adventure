package com.punventure.punadventure.event;

public class LocationAvailableEvent {

    private boolean available;

    public LocationAvailableEvent(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }
}
