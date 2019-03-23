package com.example.familymapclient;

import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.Person;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FamilyDataParser {

    // TODO: 3/23/2019 SEPARATE THIS PART DOWN INTO NEW DATA SORT CLASS

    void parseFamilyData(Person[] persons, Event[] events) throws Exception{


        //  Set up dictionaries from personID to Person
        Map<String, Person> personMap = new HashMap<>();
        for (Person person : persons){
            personMap.put(person.getPersonID(), person);
        }


        //  Find User's Person
        DataCache dataCache = DataCache.getInstance();
        String userPersonID = dataCache.userPersonID;
        if (!personMap.containsKey(userPersonID)) {throw new Exception("User Person not found.");}
        Person userPerson = personMap.get(userPersonID);

        dataCache.userPerson = userPerson;

//        //  Set up motherSet, fatherSet
//        Set<Person> motherPersons = new HashSet<>();
//        Set<Person> fatherPersons = new HashSet<>();
//
//        // TODO: 3/23/2019 Does userPerson count in mother/father side lists?
//
//        String motherID = userPerson.getMotherID();
//        if (motherID != null && personMap.containsKey(motherID)){
//            Person mother = personMap.get(motherID);
//            motherPersons.add(mother);
//            addParentsToSet(mother, fatherPersons, personMap);
//        }
//
//        String fatherID = userPerson.getFatherID();
//        if (fatherID != null && personMap.containsKey(fatherID)){
//            Person father = personMap.get(fatherID);
//            fatherPersons.add(father);
//            addParentsToSet(father, fatherPersons, personMap);
//        }
//
//        //  Similarly sort the Event data
//        // TODO: 3/23/2019 Sort Event Data (again, does user count in mother/father or not?)
//
//        //  Update cache sets
    }

    void addParentsToSet(Person person, Set<Person> set, Map<String, Person> personMap){
        String fatherID = person.getFatherID();
        String motherID = person.getMotherID();

        if (fatherID != null && personMap.containsKey(fatherID)){
            Person father = personMap.get(fatherID);
            set.add(father);
            addParentsToSet(father, set, personMap);
        }

        if (motherID != null && personMap.containsKey(motherID)){
            Person mother = personMap.get(motherID);
            set.add(mother);
            addParentsToSet(mother, set, personMap);
        }

    }

}
