package com.example.familymapclient.helpers;

import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.Person;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchMap {
    private final Map<String,String> eventMap;
    public Map<String, String> personMap;


    public SearchMap(Person[] persons, Event[] events){
        personMap = new HashMap<>();
        eventMap = new HashMap<>();

        //  Concatenate all relevant person data into one string
        for (Person person : persons){
            personMap.put(getPersonDataString(person), person.getPersonID());
        }

        for (Event event : events){
            eventMap.put(getEventDataString(event),event.getEventID());
        }

    }

    private String getPersonDataString(Person person){
        return person.getFirstName() + " " +person.getLastName();
    }

    private String getEventDataString(Event event){
        return event.getEventType() + " " + event.getCountry()+ " " + event.getCity() + " " +
                event.getYear();
    }

    public class SearchResult {
        public List<Person>
    }
}
