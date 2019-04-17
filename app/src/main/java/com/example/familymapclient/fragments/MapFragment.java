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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final Logger log = new Logger("MapFragment");
    private static final String ARG_SELECTED_EVENT = "event";
    private GoogleMap map;
    private ImageView genderIcon;
    private TextView personFullName;
    private TextView eventTypeLocationYear;
    private List<Event> eventList;
    private Event currentEvent;
    private boolean menu = true;
    private Map<String, Integer> eventTypeToColor;

    private static int[] colorList = {Color.RED,Color.YELLOW, Color.GREEN,Color.CYAN,Color.MAGENTA,Color.GRAY,Color.LTGRAY,Color.WHITE};

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        eventTypeToColor = new HashMap<>();

        if (getArguments() != null){
            currentEvent = getArguments().getParcelable(ARG_SELECTED_EVENT);
            menu = false;
        }

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater,container,savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        //  Turn on the MAP
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //  Grab event data fields
        this.genderIcon = view.findViewById(R.id.genderIcon);
        this.personFullName = view.findViewById(R.id.personFullName);
        this.eventTypeLocationYear = view.findViewById(R.id.eventTypeLocationYear);

        //  Set click listener for event data fields
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

        //  Pull the list of valid events
        getEventList();

        return view;
    }

    private void getEventList() {

        this.eventList = DataCache.getInstance().eventMap.getFilteredEvents();

    }

    @Override
    public void onResume() {
        super.onResume();
        getEventList();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void updateMap(){
        //  Clear Map
        map.clear();


        //  Draw events
        for (Event e : eventList){
            addEventMarker(e);
        }

        if (currentEvent != null){
            //  Focus current event
            map.animateCamera(CameraUpdateFactory.newLatLng(
                    new LatLng(Double.valueOf(currentEvent.getLatitude()),Double.valueOf(currentEvent.getLongitude()))));


            //  Draw lines
            MapLinesBuilder linesBuilder = new MapLinesBuilder(currentEvent);
            for (MapLinesBuilder.MapLine l: linesBuilder.mapLines){
                drawLine(l);
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //  Make sure we are in a state that needs a menu
        if (!this.menu){ return; }

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
        log.d("On Map Ready called");
        map = googleMap;
        map.setMapType(DataCache.getInstance().getSettings().getMapType());

        //  Add listener for map marker
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
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
        LatLng[] points = {
                new LatLng(l.getLat1(),l.getLong1()),
                new LatLng(l.getLat2(), l.getLong2())
        };


        Polyline polyline = map.addPolyline(new PolylineOptions()
                .add(points)
                .color(l.getColor())
                .width(l.getWidth())
                .zIndex(30)
                .visible(true));
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
        Integer color = eventTypeToColor.get(event.getEventType());
        if (color == null){
            eventTypeToColor.put(event.getEventType(), eventTypeToColor.keySet().size() +1);
        }
        // TODO: 4/16/19 FINISH THIS 
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
