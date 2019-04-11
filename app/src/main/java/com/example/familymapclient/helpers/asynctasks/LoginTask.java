package com.example.familymapclient.helpers.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.familymapclient.helpers.asynctasks.http.HttpClient;
import com.example.familymapclient.helpers.asynctasks.http.httpRequests.LoginRequestBody;
import com.example.familymapclient.helpers.asynctasks.http.httpResponses.LoginResponseBody;
import com.example.familymapclient.model.DataCache;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class LoginTask extends AsyncTask<LoginRequestBody, Void, String> {

    private static final String LOG_TAG = "LoginTask";

    public interface LoginTaskListener {
        void loginTaskCompleted(boolean result);
    }

    private final List<LoginTaskListener> listeners = new ArrayList<>();

    /**
     * Register a listener to this observable
     * @param listener
     */
    public void registerListener(LoginTaskListener listener){
        listeners.add(listener);
    }

    private void fireTaskCompleted(boolean result){
        for (LoginTaskListener listener : listeners ){
            listener.loginTaskCompleted(result);
        }
    }


    @Override
    protected String doInBackground(LoginRequestBody... req) {

        // Spin up a new HttpClient object
        HttpClient httpClient = new HttpClient();

        try{

            return httpClient.postUrl(new HttpClient.URLUtils().getLoginURL(), JSONUtils.ObjectToJson(req[0]),false);

        } catch (MalformedURLException ex){

            Log.e(LOG_TAG, ex.getMessage(), ex);

        }

        //  If something goes wrong, return null
        return null;
    }

    @Override
    protected void onPostExecute(String result){
        Log.d(LOG_TAG,"onPostExecute recieved " + result);
        //  Parse results
        if (result == null){
            fireTaskCompleted(false);
            return;
        }
        LoginResponseBody response = new JSONUtils().JsonToObject(result, LoginResponseBody.class);


        //  Check to see if call succeeded
        if(response.success){

            //  Store authToken, userName, personID in DataCache
            DataCache dataCache = DataCache.getInstance();
            dataCache.userName = response.username;
            dataCache.authToken = response.authToken;
            dataCache.userPersonID = response.personID;
            dataCache.isLoggedIn = true;

        } else {
            Log.e(LOG_TAG, "Server sent following error: "+response.message);
        }

        fireTaskCompleted(response.success);

    }



}
