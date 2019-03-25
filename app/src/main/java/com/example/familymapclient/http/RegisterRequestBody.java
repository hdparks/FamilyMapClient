package com.example.familymapclient.http;

public class RegisterRequestBody {

    public String userName;
    public String password;
    public String email;
    public String firstName;
    public String lastName;
    public String gender;

    public RegisterRequestBody(String userName, String password, String email, String firstName, String lastName, String gender) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }
}
