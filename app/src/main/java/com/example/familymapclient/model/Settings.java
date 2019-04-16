package com.example.familymapclient.model;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by hdparkin on 4/12/19.
 */

public class Settings {

    public static int[] colorMap = {Color.RED,Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};

    private int spouseLineColor;
    private int familyTreeLineColor;
    private int lifeStoryLineColor;

    private boolean spouseLineOn;
    private boolean familyTreeLineOn;
    private boolean lifeStoryLineOn;

    public enum MapType{Normal,Hybrid,Satellite,Terrain}
    private MapType mapType;


    public int getMapType() {
        switch (mapType){
            case Hybrid: return GoogleMap.MAP_TYPE_HYBRID;
            case Normal: return GoogleMap.MAP_TYPE_NORMAL;
            case Terrain: return GoogleMap.MAP_TYPE_TERRAIN;
            case Satellite: return GoogleMap.MAP_TYPE_SATELLITE;
            default: return 0;
        }
    }

    public int getMapTypePosition(){
        switch (mapType){
            case Normal: return 0;
            case Hybrid: return 1;
            case Satellite: return 2;
            case Terrain: return 3;
            default: return 0;
        }
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    public boolean isLifeStoryLineOn() {
        return lifeStoryLineOn;
    }

    public void setLifeStoryLineOn(boolean lifeStoryLineOn) {
        this.lifeStoryLineOn = lifeStoryLineOn;
    }

    public boolean isFamilyTreeLineOn() {
        return familyTreeLineOn;
    }

    public void setFamilyTreeLineOn(boolean familyTreeLineOn) {
        this.familyTreeLineOn = familyTreeLineOn;
    }

    public boolean isSpouseLineOn() {
        return spouseLineOn;
    }

    public void setSpouseLineOn(boolean spouseLineOn) {
        this.spouseLineOn = spouseLineOn;
    }


    public int getSpouseLineColor() {
        return colorMap[spouseLineColor];
    }

    public int getSpouseColorPosition(){
        return spouseLineColor;
    }

    public void setSpouseLineColor(int spouseLineColor) {
        this.spouseLineColor = spouseLineColor;
    }

    public int getFamilyTreeLineColor() {
        return colorMap[familyTreeLineColor];
    }

    public int getFamilyLinePosition(){
        return familyTreeLineColor;
    }

    public void setFamilyTreeLineColor(int familyTreeLineColor) {
        Log.d("SETTINGS","CHANGING FAMILY TREE COLOR");
        this.familyTreeLineColor = familyTreeLineColor;
    }

    public int getLifeStoryLineColor() {
        return colorMap[lifeStoryLineColor];
    }

    public int getLifeStoryLinePosition(){
        return lifeStoryLineColor;
    }

    public void setLifeStoryLineColor(int lifeStoryLineColor) {
        this.lifeStoryLineColor = lifeStoryLineColor;
    }

    Settings() {
        this.spouseLineColor = 0;
        this.familyTreeLineColor = 1;
        this.lifeStoryLineColor = 2;
        this.spouseLineOn = true;
        this.familyTreeLineOn = true;
        this.lifeStoryLineOn = true;
        this.mapType = MapType.Normal;
    }

}
