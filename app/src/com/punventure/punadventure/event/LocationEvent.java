package com.punventure.punadventure.event;

import android.location.Location;

public class LocationEvent {

    private Location location;

    public LocationEvent(Location location) {
        this.location = location;
        // TODO Auto-generated constructor stub
    }
    public Location getLocation() {
        return location;
    }
}
