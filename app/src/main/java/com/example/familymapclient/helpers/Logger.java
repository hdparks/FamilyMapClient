package com.example.familymapclient.helpers;

import android.util.Log;

/**
 * Created by hdparkin on 4/12/19.
 */

public class Logger {
    private String LOG_TAG;

    public Logger(String tag){
        this.LOG_TAG = tag;
    }

    public void d(String m){
        Log.d(LOG_TAG,m);
    }
}
