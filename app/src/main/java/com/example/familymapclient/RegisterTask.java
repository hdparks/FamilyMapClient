package com.example.familymapclient;

import android.os.AsyncTask;
import android.util.Log;

import com.example.familymapclient.http.LoginResponseBody;
import com.example.familymapclient.http.RegisterRequestBody;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

class RegisterTask extends AsyncTask<RegisterRequestBody, Void, String> {

    private static final String LOG_TAG = "RegisterTask";

    interface RegisterTaskListener{
        void registerTaskCompleted(boolean result);
    }

    private final List<RegisterTaskListener> listeners = new ArrayList<>();

    void registerListener(RegisterTaskListener listener) { listeners.add(listener); }

    private void fireTaskCompleted(boolean result){
        for (RegisterTaskListener listener: listeners){
            listener.registerTaskCompleted(result);
        }
    }


    @Override
    protected String doInBackground(RegisterRequestBody... body) {

        //  Spin up HttpClient
        HttpClient httpClient = new HttpClient();

        try{

            return httpClient.getUrl(new URLUtils().getRegisterURL(), false);

        } catch (MalformedURLException ex){

            Log.e(LOG_TAG, ex.getMessage(), ex);

        }

        return null;

    }

    @Override
    protected void onPostExecute(String result){

        //  Parse results into register response object
        if (result == null){
            fireTaskCompleted(false);
            return;
        }
        LoginResponseBody response = new JSONUtils().JsonToObject(result, LoginResponseBody.class);

        //  Check for successful response
        if (response.success){
            //  Store authToken, userName, personID in DataCache
            DataCache dataCache = DataCache.getInstance();
            dataCache.userName = response.username;
            dataCache.authToken = response.authToken;
            dataCache.userPersonID = response.personID;
        } else {
            Log.e(LOG_TAG,"Server sent following error: "+response.message);
        }

        fireTaskCompleted(response.success);

    }
}
