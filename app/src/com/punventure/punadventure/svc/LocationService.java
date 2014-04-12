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
import android.provider.Settings;

import com.punventure.punadventure.event.LocationAvailableEvent;
import com.punventure.punadventure.event.LocationEvent;
import com.punventure.punadventure.model.OttoBus;

public class LocationService extends Service {

    public class LocationServiceBinder extends Binder implements ServiceBinder<LocationService> {

        public LocationService getService() {
            return LocationService.this;
        };
    }

    private static final long MIN_UPDATE_TIME = 1 * 1000; //milliseconds

    private static final float MIN_UPDATE_DISTANCE = 0;

    private LocationManager locationManager;

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
        
        String provider = locationManager.getBestProvider(criteria, false);
        if (!this.locationManager.isProviderEnabled(provider)) {
            OttoBus.publish(new LocationAvailableEvent(false));
        }
        LocationListener listener = new LocationListener() {
            
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                OttoBus.publish(new LocationAvailableEvent(status == LocationProvider.AVAILABLE));
            }
            
            @Override
            public void onProviderEnabled(String provider) {
                OttoBus.publish(new LocationAvailableEvent(true));
            }
            
            @Override
            public void onProviderDisabled(String provider) {
                OttoBus.publish(new LocationAvailableEvent(false));
            }
            
            @Override
            public void onLocationChanged(Location location) {
                OttoBus.publish(new LocationEvent(location));
            }
        };
        locationManager.requestLocationUpdates(provider, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, listener, this.getMainLooper());
    }
}
