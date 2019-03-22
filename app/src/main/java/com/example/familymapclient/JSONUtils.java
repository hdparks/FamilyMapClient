package com.example.familymapclient;

public class JSONUtils {

    public static <T> T JsonToObject(String json, Class<T> tClass){


        return new Gson().fromJson(json, tClass);
    }

}
