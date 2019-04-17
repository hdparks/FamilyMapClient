package com.example.familymapclient.model;

import android.graphics.Color;

import com.example.familymapclient.R;
import com.example.familymapclient.helpers.filter.FilteredMap;

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
    public Map<String, List<String>> personEventListMap;
    public FilteredMap eventMap;

    //  Internal App Settings
    public Settings settings;


    private static DataCache instance;

    public static DataCache getInstance() {
        if( instance == null ) {
            instance = new DataCache();
        }

        return instance;
    }

    private DataCache(){}

    public Settings getSettings(){
        if (settings == null){
            settings = new Settings();
        }
        return settings;
    }

    public static void clearCache(){
        instance = null;
    }


}
