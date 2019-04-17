package com.example.familymapclient.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.fragments.LoginFragment;
import com.example.familymapclient.fragments.MapFragment;
import com.example.familymapclient.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {
    private static final String EXTRA_RESYNC = "resync";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());

        setContentView(R.layout.activity_main);

        //  Assert login state
        if (DataCache.getInstance().isLoggedIn){
            //  Add MapFragment if user is logged in
            displayMapFragment();
        } else {
            //  Add LoginFragment if user is not logged in
            displayLoginFragment();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        boolean resync = intent.getBooleanExtra(EXTRA_RESYNC,false);
        if(resync) displayMapFragment();
    }

    private void displayLoginFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.mainFrameLayout);
        if ( fragment == null ){
            fragment = new LoginFragment();
            fm.beginTransaction().add(R.id.mainFrameLayout, fragment).commit();
        } else {
            Fragment loginFragment = new LoginFragment();
            fm.beginTransaction().replace(R.id.mainFrameLayout, loginFragment).commit();
        }
    }

    public void displayMapFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.mainFrameLayout);
        if ( fragment == null ){
            fragment = new MapFragment();
            fm.beginTransaction().add(R.id.mainFrameLayout, fragment).commit();
        } else {
            Fragment mapFragment = new MapFragment();
            fm.beginTransaction().replace(R.id.mainFrameLayout, mapFragment).commit();
        }
    }
}
