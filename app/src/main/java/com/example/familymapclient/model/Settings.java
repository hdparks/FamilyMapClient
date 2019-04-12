package com.example.familymapclient.model;

/**
 * Created by hdparkin on 4/12/19.
 */

public class Settings {

    private int spouseLineColor;
    private int familyTreeLineColor;
    private int lifeStoryLine;

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

    public int getLifeStoryLine() {
        return lifeStoryLine;
    }

    public void setLifeStoryLine(int lifeStoryLine) {
        this.lifeStoryLine = lifeStoryLine;
    }

    public Settings(int spouseLineColor, int familyTreeLineColor, int lifeStoryLine) {
        this.spouseLineColor = spouseLineColor;
        this.familyTreeLineColor = familyTreeLineColor;
        this.lifeStoryLine = lifeStoryLine;
    }

}
