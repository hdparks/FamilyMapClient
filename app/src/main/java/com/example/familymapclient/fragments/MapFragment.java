package com.example.familymapclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.activities.FilterActivity;
import com.example.familymapclient.activities.PersonActivity;
import com.example.familymapclient.activities.SearchActivity;
import com.example.familymapclient.activities.SettingsActivity;
import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String LOG_TAG = "MapFragment";
    private GoogleMap map;
    private ImageView genderIcon;
    private TextView personFullName;
    private TextView eventTypeLocationYear;
    private Event selected;

    private List<Event> eventList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater,container,savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        //  Turn on the MAP
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.genderIcon = view.findViewById(R.id.genderIcon);
        this.personFullName = view.findViewById(R.id.personFullName);
        this.eventTypeLocationYear = view.findViewById(R.id.eventTypeLocationYear);

        getEventList();

        return view;
    }

    private void getEventList() {

        this.eventList = DataCache.getInstance().eventMap.getFilteredEvents();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.full_menu, menu);

        MenuItem filterItem = menu.findItem(R.id.filter_menu_item);
        MenuItem searchItem = menu.findItem(R.id.search_menu_item);
        MenuItem settingsItem = menu.findItem(R.id.settings_menu_item);

        filterItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_filter)
                .colorRes(R.color.colorWhite)
                .actionBarSize());

        searchItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_search)
                .colorRes(R.color.colorWhite)
                .actionBarSize());

        settingsItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear)
                .colorRes(R.color.colorWhite)
                .actionBarSize());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch( item.getItemId() ) {

            case R.id.filter_menu_item:
                Log.d(LOG_TAG, "Intenting to FilterActivity");
                Intent filterIntent = new Intent(getActivity(), FilterActivity.class);
                startActivity(filterIntent);
                return true;

            case R.id.settings_menu_item:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.search_menu_item:
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(searchIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //  Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34,151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.animateCamera(CameraUpdateFactory.newLatLng(sydney));

        for (Event e : eventList){
            addEventMarker(e);
        }
    }

    private void addEventMarker(Event event){

        Marker marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(event.getLatitude()),
                        Double.parseDouble(event.getLongitude())) ));
        marker.setTag(event);

        int color = getColor(event);
//        marker.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).colorRes(color));
    }

    private int getColor(Event event){
        return android.R.color.holo_red_dark;
    }

    public void populateMap(){
        DataCache dataCache = DataCache.getInstance();

        //  Pull all events within filters
        
    }
}
