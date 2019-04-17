package com.example.familymapclient.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {

    // ONLY USED IN TESTING. No Event should ever be constructed outside of FamilyDataParser/JSON utility functions
    public Event(String eventID, String personID, String latitude, String longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }


    public String getPersonID() {
        return personID;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (year != event.year) return false;
        if (!eventID.equals(event.eventID)) return false;
        if (!personID.equals(event.personID)) return false;
        if (!latitude.equals(event.latitude)) return false;
        if (!longitude.equals(event.longitude)) return false;
        if (!country.equals(event.country)) return false;
        if (!city.equals(event.city)) return false;
        return eventType.equals(event.eventType);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID='" + eventID + '\'' +
                ", personID='" + personID + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", eventType='" + eventType + '\'' +
                ", year=" + year +
                '}';
    }

    @Override
    public int hashCode() {
        int result = eventID.hashCode();
        result = 31 * result + personID.hashCode();
        result = 31 * result + latitude.hashCode();
        result = 31 * result + longitude.hashCode();
        result = 31 * result + country.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + eventType.hashCode();
        result = 31 * result + year;
        return result;
    }
}
