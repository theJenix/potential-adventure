package com.punventure.punadventure;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.punventure.punadventure.model.Settings;

public class WelcomeFragment extends RoboFragment {

    @InjectView(R.id.username_label) TextView usernameLabel;
    @InjectView(R.id.name_field) TextView nameView;
    @InjectView(R.id.salutations_field) TextView salutationsView;
    @InjectView(R.id.enter_button) ImageView enterButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container,
                false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Settings settings = Settings.instance();
        if (settings.getName() != null) {
            usernameLabel.setVisibility(View.GONE);
            nameView.setVisibility(View.GONE);
            salutationsView.setVisibility(View.VISIBLE);
            salutationsView.setText("Welcome back, " + settings.getName() + "!");
        }
        
        enterButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickEnterButton();
            }
        }); 
    }

    private void onClickEnterButton() {
        String name = nameView.getText().toString();
        Settings settings = Settings.instance();
        settings.setName(name);
        settings.save();
        startActivity(new Intent(this.getActivity(), NoteListActivity.class));
    }
}
