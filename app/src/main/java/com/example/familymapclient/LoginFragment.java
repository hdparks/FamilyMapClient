package com.example.familymapclient;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginFragment extends Fragment implements LoginTask.LoginTaskListener {

    private static final String LOG_TAG = "AddressFragment";

    private EditText serverAddressEditText;
    private EditText serverPortEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;

    private Button signInButton;
    private Button registerButton;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "in onCreate(Bundle)");

        super.onCreate(savedInstanceState);
        //      (Arguments?)
        //      if (getArguments() != null) {}

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.i(LOG_TAG, "in onCreateView(...)");

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //  Connect editText fields to view fields
        serverAddressEditText = view.findViewById(R.id.serverAddress);
        serverPortEditText = view.findViewById(R.id.serverPort);
        usernameEditText = view.findViewById(R.id.userName);
        passwordEditText = view.findViewById(R.id.password);
        firstNameEditText = view.findViewById(R.id.firstName);
        lastNameEditText = view.findViewById(R.id.lastName);
        emailEditText = view.findViewById(R.id.email);

        //  Add event handler

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }



    @Override
    public void loginTaskCompleted(boolean result) {

        Context context  = getContext();
        CharSequence text = result ? "Log In Successful!" : "Log In Failed!";
        int duration = Toast.LENGTH_SHORT;

        //  Make a quick toast
        Toast.makeText(context,text,duration).show();

        //  If successful, query for family data


    }
}
