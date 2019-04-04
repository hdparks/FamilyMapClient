package com.example.familymapclient.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.familymapclient.R;
import com.example.familymapclient.model.Event;

public class EventActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT = "event";

    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //  Read the event from the intent
        event = getIntent().getExtras().getParcelable(EXTRA_EVENT);

    }
}
