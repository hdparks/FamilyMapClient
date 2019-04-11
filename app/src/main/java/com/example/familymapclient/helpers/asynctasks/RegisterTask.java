package com.example.familymapclient.helpers.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.familymapclient.helpers.asynctasks.http.HttpClient;
import com.example.familymapclient.helpers.asynctasks.http.httpResponses.LoginResponseBody;
import com.example.familymapclient.helpers.asynctasks.http.httpRequests.RegisterRequestBody;
import com.example.familymapclient.model.DataCache;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class RegisterTask extends AsyncTask<RegisterRequestBody, Void, String> {

    private static final String LOG_TAG = "RegisterTask";

    public interface RegisterTaskListener{
        void registerTaskCompleted(boolean result);
    }

    private final List<RegisterTaskListener> listeners = new ArrayList<>();

    public void registerListener(RegisterTaskListener listener) { listeners.add(listener); }

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

            return httpClient.postUrl(new HttpClient.URLUtils().getRegisterURL(), JSONUtils.ObjectToJson(body[0]),false);

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
