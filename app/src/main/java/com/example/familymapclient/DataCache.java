package com.example.familymapclient;

import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.Person;

/**
 * Central holding pen for all synced data
 */
public class DataCache {

    public Person userPerson;

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
