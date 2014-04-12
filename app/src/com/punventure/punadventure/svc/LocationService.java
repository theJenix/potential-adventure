package com.punventure.punadventure.svc;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.punventure.punadventure.event.LocationAvailableEvent;
import com.punventure.punadventure.event.LocationEvent;
import com.punventure.punadventure.event.RequestLocationEvent;
import com.punventure.punadventure.model.OttoBus;
import com.squareup.otto.Subscribe;

public class LocationService extends Service {

    public class LocationServiceBinder extends Binder implements ServiceBinder<LocationService> {

        public LocationService getService() {
            return LocationService.this;
        };
    }

    private static final long MIN_UPDATE_TIME = 1 * 1000; //milliseconds

    private static final float MIN_UPDATE_DISTANCE = 0;
    
//    private static final long STALE_TIME = 309 

    private LocationManager locationManager;

    private String provider;

    private Location location;

    @Override
    public IBinder onBind(Intent intent) {
        return new LocationServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        OttoBus.register(this);
        startLocationPolling();
    }

    private void startLocationPolling() {
        
        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
//        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        Provider provider = locationManager.getBestProvider(criteria, false);
        
        this.provider = locationManager.getBestProvider(criteria, false);
        this.location = locationManager.getLastKnownLocation(provider);
        boolean enabled = this.locationManager.isProviderEnabled(provider);
        OttoBus.publish(new LocationAvailableEvent(enabled, location != null));
        //publis the last location if it exists
        if (location != null) {
            OttoBus.publish(new LocationEvent(location));
        }

        LocationListener listener = new LocationListener() {
            
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                OttoBus.publish(new LocationAvailableEvent(locationManager.isProviderEnabled(provider), status == LocationProvider.AVAILABLE));
            }
            
            @Override
            public void onProviderEnabled(String provider) {
                OttoBus.publish(new LocationAvailableEvent(true, location != null));
            }
            
            @Override
            public void onProviderDisabled(String provider) {
                OttoBus.publish(new LocationAvailableEvent(false, location != null));
            }
            
            @Override
            public void onLocationChanged(Location l) {
                if (l == null) {
                    Log.wtf("LocationServices", "LOCATION CHANGED TO NULL WTF");
                }

                location = l;
                OttoBus.publish(new LocationEvent(l));
            }
        };
        locationManager.requestLocationUpdates(provider, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, listener, this.getMainLooper());
    }
    
    @Subscribe public void onRequestLocation(RequestLocationEvent event) {
        if (location != null) {
            OttoBus.publish(new LocationEvent(location));
        }
    }

    public Location getCurrentLocation() {
        return this.location;
    }    
}