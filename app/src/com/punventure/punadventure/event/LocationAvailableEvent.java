package com.punventure.punadventure.event;

public class LocationAvailableEvent {

    private boolean available;
    private boolean enabled;

    public LocationAvailableEvent(boolean enabled, boolean available) {
        this.enabled = enabled;
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
}
