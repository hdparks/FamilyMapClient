package com.example.familymapclient.model.filter;

import com.example.familymapclient.helpers.Logger;
import com.example.familymapclient.helpers.UnitTestLogger;
import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.FamilyMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilteredMap {
    private static final UnitTestLogger ulog = new UnitTestLogger("FilteredMap");
    private static final Logger log = new Logger("FilteredMap");


    private Map<String, Event> eventMap;



    enum Gender {f, m}
    enum Side {Mother, Father, User}
    private Map<String, Gender> genderMap;

    private Map<String, Side> sideMap;
    private Map<String, String> typeMap;
    private Filter maleFilter;

    private Filter femaleFilter;
    private Filter maternalFilter;
    private Filter paternalFilter;
    public Map<String, Filter> typeToFilterMap;

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

    public void putUserEvent(Event event, boolean isFemale){
        this.genderMap.put(event.getEventID(), isFemale ? Gender.f : Gender.m);
        this.sideMap.put(event.getEventID(),Side.User);
        this.typeMap.put(event.getEventID(),event.getEventType().toLowerCase());

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


    public Event get(String eventID) {

        //  Check all filters
        //  Check gender
        Gender gender = genderMap.get(eventID);
        if (gender == null){
            log.d("SOMETHING REALLY WRONG");
            log.d("Event "+ eventID + " has no gender");
            return null;
        }
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
                if (!paternalFilter.getActive()) {
                    log.d("Father Filter off");
                    return null;
                }
            break;

            case Mother:
                if (!maternalFilter.getActive()) {
                    log.d("Mother Filter off");
                    return null;

                }

                break;

            case User:
                log.d("Getting User Event");
                break;

            default:
                return null;
        }

        //  Get filter, ensure valid filter typ
        Filter filter = typeToFilterMap.get(typeMap.get(eventID));
        if (filter == null) return null;


        //  Filter
        boolean allowed = filter.getActive();
        if (!allowed) return null;


        //  Retrieve
        return this.eventMap.get(eventID);


    }

    public List<Event> getFilteredEvents() {
        log.d("Sorting through "+eventMap.keySet().size()+ " Events");
        List<Event> events = new ArrayList<>();

        for (String id: eventMap.keySet()){
            Event e = get(id);
            if (e != null) events.add(e);
        }

        return events;
    }

    public FilteredMap(Map<String, FamilyMember> familyMemberIDMap, Map<String, Event> eventIDMap,
                       Map<String, List<String>> personEventListMap, FamilyMember userPerson){
        this();

        //  Start with the User's events
        boolean isFemale = userPerson.getGender().toLowerCase().equals("f");
        List<String> eventIDs = personEventListMap.get(userPerson.getPersonID());

        if (eventIDs != null){
            log.d("Doing User's events");
            for(String id: eventIDs){
                putUserEvent(eventIDMap.get(id), isFemale);
            }
        }

        //  Also get their spouse!
        String spouseID = userPerson.getSpouseID();
        if  (spouseID != null){
            boolean spouseIsFemale = familyMemberIDMap.get(userPerson.getSpouseID()).getGender().toLowerCase().equals("f");
            List<String> spouseEventIDs = personEventListMap.get(spouseID);

            log.d("doing spouse's events");
            if (spouseEventIDs != null){
                for(String id: spouseEventIDs){
                    putUserEvent(eventIDMap.get(id),spouseIsFemale);
                }
            }

        }

        //  Now move up the line on Mother's side
        if (userPerson.getMotherID() != null){
            log.d("Moving up mother's side");
            FamilyMember mother = familyMemberIDMap.get(userPerson.getMotherID());
            populateMaternalSide(familyMemberIDMap,eventIDMap,personEventListMap,mother);
        }

        //  And Father's side
        if (userPerson.getFatherID() != null){
            log.d("Moving up mother's side");
            FamilyMember father = familyMemberIDMap.get(userPerson.getFatherID());
            populatePaternalSide(familyMemberIDMap,eventIDMap,personEventListMap,father);
        }

    }

    private void populatePaternalSide(Map<String, FamilyMember> familyMemberIDMap, Map<String, Event> eventIDMap, Map<String, List<String>> personEventListMap, FamilyMember patPerson) {
        boolean isFemale = patPerson.getGender().toLowerCase().equals("f");
        List<String> eventIDs = personEventListMap.get(patPerson.getPersonID());

        if (eventIDs != null){
            for(String id: eventIDs){
                putEvent(eventIDMap.get(id),false, isFemale);
            }
        }

        //  Now move up the line on Mother's side
        if (patPerson.getMotherID() != null){
            FamilyMember mother = familyMemberIDMap.get(patPerson.getMotherID());
            populateMaternalSide(familyMemberIDMap,eventIDMap,personEventListMap,mother);
        }

        //  And Father's side
        if (patPerson.getFatherID() != null){
            FamilyMember father = familyMemberIDMap.get(patPerson.getFatherID());
            populatePaternalSide(familyMemberIDMap,eventIDMap,personEventListMap,father);
        }
    }

    private void populateMaternalSide(Map<String, FamilyMember> familyMemberIDMap, Map<String, Event> eventIDMap, Map<String, List<String>> personEventListMap, FamilyMember matPerson) {
        boolean isFemale = matPerson.getGender().toLowerCase().equals("f");
        List<String> eventIDs = personEventListMap.get(matPerson.getPersonID());
        if (eventIDs != null){
            for(String id: eventIDs){
                putEvent(eventIDMap.get(id), true, isFemale);
            }
        }

        //  Now move up the line on Mother's side
        if (matPerson.getMotherID() != null){
            FamilyMember mother = familyMemberIDMap.get(matPerson.getMotherID());
            populateMaternalSide(familyMemberIDMap,eventIDMap,personEventListMap,mother);
        }

        //  And Father's side
        if (matPerson.getFatherID() != null){
            FamilyMember father = familyMemberIDMap.get(matPerson.getFatherID());
            populateMaternalSide(familyMemberIDMap,eventIDMap,personEventListMap,father);
        }
    }


}

