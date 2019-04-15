package com.example.familymapclient.model;

/**
 * Created by hdparkin on 4/12/19.
 */

public class Settings {

    private int spouseLineColor;
    private int familyTreeLineColor;
    private int lifeStoryLineColor;

    private boolean spouseLineOn;
    private boolean familyTreeLineOn;
    private boolean lifeStoryLineOn;

    enum MapType{Normal,Hybrid,Satellite,Terrain}
    private MapType mapType;

    public MapType getMapType() {
        return mapType;
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
        return spouseLineColor;
    }

    public void setSpouseLineColor(int spouseLineColor) {
        this.spouseLineColor = spouseLineColor;
    }

    public int getFamilyTreeLineColor() {
        return familyTreeLineColor;
    }

    public void setFamilyTreeLineColor(int familyTreeLineColor) {
        this.familyTreeLineColor = familyTreeLineColor;
    }

    public int getLifeStoryLineColor() {
        return lifeStoryLineColor;
    }

    public void setLifeStoryLineColor(int lifeStoryLineColor) {
        this.lifeStoryLineColor = lifeStoryLineColor;
    }

    Settings(int spouseLineColor, int familyTreeLineColor, int lifeStoryLineColor) {
        this.spouseLineColor = spouseLineColor;
        this.familyTreeLineColor = familyTreeLineColor;
        this.lifeStoryLineColor = lifeStoryLineColor;
        this.spouseLineOn = true;
        this.familyTreeLineOn = true;
        this.lifeStoryLineOn = true;
    }

}
