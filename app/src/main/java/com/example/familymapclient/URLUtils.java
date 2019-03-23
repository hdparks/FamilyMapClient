package com.example.familymapclient;

import android.provider.ContactsContract;

import java.net.MalformedURLException;
import java.net.URL;

public class URLUtils {

    private static final String LOGIN_PATH = "/user/login";
    private static final String REGISTER_PATH  = "/user/register";
    private static final String PERSON_PATH = "/person";
    private static final String EVENT_PATH = "/event";

    public URL getLoginURL() throws MalformedURLException {

        return addPathToURL(LOGIN_PATH);
    }

    public URL getRegisterURL() throws MalformedURLException {

        return addPathToURL(REGISTER_PATH);
    }

    public URL getPersonURL() throws MalformedURLException {

        return addPathToURL(PERSON_PATH);
    }

    public URL getEventURL() throws MalformedURLException {

        return addPathToURL(EVENT_PATH);
    }

    public URL addPathToURL(String path) throws MalformedURLException {

        DataCache dataCache = DataCache.getInstance();

        return new URL("http://" + dataCache.serverAddress + ":" + dataCache.serverPort + path);
    }

}
