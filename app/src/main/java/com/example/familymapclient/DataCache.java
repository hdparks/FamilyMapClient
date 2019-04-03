package com.example.familymapclient;

import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Person> persons;
    public List<Event> events;
    public Map<String, Person> personIDMap;
    public Map<String, List<Event>> personEventListMap;


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
