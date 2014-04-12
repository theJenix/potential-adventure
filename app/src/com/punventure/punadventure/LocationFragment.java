package com.punventure.punadventure;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.punventure.punadventure.event.LocationAvailableEvent;
import com.punventure.punadventure.event.LocationEvent;
import com.punventure.punadventure.model.OttoBus;
import com.punventure.punadventure.svc.LocationService;
import com.squareup.otto.Subscribe;

public class LocationFragment extends RoboFragment implements ServiceConnection {

    @InjectView(R.id.text_lat) TextView latView;
    @InjectView(R.id.text_lon) TextView lonView;
    @InjectView(R.id.status_image) ImageView statusImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        OttoBus.register(this);
        return inflater.inflate(R.layout.fragment_location,
                container, false);
    }
    
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().bindService(new Intent(getActivity(), LocationService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    @Subscribe public void onLocationChanged(LocationEvent event) {
        if (this.latView != null) {
            this.latView.setText(formatLatLon(event.getLocation().getLatitude()));
        }
        
        if (this.lonView != null) {
            this.lonView.setText(formatLatLon(event.getLocation().getLongitude()));
        }
    }

    private String formatLatLon(double latlon) {
        String str = Location.convert(latlon,  Location.FORMAT_SECONDS);
        if (str.indexOf(".") >= 0) {
            str = str.substring(0, str.indexOf(".") + 2);
        }
        return str;
    }

    @Subscribe public void onLocationAvailabilityChanged(LocationAvailableEvent event) {
        statusImage.setImageResource(event.isAvailable() ? R.drawable.status_good : R.drawable.status_bad);
    }
}
