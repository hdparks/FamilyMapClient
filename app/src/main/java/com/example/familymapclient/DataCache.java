package com.example.familymapclient;

import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.Person;

/**
 * Central holding pen for all synced data
 */
public class DataCache {

    //  Server/Login Data
    public String serverAddress;
    public String serverPort;
    public boolean isLoggedIn = false;

    //  User Data
    public String userName;
    public String authToken;
    public String userPersonID;
    public Person userPerson;

    //  Family Data
    public Person[] persons;
    public Event[] events;

    private static DataCache instance;

    public static DataCache getInstance() {
        if( instance == null ) {
            instance = new DataCache();
        }

        return instance;
    }

    private DataCache(){}

    public static void clearCache(){
        instance.events = null;
        instance.persons = null;
    }


}
