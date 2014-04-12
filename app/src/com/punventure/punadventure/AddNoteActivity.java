package com.punventure.punadventure;

import roboguice.activity.RoboFragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.punventure.punadventure.event.NoteSavedEvent;
import com.punventure.punadventure.model.Note;
import com.punventure.punadventure.model.OttoBus;
import com.squareup.otto.Subscribe;

public class AddNoteActivity extends RoboFragmentActivity {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_note);

		OttoBus.register(this);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new AddNoteFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Subscribe public void onNoteSaved(NoteSavedEvent event) {
	    finish();
	}
}
