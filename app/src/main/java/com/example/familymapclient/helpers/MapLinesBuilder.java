package com.example.familymapclient.helpers;

import android.provider.ContactsContract;

import com.example.familymapclient.R;
import com.example.familymapclient.fragments.BirthDeathSort;
import com.example.familymapclient.helpers.filter.FilteredMap;
import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.FamilyMember;
import com.example.familymapclient.model.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MapLinesBuilder {

    public List<MapLine> mapLines;
    public Settings settings;

    public MapLinesBuilder(Event event){
        mapLines = new ArrayList<>();

        //  Get Settings for what lines to draw
        settings = DataCache.getInstance().getSettings();

        if (settings.isFamilyTreeLineOn()){ addFamilyTreeLines(event); }

        if (settings.isLifeStoryLineOn()) { addLifeStoryLines(); }

        if (settings.isSpouseLineOn()) { addSpouseLine(); }

    }

    private void addFamilyTreeLines(Event currentEvent) {
        //  Line from current event to Father's birth event (or earliest available)
        //  Also to Mother's birth (or earliest available)
        //  Recurse through generations, LINES GET THINNER
        upFamilyTree(currentEvent.getPersonID(), currentEvent,10);

    }

    private void upFamilyTree(String personID, Event earliestEvent,int w){
        //  Get person
        FamilyMember person = DataCache.getInstance().familyMemberMap.get(personID);
        if (person == null) return;

        //  Get earliest mother event
        Event motherEvent = getEarliestEvent(person.getMotherID());
        if (motherEvent != null){
            //  Add a line between person's earliest event, mother's earliest event
            mapLines.add(new MapLine(earliestEvent,motherEvent,w));
        }
        //  Same for Father
        Event fatherEvent = getEarliestEvent(person.getFatherID());
        if (fatherEvent != null){
            //  Add a line
            mapLines.add(new MapLine(earliestEvent, fatherEvent,w));
        }

        //  recurse on either side
        upFamilyTree(person.getMotherID(), motherEvent, w > 5 ? w - 2 : 3);
        upFamilyTree(person.getFatherID(), fatherEvent, w > 5 ? w - 2: 3);

    }

    private void addLifeStoryLines() {
        //  Lines drawn through current event's timeline in chronological order
        //  Only visible events.

    }

    private void addSpouseLine() {
        //  A Line between the selected event and the birth event of the person's spouse,
        //  (or else the spouse's earliest available event)
        //  No spouse, no line


    }

    private Event getEarliestEvent(String personID){

        List<String> eventIDList = DataCache.getInstance().personEventListMap.get(personID);
        if (eventIDList == null) return null;

        FilteredMap eventMap = DataCache.getInstance().eventMap;
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

        public MapLine(Event e1, Event e2, int w){
            new MapLine(
                Double.valueOf(e1.getLatitude()),
                Double.valueOf(e1.getLongitude()),
                Double.valueOf(e2.getLatitude()),
                Double.valueOf(e2.getLongitude()),
                settings.getFamilyTreeLineColor(),
                w
            );
        }
    }

}
