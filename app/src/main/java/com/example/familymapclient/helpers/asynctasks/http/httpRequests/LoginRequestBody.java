package com.example.familymapclient.helpers.asynctasks.http.httpRequests;

public class LoginRequestBody {
    public String userName;
    public String password;

    public LoginRequestBody(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
