package com.example.familymapclient.model;

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
    public FamilyMember userPerson;

    //  Family Data
    public Map<String, FamilyMember> familyMemberMap;
    public Map<String, Event> eventMap;
    public Map<String, List<Event>> personEventListMap;


    private static DataCache instance;

    public static DataCache getInstance() {
        if( instance == null ) {
            instance = new DataCache();
        }

        return instance;
    }

    private DataCache(){}

}
