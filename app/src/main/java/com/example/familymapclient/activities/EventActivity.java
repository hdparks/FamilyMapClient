package com.example.familymapclient.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.familymapclient.R;
import com.example.familymapclient.fragments.MapFragment;
import com.example.familymapclient.helpers.Logger;
import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Event;

import java.util.Map;

public class EventActivity extends AppCompatActivity {
    private static final Logger log = new Logger("EventActivity");
    public static final String EXTRA_EVENT_ID = "event_ID";

    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        DataCache dataCache = DataCache.getInstance();

        //  Read the event from the intent
        String eventID = getIntent().getStringExtra(EXTRA_EVENT_ID);
        this.event = dataCache.eventMap.get(eventID);

        //  Create the map fragment
        FragmentManager fm = this.getSupportFragmentManager();
        MapFragment fragment = (MapFragment) fm.findFragmentById(R.id.mapFragmentLayout);
        if ( fragment == null ){
            fragment = MapFragment.newInstance(event);
            fm.beginTransaction().add(R.id.mapFragmentLayout, fragment).commit();
        } else {
            fragment = MapFragment.newInstance(event);
            fm.beginTransaction().replace(R.id.mapFragmentLayout, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            log.d("menu item selected: up button");
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}
