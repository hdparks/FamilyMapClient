package com.example.familymapclient.helpers.parsing;

import com.example.familymapclient.helpers.Logger;
import com.example.familymapclient.model.filter.FilteredMap;
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

        //  Set up dictionary from events to eventIDs
        Map<String, Event> eventIDMap = generateEventIDMap(events);

        //  Find User's Person
        DataCache dataCache = DataCache.getInstance();
        String userPersonID = dataCache.userPersonID;
        if (!familyMemberIDMap.containsKey(userPersonID)) {throw new Exception("User Person not found.");}
        FamilyMember userPerson = familyMemberIDMap.get(userPersonID);

        //  Populate eventMap
        FilteredMap eventMap = new FilteredMap(familyMemberIDMap, eventIDMap, personEventListMap, userPerson);

        //  Assign these values to the cache
        log.d("Assigning to datacache instance");
        dataCache.familyMemberMap = familyMemberIDMap;
        dataCache.eventMap = eventMap;
        dataCache.personEventListMap = personEventListMap;


        dataCache.userPerson = userPerson;

    }

    /**
     * Maps a personID to the corresponding FamilyMember instance
     * @param persons array of Person objects
     * @return a map from personIDs to corresponding FamilyMember instances
     */
    public static Map<String, FamilyMember> generateFamilyMemberIDMap(Person[] persons){
        //  Set up dictionaries from personID to Person
        Map<String, FamilyMember> personMap = new HashMap<>();
        for (Person person : persons){
            FamilyMember familyMember = new FamilyMember(person);
            log.d(familyMember.toString());
            personMap.put(person.getPersonID(), new FamilyMember(person));
        }
        return personMap;
    }

    /**
     * Links the FamilyMember instances together by populating their childID lists
     * @param familyMemberMap the map from personID to Family Members
     */
    public static void establishChildConnections(Map<String, FamilyMember> familyMemberMap){
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

    /**
     * Maps personID's to a list of all that person's corresponding Event instances
     * @param events array of Event objects
     * @return a map from personID to a list of their Events
     */
    public static Map<String, List<String>> generatePersonEventMap(Event[] events){
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

    /**
     * Maps eventID to Event
     * @param events array of Events
     * @return a map from each eventID to corresponding Event
     */
    public static Map<String, Event> generateEventIDMap(Event[] events ) {
        Map<String, Event> eventIDMap = new HashMap<>();
        for (Event event: events){
            eventIDMap.put(event.getEventID(),event);
        }
        return eventIDMap;
    }

}
