package com.example.familymapclient.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.fragments.PersonFragment;
import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.FamilyMember;
import com.example.familymapclient.model.Person;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PersonActivity extends AppCompatActivity {

    private static final String LOG_TAG = "PersonActivity";
    public static final String EXTRA_PERSON_ID = "person";

    private FamilyMember person;
    private ExpandableListView eventsAndFamily;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        DataCache dataCache = DataCache.getInstance();

        String personID = getIntent().getStringExtra(EXTRA_PERSON_ID);

        Log.d(LOG_TAG,"Person Activity for PersonID "+ personID);
        this.person = dataCache.familyMemberMap.get(personID);
        this.eventsAndFamily = findViewById(R.id.events_and_family);


        //  Set up PersonFragment
        FragmentManager fm = getSupportFragmentManager();

        Fragment personFragment = fm.findFragmentById(R.id.personFragmentLayout);

        //  If this is the first time creating the person fragment,
        //  We pass in a nice lil' bundle.
        if (personFragment == null){

            personFragment = PersonFragment.newInstance(person);
            fm.beginTransaction().add(R.id.personFragmentLayout, personFragment).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }




}
