package com.example.familymapclient;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    private static final String LOG_TAG = "HttpClient";

    public String postUrl(URL url, String JsonBody, boolean includeAuthentication){

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            //  Authentication?
            if (includeAuthentication){
                DataCache dataCache = DataCache.getInstance();
                if (dataCache.authToken == null) { throw new Exception("No Authentication token found."); }
                Log.d(LOG_TAG, "Sending Authroization token: "+dataCache.authToken);
                connection.setRequestProperty("Authorization", dataCache.authToken);
            }

            connection.setDoOutput(true);

            //   Prepare JSON body
            byte[] out = JsonBody.getBytes();

            //  Write body, send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.write(out);
            wr.flush();
            wr.close();


            //  Wait for the response
            if (connection.getResponseCode() == 200){
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                String inline;
                StringBuffer response = new StringBuffer();

                while((inline = in.readLine()) != null){
                    response.append(inline);
                }

                //  Close out the connection
                in.close();

                return response.toString();

            }

        } catch (Exception ex){
            Log.e(LOG_TAG, ex.getMessage(), ex);
        }

        return null;
    }

    public String getUrl(URL url, boolean includeAuthentication){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //  Authentication
            if (includeAuthentication){
                DataCache dataCache = DataCache.getInstance();
                if (dataCache.authToken == null) { throw new Exception("No Authentication token found."); }
                Log.d(LOG_TAG, "Sending Authroization token: "+dataCache.authToken);
                connection.setRequestProperty("Authorization", dataCache.authToken);
            }


            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                String inline;
                StringBuffer response = new StringBuffer();

                while((inline = in.readLine()) != null){
                    response.append(inline);
                }

                //  Close out the connection
                in.close();

                return response.toString();
            } else{
                Log.d(LOG_TAG, "Recieved invalid HTTP Response code: " + connection.getResponseCode());
            }

        } catch (Exception ex){
            Log.e(LOG_TAG, ex.getMessage(), ex);
        }

        return null;
    }
}
