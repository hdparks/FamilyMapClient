package com.example.familymapclient.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.activities.MainActivity;
import com.example.familymapclient.activities.PersonActivity;
import com.example.familymapclient.model.DataCache;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String LOG_TAG = "MapFragment";
    private GoogleMap map;
    private Button personButton;
    private ImageView genderIcon;
    private TextView personFullName;
    private TextView eventTypeLocationYear;


    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater,container,savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        //  Turn on the MAP
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        personButton = view.findViewById(R.id.quickButton);
        personButton.setOnClickListener(
            new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "Intenting to PersonActivity");

                    DataCache dataCache = DataCache.getInstance();

                    //  Create an intent
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    intent.putExtra(PersonActivity.EXTRA_PERSON_ID, dataCache.userPerson.getFatherID());
                    startActivity(intent);
                }
            }

        );

        this.genderIcon = view.findViewById(R.id.genderIcon);
        this.personFullName = view.findViewById(R.id.personFullName);
        this.eventTypeLocationYear = view.findViewById(R.id.eventTypeLocationYear);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //  Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34,151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.animateCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void populateMap(){
        DataCache dataCache = DataCache.getInstance();

        //  Pull all events within filters

    }
}
