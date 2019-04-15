package com.example.familymapclient.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.example.familymapclient.helpers.Logger;
import com.example.familymapclient.helpers.MapLinesBuilder;
import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.FamilyMember;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.Arrays;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final Logger log = new Logger("MapFragment");
    private static final String ARG_SELECTED_EVENT = "event";
    private GoogleMap map;
    private ImageView genderIcon;
    private TextView personFullName;
    private TextView eventTypeLocationYear;
    private List<Event> eventList;
    private Event currentEvent;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null){
            currentEvent = getArguments().getParcelable(ARG_SELECTED_EVENT);
        }

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

        view.findViewById(R.id.eventData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEvent == null) return;

                //  Intent to a PersonActivity with event.personID
                Intent personIntent = new Intent(getActivity(), PersonActivity.class);
                personIntent.putExtra(PersonActivity.EXTRA_PERSON_ID, currentEvent.getPersonID());
                startActivity(personIntent);
            }
        });


        getEventList();

        return view;
    }

    private void getEventList() {

        this.eventList = DataCache.getInstance().eventMap.getFilteredEvents();

    }


    private void updateMap(){
        //  Clear Map
        map.clear();

        //  Draw events
        for (Event e : eventList){
            addEventMarker(e);
        }

        //  Draw lines
        if (currentEvent != null){

            //  Focus current event
            map.animateCamera(CameraUpdateFactory.newLatLng(
                    new LatLng(Double.valueOf(currentEvent.getLatitude()),Double.valueOf(currentEvent.getLongitude()))));

            MapLinesBuilder linesBuilder = new MapLinesBuilder(currentEvent);
            log.d("Drawing " + linesBuilder.mapLines.size() + " lines");

            for (MapLinesBuilder.MapLine l: linesBuilder.mapLines){
                drawLine(l);
            }
        }
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
                log.d("Intenting to FilterActivity");
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

        LatLng[] points = {        new LatLng(51.5,-0.1), new LatLng(40.7,-74.0)};
        Polyline a = map.addPolyline(new PolylineOptions().addAll(Arrays.asList(points)).width(15).color(Color.RED));

        //  Add listener for map marker
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                log.d("Marker clicked");
                //  Center screen
                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

                //  Set current event to marker tag

                updateCurrentEvent((Event) marker.getTag());
                updateMap();

                return true;
            }
        });

        updateCurrentEvent(currentEvent);
        updateMap();

    }

    private void updateCurrentEvent(Event event){
        log.d("Updating current event");

        //  If null, do nothing
        if (event == null) {
            log.d("Current event is NULL");
            return;
        }

        currentEvent = event;

        //  Get person info, update fields
        FamilyMember person = DataCache.getInstance().familyMemberMap.get(event.getPersonID());
        personFullName.setText(person.getFirstName() + " " + person.getLastName());

        boolean isFemale = person.getGender().equals("f");
        FontAwesomeIcons iconType = isFemale ? FontAwesomeIcons.fa_female : FontAwesomeIcons.fa_male;
        int color = isFemale ? R.color.colorFemale : R.color.colorMale;
        Drawable g = new IconDrawable(getActivity(),iconType).colorRes(color).sizeDp(40);
        genderIcon.setImageDrawable(g);

        this.eventTypeLocationYear.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");

    }

    private void drawLine(MapLinesBuilder.MapLine l) {
        map.addPolyline(new PolylineOptions()
                .add(
                        new LatLng(l.getLat1(),l.getLong1()),
                        new LatLng(l.getLat2(), l.getLong2()))
                .color(0)
                .width(20));
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
        // TODO: 4/15/2019 Color logic here
        return android.R.color.holo_red_dark;
    }

    public void populateMap(){
        DataCache dataCache = DataCache.getInstance();

        //  Pull all events within filters

    }

    public static MapFragment newInstance(Event event){
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_SELECTED_EVENT,event);

        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
