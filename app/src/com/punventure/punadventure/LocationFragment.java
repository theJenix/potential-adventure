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
        getActivity().bindService(new Intent(getActivity(), LocationService.class), this, Context.BIND_AUTO_CREATE);
        return inflater.inflate(R.layout.fragment_location,
                container, false);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
//        this.locationService = ((LocationServiceBinder)service).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
//        this.locationService = null;
    }

    @Subscribe public void onLocationChanged(LocationEvent event) {
        this.latView.setText(Location.convert(event.getLocation().getLatitude(),  Location.FORMAT_SECONDS));
        this.lonView.setText(Location.convert(event.getLocation().getLongitude(), Location.FORMAT_SECONDS));
    }

    @Subscribe public void onLocationAvailabilityChanged(LocationAvailableEvent event) {
        statusImage.setImageResource(event.isAvailable() ? R.drawable.status_good : R.drawable.status_bad);
    }
}
