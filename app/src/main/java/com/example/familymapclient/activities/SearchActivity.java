package com.example.familymapclient.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.familymapclient.R;
import com.example.familymapclient.fragments.SearchFragment;

public class SearchActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        FragmentManager fm = getSupportFragmentManager();

        SearchFragment searchFragment = (SearchFragment) fm.findFragmentById(R.id.search_fragment);

        if (searchFragment == null){
            searchFragment = new SearchFragment();
            fm.beginTransaction().add(R.id.search_fragment,searchFragment).commit();
        }
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
