package com.example.familymapclient;

import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadFamliyDataTask extends AsyncTask<Void, Void, String[]> {

    interface FamilyDataTaskListener {
        void familyDataTaskCompleted(boolean result);
    }

    private final List<FamilyDataTaskListener> listeners = new ArrayList<>();

    void registerListener(FamilyDataTaskListener listener){
        listeners.add(listener);
    }

    private void fireTaskCompleted(boolean result){
        for (FamilyDataTaskListener listener: listeners){
            listener.familyDataTaskCompleted(result);
        }
    }

    @Override
    protected String[] doInBackground(Void... voids) {
        HttpClient httpClient = new HttpClient();


    }

    @Override
    protected void onPostExecute(String result){

        //  Parse results
    }
}
