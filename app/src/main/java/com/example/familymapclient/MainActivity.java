package com.example.familymapclient;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //  Add LoginFragment if user is not logged in
        createLoginFragment();
        //  Add MapFragment if user is logged in
    }

    private void createLoginFragment() {
        FragmentManager fm = this.getSupportFragmentManager();
        LoginFragment loginFragment = (LoginFragment) fm.findFragmentById(R.id.mainFrameLayout);
        if ( loginFragment == null ){
            loginFragment = LoginFragment.newInstance();
            fm.beginTransaction().add(R.id.mainFrameLayout, loginFragment).commit();
        }
    }
}
