package com.example.familymapclient;

import android.os.AsyncTask;
import android.util.Log;

import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.Person;

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
            listener.familyDataTaskCompleted(result);
        }
    }

    @Override
    protected String[] doInBackground(Void... voids) {
        HttpClient httpClient = new HttpClient();

        try {
            String persons = httpClient.getUrl(new URLUtils().getPersonURL(), true);
            String events = httpClient.getUrl(new URLUtils().getEventURL(), true);

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
            if (result == null || result[0].isEmpty() || result[1].isEmpty()){ throw new Exception("Invalid response data"); }

            //  Create list of persons, events
            Person[] persons = JSONUtils.JsonToObject(result[0], Person[].class);
            Event[] events = JSONUtils.JsonToObject(result[1], Event[].class);

            FamilyDataParser familyDataParser = new FamilyDataParser();
            familyDataParser.parseFamilyData(persons,events);   //  Throws Excpetion if fails

            fireFamilyDataTaskCompleted(true);

        } catch (Exception ex){

            //  Handle failed call
            fireFamilyDataTaskCompleted(false);

        }

        return;

    }



}
