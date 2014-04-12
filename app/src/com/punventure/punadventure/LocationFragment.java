package com.punventure.punadventure;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.punventure.punadventure.event.LocationAvailableEvent;
import com.punventure.punadventure.model.OttoBus;
import com.punventure.punadventure.svc.LocationService;
import com.punventure.punadventure.svc.LocationService.LocationServiceBinder;
import com.squareup.otto.Subscribe;

public class LocationFragment extends Fragment implements ServiceConnection {

    private TextView latView;
    private TextView lonView;
    private LocationService locationService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location,
                container, false);
        
        this.latView = (TextView)rootView.findViewById(R.id.text_lat);
        this.lonView = (TextView)rootView.findViewById(R.id.text_lon);

        getActivity().bindService(new Intent(getActivity(), LocationService.class), this, Context.BIND_AUTO_CREATE);
        OttoBus.register(this);
        return rootView;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.locationService = ((LocationServiceBinder)service).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.locationService = null;
    }

    @Subscribe public void onLocationChanged(Location location) {
        this.latView.setText(Location.convert(location.getLatitude(),  Location.FORMAT_SECONDS));
        this.lonView.setText(Location.convert(location.getLongitude(), Location.FORMAT_SECONDS));
    }

    @Subscribe public void onLocationAvailabilityChanged(LocationAvailableEvent available) {
        //TODO: 
    }
}
