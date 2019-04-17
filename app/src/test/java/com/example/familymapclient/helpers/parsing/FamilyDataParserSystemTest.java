package com.example.familymapclient.helpers.parsing;

import com.example.familymapclient.helpers.asynctasks.JSONUtils;
import com.example.familymapclient.helpers.asynctasks.http.httpResponses.EventResponse;
import com.example.familymapclient.helpers.asynctasks.http.httpResponses.PersonResponse;
import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.Person;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class FamilyDataParserSystemTest {
    Logger logger = Logger.getLogger("FamilyDataParserSystemTest");

    Person[] persons;
    Event[] events;


    public void populateFields() throws Exception {

        File personFile = new File("./src/test/java/com/example/familymapclient/helpers/parsing/personJson.txt");
        Scanner scPerson = new Scanner(personFile);
        StringBuilder sBuilder = new StringBuilder();
        while (scPerson.hasNext()){
            sBuilder.append(scPerson.nextLine());
        }
        scPerson.close();
        String personJSON= sBuilder.toString();

        //  Reuse stringBuilder
        sBuilder.setLength(0);
        Scanner scEvent = new Scanner(new File("./src/test/java/com/example/familymapclient/helpers/parsing/eventJson.txt"));
        while (scEvent.hasNext()){
            sBuilder.append(scEvent.nextLine());
        }
        scEvent.close();
        String eventJSON = sBuilder.toString();


        PersonResponse pRes=JSONUtils.JsonToObject(personJSON, PersonResponse.class);
        EventResponse eRes= JSONUtils.JsonToObject(eventJSON,EventResponse.class);

        persons = pRes.data;
        events = eRes.events;

    }

    @Before
    public void setUp() throws Exception {
        if (persons == null || events == null){
            populateFields();
        }
    }

    @Test
    public void establishChildConnectionsTest() {

    }

    @Test
    public void parseFamilyDataTest() {

    }

    @Test
    public void generateEventIDMap() {
        //  Creates a mpa from each eventID to the corresponding event
        Map<String, Event> eventIDMap = FamilyDataParserSystem.generateEventIDMap(events);

        //  Assert that every id was added, and that it matches the corresponding event
        for (String e : eventIDMap.keySet()){
            assertEquals(e,eventIDMap.get(e).getEventID());
        }
    }

    @Test
    public void generatePersonEventMapTest() {
        //  Creates a map from each personID to a list of that person's eventIDs
        Map<String, List<String>> personEventMap = FamilyDataParserSystem.generatePersonEventMap(events);

        //  We expect to see a list of event ids of all Sheila_Parker's events
        List<String> sheilaEvents = personEventMap.get("Sheila_Parker");
        assertNotNull(sheilaEvents);
        assert(sheilaEvents.size() != 0);

        //  Get an EventIDMap as well
        Map<String, Event> eventIDMap = FamilyDataParserSystem.generateEventIDMap(events);

        //  Check that each event belongs to Sheila
        for (String e: sheilaEvents){
            assertEquals("Sheila_Parker",eventIDMap.get(e).getPersonID());
        }
    }
}