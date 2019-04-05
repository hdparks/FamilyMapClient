package com.example.familymapclient.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtils {

    public static <T> T JsonToObject(String json, Class<T> tClass){

        return new Gson().fromJson(json, tClass);
    }

    public static <T> String ObjectToJson(T object){

        return new GsonBuilder().setPrettyPrinting().create().toJson(object);
    }

}
