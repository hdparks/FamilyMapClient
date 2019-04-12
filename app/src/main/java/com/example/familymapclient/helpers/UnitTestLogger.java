package com.example.familymapclient.helpers;

/**
 * Created by hdparkin on 4/12/19.
 */

public class UnitTestLogger {

    private String tag;

    public UnitTestLogger(String tag){
        this.tag = tag;
    }

    public void d(String m){
        System.out.println(tag + ": "+ m);
    }

}
