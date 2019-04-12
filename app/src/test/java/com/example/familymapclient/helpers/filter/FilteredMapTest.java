package com.example.familymapclient.helpers.filter;

import com.example.familymapclient.model.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hdparkin on 4/12/19.
 */
public class FilteredMapTest {


    FilteredMap filteredMap;
    Filter[] filters;
    Event[] events;

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

    @Test
    public void putEvent() throws Exception {
        //  Assert empty filtermap begins with default filters
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

    @Test
    public void putUserEvent() throws Exception {
    }

    @Test
    public void get() throws Exception {
        //  Put an event in the filtermap
        Event e1 = events[0];
        filteredMap.putEvent(e1,false,false);

        Event e1_out = filteredMap.get(e1.getEventID());
        assertNotNull(e1_out);
        assertEquals(e1,e1_out);

    }

    @Test
    public void getFilteredEvents() throws Exception {
    }

}