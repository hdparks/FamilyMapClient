package com.example.familymapclient.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {

    String personID;

    String firstName;

    String lastName;

    String gender;

    String father;

    String mother;

    String spouse;

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

    public String getFather() {
        return father;
    }

    public String getMother() {
        return mother;
    }

    public String getSpouse() {
        return spouse;
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
        dest.writeString(father);
        dest.writeString(mother);
        dest.writeString(spouse);
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
        father = in.readString();
        mother = in.readString();
        spouse = in.readString();
    }

    @Override
    public String toString() {
        return "Person{" +
                "personID='" + personID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", father='" + father + '\'' +
                ", mother='" + mother + '\'' +
                ", spouse='" + spouse + '\'' +
                '}';
    }
}
