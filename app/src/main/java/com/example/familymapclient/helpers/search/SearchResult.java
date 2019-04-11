package com.example.familymapclient.helpers.search;


import android.support.annotation.NonNull;

public class SearchResult {

    public enum IconType{event, male, female}

    private String topString;
    private String botString;
    private IconType iconType;
    private String resourceID;

    public String getTopString() {
        return topString;
    }

    public String getBotString() {
        return botString;
    }

    public IconType getIconType() {
        return iconType;
    }

    public String getResourceID() {
        return resourceID;
    }

    public SearchResult(String topString, String botString, @NonNull IconType iconType, @NonNull String resourceID) {
        this.topString = topString;
        this.botString = botString;
        this.iconType = iconType;
        this.resourceID = resourceID;
    }

}
