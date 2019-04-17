package com.example.familymapclient.model.filter;

import com.example.familymapclient.model.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by hdparkin on 4/12/19.
 */
public class FilteredMapTest {


    private FilteredMap filteredMap;
    private Filter[] filters;
    private Event[] events;

    @Before
    public void setup() throws Exception {
        filteredMap = new FilteredMap();

        events = new Event[]{
                new Event("id","pid","lat","long","country","city","eventType",1),
                new Event("id2","pid2","lat2","long2","country2","city2","eventType2",2)
        };

        filters = new Filter[]{
                new Filter("title","description"),
                new Filter("title2", "description2")
        };

    }

    @After
    public void tearDown() throws Exception {
        filteredMap = null;
    }


    /**
     * Tests adding an event to the FilteredMap
     * Makes sure that Filters are created for each new EventType,
     * Makes sure that each event is assigned to a gender and a familySide filter
     *
     * @throws Exception
     */
    @Test
    public void putEvent() throws Exception {
        //  Assert empty filterMap begins with default filters
        assertEquals(4, filteredMap.filterList.size());


        //  Test putting a new event Type
        Event e1 = events[0];
        filteredMap.putEvent(e1, false, false);

        //  Test that a new filter was created and added
        assertEquals(5, filteredMap.filterList.size());

        Event e1_out = filteredMap.get(e1.getEventID());

        //  Test that the id was mapped to the correct object
        assertEquals(e1, e1_out);

    }


    /**
     * Tests adding an event with a special "User" tag that keeps it alive when
     * the Mother's and Father's side filters are on
     *
     * @throws Exception
     */
    @Test
    public void putUserEvent() throws Exception {
        //  Assert empty filterMap begins with default filters
        assertEquals(4, filteredMap.filterList.size());

        //  Test putting a new event Type
        Event e1 = events[0];
        filteredMap.putUserEvent(e1, false);

        //  Assert a new filter was created and added
        assertEquals(5,filteredMap.filterList.size());

        Event e1_out = filteredMap.get(e1.getEventID());

        //  Test that the id was mapped to the correct object
        assertEquals(e1, e1_out);

    }

    /**
     * Makes sure you can get something out of the FilteredMap if it is NOT being filtered
     * (Normal events get passed through normally)
     * @throws Exception if someting goes wrong
     */
    @Test
    public void get() throws Exception {
        //  Put an event in the filterMap, take it back out
        Event e1 = events[0];
        filteredMap.putEvent(e1,false,false);

        //  Take the event back out
        Event e1_out = filteredMap.get(e1.getEventID());
        assertNotNull(e1_out);
        assertEquals(e1,e1_out);

    }

    /**
     * Makes sure that IF an event is filtered, null will be returned by the FilteredMap
     * (Makes sure filters are working)
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void getFilteredReturnsNull() throws Exception {
        //  Put an event in the filterMap, take it back out
        Event e1 = events[0];
        filteredMap.putEvent(e1,false,false);

        //  Turn off the "male" filter (e1 passed in as male in putEvent
        filteredMap.filterList.get(1).setActive(false);

        //  Assert that trying to get a filtered event returns null
        Event e1_filtered = filteredMap.get(e1.getEventID());
        assertNull(e1_filtered);

    }

    /**
     * Makes sure that Filters apply when pulling out ALL viable (not filtered) events
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void getFilteredEvents() throws Exception {



        //  Pull out a list of events from an empty map, ensure nothing is returned
        assertEquals(0,filteredMap.getFilteredEvents().size());


        //  Put a list of events in filterMap
        for (Event event : events){
            filteredMap.putEvent(event, true, true);
        }

        //  Pull out all the events that pass the filters
        List<Event> eventList = filteredMap.getFilteredEvents();

        //  Assert that everything that was put in was taken out
        assertEquals(events.length,eventList.size());


        //  Filter out the top event
        Filter filter = filteredMap.typeToFilterMap.get(events[0].getEventType().toLowerCase());
        assertNotNull(filter);
        filter.setActive(false);

        //  Assert everything but the first event is taken out
        assertEquals(events.length - 1, filteredMap.getFilteredEvents().size());
    }
}