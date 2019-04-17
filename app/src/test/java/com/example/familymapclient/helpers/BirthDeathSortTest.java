package com.example.familymapclient.helpers;

import android.util.Log;

import com.example.familymapclient.helpers.asynctasks.JSONUtils;
import com.example.familymapclient.helpers.asynctasks.http.httpResponses.EventResponse;
import com.example.familymapclient.model.Event;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class BirthDeathSortTest {

    Logger logger = java.util.logging.Logger.getLogger("BirthDeathSortTest");

    List<Event> events;

    /**
     * Reads in event data to be filtered
     * @throws Exception
     */
    void populateEvents() throws Exception{

        Scanner scEvent = new Scanner(new File("./src/test/java/com/example/familymapclient/helpers/eventsJson.txt"));
        StringBuilder stringBuilder = new StringBuilder();
        while (scEvent.hasNext()){
            stringBuilder.append(scEvent.next());
        }

        String json = stringBuilder.toString();

        EventResponse eRes = JSONUtils.JsonToObject(json,EventResponse.class);
        events = Arrays.asList(eRes.events);
    }

    @Before
    public void setUp() throws Exception{
        populateEvents();
    }


    /**
     * Reads in Event Data for Sheila. Event Data is scrambled, and with "chronologically
     * infeasible" events: time traveling to before her birth, and resurrection after death
     *
     * Makes sure that the BirthDeathSort handles these appropriately, sorting in
     * birth - everything else chronologically, then type by alphabet - death order
     */
    @Test
    public void sortBirthToDeath() throws Exception{
        //  Read in all of Sheila's events, show they are not in correct order
        logger.info("First element:"+ events.get(0).getEventType() + " " +events.get(0).getYear());
        logger.info("Second element:"+ events.get(1).getEventType() + " " +events.get(1).getYear());
        logger.info("Third element:"+ events.get(2).getEventType() + " " + events.get(2).getYear());

        //  Now sort them
        Collections.sort(events,new BirthDeathSort());

        //  Make sure the birth comes first
        assertEquals("birth",events.get(0).getEventType());

        //  Make sure the time travel comes second, even though it comes chronologically before birth
        assertEquals("time_travel",events.get(1).getEventType());

        //  Make sure death comes last
        assertEquals("death",events.get(events.size()-1).getEventType());

        //  Make sure resurrection comes second last, even though happens after death
        assertEquals("resurrection",events.get(events.size()-2).getEventType());

        //  Make sure for two events the same year, is sorted by type name
        //  These correspond to completed_asteroids and COMPLETED_ASTEROIDS, both in 2014
        assert(events.get(events.size()-3).getEventType().compareTo(events.get(events.size()-4).getEventType()) < 0);


    }

}