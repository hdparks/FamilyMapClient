package com.example.familymapclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class FamilyMember implements Parcelable {
    String personID;
    String firstName;
    String lastName;
    String gender;
    String fatherID;
    String motherID;
    String spouseID;
    List<String> childrenIDList;


    public List<String> getChildrenIDList() {
        return childrenIDList;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }



    public FamilyMember(Person person){
        this.personID = person.personID;
        this.firstName = person.firstName;
        this.lastName = person.lastName;
        this.gender = person.gender;
        this.fatherID = person.fatherID;
        this.motherID = person.motherID;
        this.spouseID = person.spouseID;
        this.childrenIDList = new ArrayList<>();
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
        dest.writeStringList(childrenIDList);
    }

    public static final Parcelable.Creator<FamilyMember> CREATOR
            = new Parcelable.Creator<FamilyMember>(){
        public FamilyMember createFromParcel(Parcel in) { return new FamilyMember(in);}

        @Override
        public FamilyMember[] newArray(int size){ return new FamilyMember[size];}
    };

    private FamilyMember(Parcel in){
        personID = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        gender  = in.readString();
        fatherID = in.readString();
        motherID = in.readString();
        spouseID = in.readString();
        in.readStringList(childrenIDList);
    }
}
