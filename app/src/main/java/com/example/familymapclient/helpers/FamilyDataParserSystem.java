package com.example.familymapclient.helpers;

import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.FamilyMember;
import com.example.familymapclient.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyDataParserSystem {

    // TODO: 3/23/2019 SEPARATE THIS PART DOWN INTO NEW DATA SORT CLASS

    public static void parseFamilyData(Person[] persons, Event[] events) throws Exception{


        //  Set up dictionaries from personID to FamilyMember
        Map<String, FamilyMember> familyMemberIDMap = generateFamilyMemberIDMap(persons);

        //  Set up dictionary from eventID to Event
        Map<String, Event> eventIDMap = generateEventIDMap(events);

        //  Set up dictionary from personID to a list of their events
        Map<String, List<Event>> personEventListMap = generatePersonEventMap(events);

        //  Assign family relationships
        establishChildConnections(familyMemberIDMap);

        //  Assign these values to the cache
        DataCache dataCache = DataCache.getInstance();

        dataCache.familyMemberMap = familyMemberIDMap;
        dataCache.eventMap = eventIDMap;
        dataCache.personEventListMap = personEventListMap;

        //  Find User's Person
        String userPersonID = dataCache.userPersonID;
        if (!familyMemberIDMap.containsKey(userPersonID)) {throw new Exception("User Person not found.");}
        FamilyMember userPerson = familyMemberIDMap.get(userPersonID);

        dataCache.userPerson = userPerson;

    }

    private static Map<String, Event> generateEventIDMap(Event[] events) {
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
            personMap.put(person.getPersonID(), new FamilyMember(person));
        }
        return personMap;
    }

    private static Map<String, List<Event>> generatePersonEventMap(Event[] events){
        //  Set up dictionary from personID to associated events
        Map<String, List<Event>> personEventListMap = new HashMap<>();
        for (Event event: events){
            if (personEventListMap.containsKey(event.getPersonID())){
                //  Add to the existing list
                personEventListMap.get(event.getPersonID()).add(event);

            } else {
                //  Make a new list of events for the personID
                List<Event> eventsList = new ArrayList<>();
                eventsList.add(event);
                personEventListMap.put(event.getPersonID(),eventsList);
            }
        }
        return personEventListMap;
    }

    private static void establishChildConnections(Map<String, FamilyMember> familyMemberMap){
        for (FamilyMember child : familyMemberMap.values()){
            //  Add to father's child list
            if (child.getFatherID() != null){
                familyMemberMap.get(child.getFatherID()).getChildrenIDList().add(child.getPersonID());
            }
            //  Add to mother's child list
            if (child.getMotherID() != null){
                familyMemberMap.get(child.getMotherID()).getChildrenIDList().add(child.getPersonID());
            }
        }
    }

}
