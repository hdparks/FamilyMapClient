package com.example.familymapclient;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class LoginFragment extends Fragment implements LoginTask.LoginTaskListener, DownloadFamliyDataTask.FamilyDataTaskListener {

    private static final String LOG_TAG = "AddressFragment";

    private EditText serverAddressEditText;
    private EditText serverPortEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;

    private RadioGroup genderGroup;

    private Button signInButton;
    private Button registerButton;

    private TextWatcher updateLoginButtons = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            //  Handle Login parser logic
            signInButton.setEnabled(canLogin());
            registerButton.setEnabled(canRegister());
        }
    };

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "in onCreate(Bundle)");
        super.onCreate(savedInstanceState);
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
        genderGroup = view.findViewById(R.id.gender);

        signInButton = view.findViewById(R.id.signIn);
        registerButton = view.findViewById(R.id.register);


        //  Add event handlers
        serverAddressEditText.addTextChangedListener(updateLoginButtons);
        serverPortEditText.addTextChangedListener(updateLoginButtons);
        usernameEditText.addTextChangedListener(updateLoginButtons);
        passwordEditText.addTextChangedListener(updateLoginButtons);
        firstNameEditText.addTextChangedListener(updateLoginButtons);
        lastNameEditText.addTextChangedListener(updateLoginButtons);
        emailEditText.addTextChangedListener(updateLoginButtons);


        //  Sign in and Register handlers
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginTask loginTask = new LoginTask();
                loginTask.registerListener(LoginFragment.this);
                loginTask.execute();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterTask registerTask = new RegisterTask();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public boolean canLogin(){
        return  serverAddressEditText.getText().length() != 0 &&
                serverPortEditText.getText().length() != 0 &&
                usernameEditText.getText().length() != 0 &&
                passwordEditText.getText().length() != 0 ;
    }

    public boolean canRegister(){
        return  serverAddressEditText.getText().length() != 0 &&
                serverPortEditText.getText().length() != 0 &&
                usernameEditText.getText().length() != 0 &&
                passwordEditText.getText().length() != 0 &&
                firstNameEditText.getText().length() != 0 &&
                lastNameEditText.getText().length() != 0 &&
                emailEditText.getText().length() != 0 ;
    }

    @Override
    public void loginTaskCompleted(boolean result) {

        Context context  = getContext();
        CharSequence text = result ? "Log In Successful!" : "Log In Failed!";


        //  Make a quick toast
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();

        //  If successful, query for family data


    }

    @Override
    public void familyDataTaskCompleted(boolean result) {

        DataCache dataCache = DataCache.getInstance();

        Context context = getContext();
        CharSequence text = result ? "Welcome, "+ dataCache.userPerson.getFirstName() + " " +  dataCache.userPerson.getLastName() : "Error Getting Family Data";

        //  Make some toast
        Toast.makeText(context,text,Toast.LENGTH_SHORT)
    }
}
