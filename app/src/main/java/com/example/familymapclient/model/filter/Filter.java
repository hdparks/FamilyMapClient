package com.example.familymapclient.model.filter;

public class Filter {
    private boolean active;
    private String title;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    private String description;

    public Filter(String title, String description){
        this.title = title;
        this.description = description;
        this.active = true;
    }

    public boolean getActive(){
        return this.active;
    }

    public void setActive(boolean active){
        this.active = active;
    }
}
