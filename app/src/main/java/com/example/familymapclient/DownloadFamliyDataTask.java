package com.example.familymapclient;

import android.os.AsyncTask;
import android.util.Log;

import com.example.familymapclient.http.EventResponse;
import com.example.familymapclient.http.HttpClient;
import com.example.familymapclient.http.PersonResponse;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class DownloadFamliyDataTask extends AsyncTask<Void, Void, String[]> {

    private static final String LOG_TAG = "DownloadFamilyDataTask";

    interface FamilyDataTaskListener {
        void familyDataTaskCompleted(boolean result);
    }

    private final List<FamilyDataTaskListener> listeners = new ArrayList<>();

    void registerListener(FamilyDataTaskListener listener){
        listeners.add(listener);
    }

    private void fireFamilyDataTaskCompleted(boolean result){
        for (FamilyDataTaskListener listener: listeners){
            Log.d(LOG_TAG, "Family Data Task complete, alerting"+ listener.toString());
            listener.familyDataTaskCompleted(result);
        }
    }

    @Override
    protected String[] doInBackground(Void... voids) {
        HttpClient httpClient = new HttpClient();

        try {
            String persons = httpClient.getUrl(new URLUtils().getPersonURL(), true);
            String events = httpClient.getUrl(new URLUtils().getEventURL(), true);
            Log.d(LOG_TAG, "doInBackground persons response: "+persons);
            Log.d(LOG_TAG, "doInBackground events response: "+events);
            String[] results =  { persons, events };

            return results;

        } catch ( MalformedURLException ex){
            Log.e(LOG_TAG, ex.getMessage(), ex);
        }


        return null;
    }

    @Override
    protected void onPostExecute(String[] result){

        try{

            //  Parse result
            if (result == null || result[0] == null || result[1] == null){ throw new Exception("Invalid response data"); }

            //  Create list of persons, events
            PersonResponse persons = JSONUtils.JsonToObject(result[0], PersonResponse.class);
            EventResponse events = JSONUtils.JsonToObject(result[1], EventResponse.class);

            FamilyDataParserSystem familyDataParser = new FamilyDataParserSystem();
            familyDataParser.parseFamilyData(persons.data,events.events);   //  Throws Excpetion if fails

            fireFamilyDataTaskCompleted(true);

        } catch (Exception ex){
            Log.d(LOG_TAG,ex.getMessage());
            //  Handle failed call
            fireFamilyDataTaskCompleted(false);

        }

        return;

    }



}
