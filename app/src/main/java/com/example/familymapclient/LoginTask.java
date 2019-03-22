package com.example.familymapclient;

import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginTask extends AsyncTask<URL, Void, String> {

    interface LoginTaskListener {
        void taskCompleted(boolean result);
    }

    private final List<LoginTaskListener> listeners = new ArrayList<>();

    /**
     * Register a listener to this observable
     * @param listener
     */
    void registerListener(LoginTaskListener listener){
        listeners.add(listener);
    }

    private void fireTaskCompleted(boolean result){
        for (LoginTaskListener listener : listeners ){
            listener.taskCompleted(result);
        }
    }


    @Override
    protected String doInBackground(URL... urls) {

        // Spin up a new HttpClient object
        HttpClient httpClient = new HttpClient();

        //  Build string
        return httpClient.getUrl(urls[0]);

    }

    @Override
    protected void onPostExecute(String result){

        //  Parse results



        fireTaskCompleted(result);

    }



}
