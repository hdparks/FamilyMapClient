package com.example.familymapclient.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.familymapclient.R;
import com.example.familymapclient.fragments.FilterFragment;
import com.example.familymapclient.fragments.PersonFragment;

public class FilterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
