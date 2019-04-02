package com.example.familymapclient.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    String eventID;

    String personID;

    String latitude;

    String longitude;

    String country;

    String city;

    String eventType;

    int year;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventID);
        dest.writeString(personID);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(eventType);
        dest.writeInt(year);
    }

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size){
            return new Event[size];
        }
    };

    private Event(Parcel in){
        eventID = in.readString();
        personID = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        country = in.readString();
        city = in.readString();
        eventType = in.readString();
        year = in.readInt();
    }
}
