package com.example.familymapclient.fragments;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.familymapclient.helpers.asynctasks.RegisterTask;
import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.helpers.asynctasks.DownloadFamilyDataTask;
import com.example.familymapclient.helpers.asynctasks.LoginTask;
import com.example.familymapclient.R;
import com.example.familymapclient.activities.MainActivity;
import com.example.familymapclient.helpers.asynctasks.http.httpRequests.LoginRequestBody;
import com.example.familymapclient.helpers.asynctasks.http.httpRequests.RegisterRequestBody;


public class LoginFragment extends Fragment implements LoginTask.LoginTaskListener, RegisterTask.RegisterTaskListener, DownloadFamilyDataTask.DownloadFamilyDataTaskListener {

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

            Log.d(LOG_TAG,"Sign In Button Pressed");

            //  Update port and server data
            DataCache dataCache = DataCache.getInstance();
            dataCache.serverPort = serverPortEditText.getText().toString();
            dataCache.serverAddress = serverAddressEditText.getText().toString();


            LoginRequestBody req = new LoginRequestBody(
                    usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());

            LoginTask loginTask = new LoginTask();
            loginTask.registerListener(LoginFragment.this);
            loginTask.execute(req);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Update port and server data
                DataCache dataCache = DataCache.getInstance();
                dataCache.serverPort = serverPortEditText.getText().toString();
                dataCache.serverAddress = serverAddressEditText.getText().toString();


                RegisterRequestBody req = new RegisterRequestBody(
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(),
                        getGender()
                        );

                RegisterTask registerTask = new RegisterTask();
                registerTask.registerListener(LoginFragment.this);
                registerTask.execute(req);
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

    public String getGender(){
        return genderGroup.getCheckedRadioButtonId() == R.id.male ? "m" : "f";
    }

    @Override
    public void loginTaskCompleted(boolean result) {

        Context context  = getContext();
        CharSequence text = result ? "Log In Successful!" : "Log In Failed!";


        //  Make a quick toast
        Toast toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        toast.show();

        //  If successful, query for family data
        if (result){
            DownloadFamilyDataTask task = new DownloadFamilyDataTask();
            task.registerListener(this);
            task.execute();
        }

    }

    @Override
    public void registerTaskCompleted(boolean result) {

        Context context = getContext();
        CharSequence text = result ? "Registration Successful!": "Registration Failed!";

        //  Make a quick toast
        Toast toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        toast.show();

        //  If successful, query for family data
        if(result){
            DownloadFamilyDataTask task = new DownloadFamilyDataTask();
            task.registerListener(this);
            task.execute();
        }
    }

    @Override
    public void familyDataTaskCompleted(boolean result) {
        Log.d(LOG_TAG,"FamilyDataTask complete!");
        DataCache dataCache = DataCache.getInstance();

        Context context = getContext();
        CharSequence text = result ? "Welcome, "+ dataCache.userPerson.getFirstName() + " " +  dataCache.userPerson.getLastName() : "Error Getting Family Data";

        //  Make some toast
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();

        if (result){
            //  Switch to Map Fragment
            Log.d(LOG_TAG,"Switching from Login Fragment to Map Fragment");
            ((MainActivity) getActivity()).displayMapFragment();
        }

    }
}
