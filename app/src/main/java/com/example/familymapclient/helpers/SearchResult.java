package com.example.familymapclient.helpers;

import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.FamilyMember;
import com.example.familymapclient.model.Person;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    public List<FamilyMember> personList;
    public List<Event> eventList;

    public SearchResult(List<String> personIDs, List<String> eventIDs){
        personList = new ArrayList<>();
        eventList = new ArrayList<>();

        DataCache dataCache = DataCache.getInstance();

        for (String id : personIDs){
            FamilyMember p = dataCache.familyMemberMap.get(id);
            if (p != null){
                personList.add(p);
            }
        }

        for (String id: eventIDs){
            Event e = dataCache.eventMap.get(id);
            if (e != null){
                eventList.add(e);
            }
        }

    }
}
