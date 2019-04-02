package com.example.familymapclient;

import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {

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



    private void displayLoginFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.mainFrameLayout);
        if ( fragment == null ){
            fragment = new LoginFragment();
            fm.beginTransaction().add(R.id.mainFrameLayout, fragment).commit();
        } else {
            Fragment loginFragment = new LoginFragment();
            fm.beginTransaction().replace(R.id.mainFrameLayout, loginFragment);
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
