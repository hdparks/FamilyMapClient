package com.example.familymapclient.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {

    String personID;

    String firstName;

    String lastName;

    String gender;

    String fatherID;

    String motherID;

    String spouseID;

    public String getPersonID() {
        return personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(personID);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(gender);
        dest.writeString(fatherID);
        dest.writeString(motherID);
        dest.writeString(spouseID);
    }

    public static final Parcelable.Creator<Person> CREATOR
            = new Parcelable.Creator<Person>(){
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    private Person(Parcel in){
        personID = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        gender  = in.readString();
        fatherID = in.readString();
        motherID = in.readString();
        spouseID = in.readString();
    }
}
