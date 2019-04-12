package com.example.familymapclient.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.familymapclient.R;
import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Event;

public class EventActivity extends AppCompatActivity {
    private static final String LOG_TAG = "EventActivity";
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}
