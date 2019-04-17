package com.example.familymapclient.helpers;

import com.example.familymapclient.helpers.filter.FilteredMap;
import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.FamilyMember;
import com.example.familymapclient.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyDataParserSystem {
    private static Logger log = new Logger("FamilyDataParserSystem");


    public static void parseFamilyData(Person[] persons, Event[] events) throws Exception{

        log.d("PARSING " + persons.length + " Persons, " + events.length + " events");


        //  Set up dictionaries from personID to FamilyMember
        Map<String, FamilyMember> familyMemberIDMap = generateFamilyMemberIDMap(persons);

        //  Assign family relationships
        establishChildConnections(familyMemberIDMap);

        //  Set up dictionary from personID to a list of their events
        Map<String, List<String>> personEventListMap = generatePersonEventMap(events);

        Map<String, Event> eventIDMap = generateEventIDMap(events);

        //  Find User's Person
        DataCache dataCache = DataCache.getInstance();
        String userPersonID = dataCache.userPersonID;
        if (!familyMemberIDMap.containsKey(userPersonID)) {throw new Exception("User Person not found.");}
        FamilyMember userPerson = familyMemberIDMap.get(userPersonID);

        //  Populate eventMap
        FilteredMap eventMap = new FilteredMap();

        populateEventMap(eventMap, familyMemberIDMap, eventIDMap, personEventListMap, userPerson);

        //  Assign these values to the cache
        log.d("Assigning to datacache instance");
        dataCache.familyMemberMap = familyMemberIDMap;
        dataCache.eventMap = eventMap;
        dataCache.personEventListMap = personEventListMap;


        dataCache.userPerson = userPerson;

    }

    private static void populateEventMap(FilteredMap eventMap,
                                         Map<String, FamilyMember> familyMemberIDMap,
                                         Map<String, Event> eventIDMap,
                                         Map<String, List<String>> personEventListMap,
                                         FamilyMember userPerson) {
        log.d("Populating FilteredMap");

        //  Start with the User's events
        boolean isFemale = userPerson.getGender().toLowerCase().equals("f");
        List<String> eventIDs = personEventListMap.get(userPerson.getPersonID());

        if (eventIDs != null){
            log.d("Doing User's events");
            for(String id: eventIDs){
                eventMap.putUserEvent(eventIDMap.get(id), isFemale);
            }
        }

        //  Now move up the line on Mother's side
        if (userPerson.getMotherID() != null){
            log.d("Moving up mother's side");
            FamilyMember mother = familyMemberIDMap.get(userPerson.getMotherID());
            populateMaternalSide(eventMap,familyMemberIDMap,eventIDMap,personEventListMap,mother);
        }

        //  And Father's side
        if (userPerson.getFatherID() != null){
            log.d("Moving up mother's side");
            FamilyMember father = familyMemberIDMap.get(userPerson.getFatherID());
            populatePaternalSide(eventMap,familyMemberIDMap,eventIDMap,personEventListMap,father);
        }
    }

    private static void populateMaternalSide(FilteredMap eventMap,
                                             Map<String, FamilyMember> familyMemberIDMap,
                                             Map<String, Event> eventIDMap,
                                             Map<String, List<String>> personEventListMap,
                                             FamilyMember matPerson){
        boolean isFemale = matPerson.getGender().toLowerCase().equals("f");
        List<String> eventIDs = personEventListMap.get(matPerson.getPersonID());
        if (eventIDs != null){
            for(String id: eventIDs){
                eventMap.putEvent(eventIDMap.get(id), true, isFemale);
            }
        }

        //  Now move up the line on Mother's side
        if (matPerson.getMotherID() != null){
            FamilyMember mother = familyMemberIDMap.get(matPerson.getMotherID());
            populateMaternalSide(eventMap,familyMemberIDMap,eventIDMap,personEventListMap,mother);
        }

        //  And Father's side
        if (matPerson.getFatherID() != null){
            FamilyMember father = familyMemberIDMap.get(matPerson.getFatherID());
            populateMaternalSide(eventMap,familyMemberIDMap,eventIDMap,personEventListMap,father);
        }

    }

    private static void populatePaternalSide(FilteredMap eventMap, Map<String, FamilyMember> familyMemberIDMap, Map<String, Event> eventIDMap, Map<String, List<String>> personEventListMap, FamilyMember patPerson) {
        boolean isFemale = patPerson.getGender().toLowerCase().equals("f");
        List<String> eventIDs = personEventListMap.get(patPerson.getPersonID());

        if (eventIDs != null){
            for(String id: eventIDs){
                eventMap.putEvent(eventIDMap.get(id),false, isFemale);
            }
        }

        //  Now move up the line on Mother's side
        if (patPerson.getMotherID() != null){
            FamilyMember mother = familyMemberIDMap.get(patPerson.getMotherID());
            populateMaternalSide(eventMap,familyMemberIDMap,eventIDMap,personEventListMap,mother);
        }

        //  And Father's side
        if (patPerson.getFatherID() != null){
            FamilyMember father = familyMemberIDMap.get(patPerson.getFatherID());
            populatePaternalSide(eventMap,familyMemberIDMap,eventIDMap,personEventListMap,father);
        }

    }

    private static Map<String, Event> generateEventIDMap(Event[] events ) {
        Map<String, Event> eventIDMap = new HashMap<>();
        for (Event event: events){
            eventIDMap.put(event.getEventID(),event);
        }
        return eventIDMap;
    }

    private static Map<String, FamilyMember> generateFamilyMemberIDMap(Person[] persons){
        //  Set up dictionaries from personID to Person
        Map<String, FamilyMember> personMap = new HashMap<>();
        for (Person person : persons){
            FamilyMember familyMember = new FamilyMember(person);
            log.d(familyMember.toString());
            personMap.put(person.getPersonID(), new FamilyMember(person));
        }
        return personMap;
    }

    private static Map<String, List<String>> generatePersonEventMap(Event[] events){
        //  Set up dictionary from personID to associated eventIDs
        Map<String, List<String>> personEventListMap = new HashMap<>();
        for (Event event: events){
            List<String> eventIDList = personEventListMap.get(event.getPersonID());
            if (eventIDList != null){
                //  Add to the existing list
                eventIDList.add(event.getEventID());

            } else {
                //  Make a new list of events for the personID
                log.d("Assigning eventlist to "+event.getPersonID());
                List<String> eventIDsList = new ArrayList<>();
                eventIDsList.add(event.getEventID());
                personEventListMap.put(event.getPersonID(),eventIDsList);
            }
        }
        return personEventListMap;
    }

    private static void establishChildConnections(Map<String, FamilyMember> familyMemberMap){
        for (FamilyMember child : familyMemberMap.values()){
            //  Add to father's child list
            if (child.getFatherID() != null){
                familyMemberMap.get(child.getFatherID()).getChildrenIDList().add(child.getPersonID());
            } else {
                log.d(child.getPersonID() + " has no father ");

            }
            //  Add to mother's child list
            if (child.getMotherID() != null){
                familyMemberMap.get(child.getMotherID()).getChildrenIDList().add(child.getPersonID());
            } else {
                log.d(child.getPersonID() + " has no mother ");
            }
        }
    }
}
