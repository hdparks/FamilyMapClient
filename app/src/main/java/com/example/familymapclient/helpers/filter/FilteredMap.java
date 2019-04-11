package com.example.familymapclient.helpers.filter;

import com.example.familymapclient.model.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilteredMap {

    private Map<String, Event> eventMap;



    enum Gender {f, m}

    enum Side {Mother, Father, User;}
    private Map<String, Gender> genderMap;

    private Map<String, Side> sideMap;
    private Map<String, String> typeMap;
    private Filter maleFilter;

    private Filter femaleFilter;
    private Filter maternalFilter;
    private Filter paternalFilter;
    private Map<String, Filter> typeToFilterMap;

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

    private void updateFilterMap(Event event){
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
        Gender gender = genderMap.get(eventID);
        switch (gender) {
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
        Side side = sideMap.get(eventID);
        switch (side) {
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

        //  Get filter, ensure valid filter typ
        Filter filter = typeToFilterMap.get(typeMap.get(eventID));
        if (filter == null) return null;

        //  Filter
        boolean filtered = filter.getActive();
        if (filtered) return null;


        //  Retrieve
        return eventMap.get(eventID);


    }

    public List<Event> getFilteredEvents() {

        List<Event> events = new ArrayList<>();

        for (String id: eventMap.keySet()){
            Event e = get(id);
            if (e != null) events.add(e);
        }

        return events;
    }
}

