package com.example.familymapclient;

import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyDataParserSystem {

    // TODO: 3/23/2019 SEPARATE THIS PART DOWN INTO NEW DATA SORT CLASS

    void parseFamilyData(Person[] persons, Event[] events) throws Exception{


        //  Set up dictionaries from personID to Person
        Map<String, Person> personIDMap = generatePersonIDMap(persons);

        //  Set up dictionary from eventID to Event
        Map<String, Event> eventIDMap = generateEventIDMap(events);

        //  Set up dictionary from personID to a list of their events
        Map<String, List<Event>> personEventListMap = generatePersonEventMap(events);

        //  Set up FamilyTree
        











        //  Find User's Person
        DataCache dataCache = DataCache.getInstance();
        String userPersonID = dataCache.userPersonID;
        if (!personIDMap.containsKey(userPersonID)) {throw new Exception("User Person not found.");}
        Person userPerson = personIDMap.get(userPersonID);

        dataCache.userPerson = userPerson;

    }

    private Map<String, Event> generateEventIDMap(Event[] events) {
        Map<String, Event> eventIDMap = new HashMap<>();
        for (Event event: events){
            eventIDMap.put(event.getEventID(),event);
        }
        return eventIDMap;
    }

    Map<String, Person> generatePersonIDMap(Person[] persons){
        //  Set up dictionaries from personID to Person
        Map<String, Person> personMap = new HashMap<>();
        for (Person person : persons){
            personMap.put(person.getPersonID(), person);
        }
        return personMap;
    }

    Map<String, List<Event>> generatePersonEventMap(Event[] events){
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

}
