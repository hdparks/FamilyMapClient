package com.example.familymapclient;

import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadTask extends AsyncTask<URL, Integer, String> {

    interface DownloadTaskListener {
        void progressUpdated(int progress);
        void taskCompleted(String result);
    }

    /**
     * A list of objects listening to the task
     */
    private final List<DownloadTaskListener> listeners = new ArrayList<>();

    /**
     * Adds a new listener to the listener list
     * @param listener new listener object
     */
    void registerListener(DownloadTaskListener listener) {
        listeners.add(listener);
    }

    /**
     * Sends a progress update to each of the subscribed listeners
     * @param progress an int representing the progress
     */
    private void fireProgressUpdate(int progress){
        for(DownloadTaskListener listener : listeners ){
            listener.progressUpdated(progress);
        }
    }

    /**
     * Sends the result of the task to each listener
     * @param result the length of the resulting task solution
     */
    private void fireTaskCompleted(String result){
        for(DownloadTaskListener listener: listeners){
            listener.taskCompleted(result);
        }
    }


    @Override
    protected String doInBackground(URL... urls) {

        HttpClient httpClient = new HttpClient();

        StringBuilder content = new StringBuilder();

        for (int i = 0; i < urls.length; i++){

            //  Build string
            String urlContent = httpClient.getUrl(urls[i]);
            if (urlContent != null){
                 content.append(urlContent);
            }

            //  Update progress
            int progress;
            if (i == urls.length - 1) {
                progress = 100;
            } else {
                float cur = i + 1;
                float total = urls.length;
                progress = (int) ((cur / total) * 100);
            }

            publishProgress(progress);

        }

        return content.toString();
    }


    @Override
    protected void onProgressUpdate(Integer... progress){
        fireProgressUpdate(progress[0]);
    }

    @Override
    protected  void onPostExecute(String result){
        fireTaskCompleted(result);
    }

}
