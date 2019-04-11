package com.example.familymapclient.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.familymapclient.R;
import com.example.familymapclient.fragments.FilterFragment;

public class FilterActivity extends AppCompatActivity {
    private static final String LOG_TAG = "FilterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Creating FilterActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //  Set up PersonFragment
        FragmentManager fm = getSupportFragmentManager();

        Fragment filterFragment = fm.findFragmentById(R.id.filterFragmentLayout);

        if (filterFragment == null){

            filterFragment = FilterFragment.newInstance();
            fm.beginTransaction().add(R.id.filterFragmentLayout, filterFragment).commit();
        }
    }
}
