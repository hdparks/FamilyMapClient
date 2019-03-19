package com.example.familymapclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;




public class LoginFragment extends Fragment {

    public enum Gender{ m,f }

    private String serverAddress;
    private int serverPort;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
