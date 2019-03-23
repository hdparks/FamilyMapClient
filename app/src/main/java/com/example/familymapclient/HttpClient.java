package com.example.familymapclient;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    private static final String LOG_TAG = "HttpClient";

    public String postUrl(URL url, String JsonBody){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            //   Prepare JSON body
            byte[] out = JsonBody.getBytes();
            int len = out.length;
            


        } catch (Exception ex){
            Log.e(LOG_TAG, ex.getMessage(), ex);
        }
    }

    public String getUrl(URL url, boolean includeAuthentication){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //  Get response from input stream
                InputStream responseBody = connection.getInputStream();

                //  Read response body bytes
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while((length = responseBody.read(buffer)) != -1) {
                    outputStream.write(buffer,0,length);
                }

                return outputStream.toString();
            }

        } catch (Exception ex){
            Log.e(LOG_TAG, ex.getMessage(), ex);
        }

        return null;
    }
}
