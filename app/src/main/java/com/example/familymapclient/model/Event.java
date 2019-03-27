package com.example.familymapclient.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {

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
