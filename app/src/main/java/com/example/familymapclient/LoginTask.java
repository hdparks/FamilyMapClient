package com.example.familymapclient;

import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginTask extends AsyncTask<String, Void, String> {

    interface LoginTaskListener {
        void loginTaskCompleted(boolean result);
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
            listener.loginTaskCompleted(result);
        }
    }


    @Override
    protected String doInBackground(String ... strings) {

        // Spin up a new HttpClient object
        HttpClient httpClient = new HttpClient();

        //  Build string
        return httpClient.getUrl();

    }

    @Override
    protected void onPostExecute(String result){

        //  Parse results
        LoginResponseBody response = new JSONUtils().JsonToObject(result, LoginResponseBody.class);

        //  Check to see if call succeeded
        if(response.success){

            //  Store authToken, userName, personID in DataCache
            DataCache dataCache = DataCache.getInstance();
            dataCache.userName = response.username;
            dataCache.authToken = response.authToken;
            dataCache.userPersonID = response.personID;

        }

        fireTaskCompleted(response.success);

    }



}
