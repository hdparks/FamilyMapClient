package com.example.familymapclient.helpers;

import com.example.familymapclient.helpers.filter.FilteredMap;
import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.FamilyMember;
import com.example.familymapclient.model.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapLinesBuilder {

    public List<MapLine> mapLines;
    public Settings settings;
    private static Logger log = new Logger("MapLinesBuilder");
    private DataCache dataCache;

    public MapLinesBuilder(Event event){
        mapLines = new ArrayList<>();

        dataCache = DataCache.getInstance();

        //  Get Settings for what lines to draw
        settings = dataCache.getSettings();

        if (settings.isFamilyTreeLineOn()){ addFamilyTreeLines(event); }

        if (settings.isLifeStoryLineOn()) { addLifeStoryLines(event); }

        if (settings.isSpouseLineOn()) { addSpouseLine(event); }

    }

    private void addFamilyTreeLines(Event currentEvent) {
        //  Line from current event to Father's birth event (or earliest available)
        //  Also to Mother's birth (or earliest available)
        //  Recurse through generations, LINES GET THINNER
        if (currentEvent == null){ log.d("Current Event nulled"); return;}

        upFamilyTree(currentEvent.getPersonID(), currentEvent,15);

    }

    private void upFamilyTree(String personID, Event earliestEvent,int w){
        if (earliestEvent == null) { log.d("NULL EVENT PASSED IN"); }
        //  Get person
        FamilyMember person = dataCache.familyMemberMap.get(personID);
        if (person == null) return;

        //  Get earliest mother event
        Event motherEvent = getEarliestEvent(person.getMotherID());
        if (motherEvent != null){
            //  Add a line between person's earliest event, mother's earliest event
            mapLines.add(new MapLine(earliestEvent,motherEvent,w,settings.getFamilyTreeLineColor()));
            upFamilyTree(person.getMotherID(), motherEvent, w > 7 ? w - 4 : 3);
        }
        //  Same for Father
        Event fatherEvent = getEarliestEvent(person.getFatherID());
        if (fatherEvent != null){
            //  Add a line
            mapLines.add(new MapLine(earliestEvent, fatherEvent,w,settings.getFamilyTreeLineColor()));
            upFamilyTree(person.getFatherID(), fatherEvent, w > 7 ? w - 4 : 3);
        }

    }

    private void addLifeStoryLines(Event event) {
        //  Lines drawn through current event's timeline in chronological order
        //  Only visible events.

        //  Get list of events
        List<String> eventIDList = dataCache.personEventListMap.get(event.getPersonID());
        List<Event> eventList = new ArrayList<>();
        for (String id : eventIDList){
            Event e = dataCache.eventMap.get(id);
            if (e != null){
                eventList.add(e);
            }
        }
        
        //  Sort Event list
        Collections.sort(eventList,new BirthDeathSort());
        
        //  Add line through events
        for (int i = 0; i < eventList.size(); i++){
            // TODO: 4/16/2019 FINISH THIS 
        }


    }

    private void addSpouseLine() {
        //  A Line between the selected event and the birth event of the person's spouse,
        //  (or else the spouse's earliest available event)
        //  No spouse, no line


    }

    private Event getEarliestEvent(String personID){

        List<String> eventIDList = dataCache.personEventListMap.get(personID);
        if (eventIDList == null) return null;

        FilteredMap eventMap = dataCache.eventMap;
        List<Event> eventList = new ArrayList<>();
        for (String id: eventIDList){
            Event e = eventMap.get(id);
            if (e != null) eventList.add(e);
        }

        //  Sort event list
        Collections.sort(eventList,new BirthDeathSort());

        if (eventList.size() == 0) return null;
        return eventList.get(0);

    }

    public class MapLine {

        public double getLat1() {
            return lat1;
        }

        public double getLong1() {
            return long1;
        }

        public double getLat2() {
            return lat2;
        }

        public double getLong2() {
            return long2;
        }

        public int getColor() {
            return color;
        }

        public int getWidth() {
            return width;
        }

        double lat1;
        double long1;
        double lat2;
        double long2;

        int color;
        int width;

        public MapLine(double lat1, double long1, double lat2, double long2, int color, int width) {
            this.lat1 = lat1;
            this.long1 = long1;
            this.lat2 = lat2;
            this.long2 = long2;
            this.color = color;
            this.width = width;
        }

        public MapLine(Event e1, Event e2, int w,int color){
            this(
                Double.parseDouble(e1.getLatitude()),
                Double.parseDouble(e1.getLongitude()),
                Double.parseDouble(e2.getLatitude()),
                Double.parseDouble(e2.getLongitude()),
                color,
                w
            );
        }
    }

}
