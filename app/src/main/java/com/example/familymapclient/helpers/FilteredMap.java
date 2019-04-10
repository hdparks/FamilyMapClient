package com.example.familymapclient.helpers;

import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilteredMap {

    Map<String, Event> eventMap;

    enum Gender {f, m}
    enum Side {Mother, Father, User}

    Map<String, Gender> genderMap;
    Map<String, Side> sideMap;
    Map<String, String> typeMap;

    Filter maleFilter;
    Filter femaleFilter;
    Filter maternalFilter;
    Filter paternalFilter;

    Map<String, Filter> typeToFilterMap;

    public List<Filter> filterList;


    public FilteredMap() {
        this.eventMap = new HashMap<>();

        this.genderMap = new HashMap<>();
        this.sideMap = new HashMap<>();
        this.typeMap = new HashMap<>();
        this.typeToFilterMap = new HashMap<>();

        filterList = new ArrayList<>();


        femaleFilter = new Filter("Female Events","Filter events based on gender");
        maleFilter = new Filter("Male Events", "Filter events based on gender");
        maternalFilter = new Filter("Mother's Side", "Filter by Mother's side of family");
        paternalFilter =  new Filter("Father's Side","Filter by Father's side of family");

        filterList.add(femaleFilter);
        filterList.add(maleFilter);
        filterList.add(maternalFilter);
        filterList.add(paternalFilter);
    }

    public void putEvent(Event event, boolean isMaternal, boolean isFemale) {
        this.genderMap.put(event.getEventID(), isFemale ? Gender.f : Gender.m);
        this.sideMap.put(event.getEventID(), isMaternal ? Side.Mother : Side.Father);
        this.typeMap.put(event.getEventID(), event.getEventType().toLowerCase());

        //  Update filterList if new filters are out there
        updateFilterMap(event);


        this.eventMap.put(event.getEventID(), event);

    }

    public void updateFilterMap(Event event){
        if (!typeToFilterMap.containsKey(event.getEventType().toLowerCase())){
            String type = event.getEventType().toLowerCase();
            type = type.substring(0,1).toUpperCase() + type.substring(1);

            Filter typeFilter = new Filter(type + " Events","Filter by " + type + " Events");

            typeToFilterMap.put(type.toLowerCase(), typeFilter);
            filterList.add(typeFilter);
        }
    }

    public void putUserEvent(Event event, boolean isFemale){
        this.genderMap.put(event.getEventID(), isFemale ? Gender.f : Gender.m);
        this.sideMap.put(event.getEventID(),Side.User);
        this.typeMap.put(event.getEventID(),event.getEventType());

        updateFilterMap(event);

        this.eventMap.put(event.getEventID(), event);
    }


    public Event get(String eventID) {

        //  Check all filters
        //  Check gender
        switch (genderMap.get(eventID)) {
            case f:
                if (!femaleFilter.getActive()) return null;
                break;

            case m:
                if (!maleFilter.getActive()) return null;
                break;

            default:
                return null;
        }

        //  Check side

        switch (sideMap.get(eventID)) {
            case Father:
                if (!paternalFilter.getActive()) return null;
                break;

            case Mother:
                if (!maternalFilter.getActive()) return null;
                break;

            case User:
                break;

            default:
                return null;
        }

        //  Check type
        if (!typeToFilterMap.get(typeMap.get(eventID)).getActive()) return null;


        //  Retrieve
        return eventMap.get(eventID);


    }
}

