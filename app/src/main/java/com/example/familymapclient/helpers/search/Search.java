package com.example.familymapclient.helpers.search;

import android.util.Log;

import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.FamilyMember;

import java.util.ArrayList;
import java.util.List;

public class Search {

    private static final String LOG_TAG = "Search";
    public List<SearchResult> results;

    public Search(String query){
        query = query.toLowerCase();
        this.results = new ArrayList<>();

        //  Search each familyMember's first and last name for a hit on the query
        searchFamilyMembers(query);

        searchFilteredEvents(query);
    }

    private void searchFamilyMembers(String query) {
        //  Get all Family Member objects
        final List<FamilyMember> familyMembers = new ArrayList<>(DataCache.getInstance().familyMemberMap.values());

        for (FamilyMember f : familyMembers){
            if (f.getFirstName().toLowerCase().contains(query) || f.getLastName().toLowerCase().contains(query)){
                results.add(familyMemberToSearchResult(f));
            }
        }
    }

    private void searchFilteredEvents(String query) {
        //  Get all filtered event objects
        List<Event> events = DataCache.getInstance().eventMap.getFilteredEvents();
        Log.d(LOG_TAG, "We found "+ events.size() +" events");
        for (Event e : events){
            if (e.getEventType().toLowerCase().contains(query) ||
                e.getCountry().toLowerCase().contains(query) ||
                e.getCity().toLowerCase().contains(query) ||
                String.valueOf(e.getYear()).contains(query)){

                results.add(eventToSearchResult(e));
            }
        }
    }

    private SearchResult familyMemberToSearchResult(FamilyMember familyMember){
        return new SearchResult(
                familyMember.getFirstName() + " " + familyMember.getLastName(),
                null,
                familyMember.getGender().equals("f") ? SearchResult.IconType.female : SearchResult.IconType.male,
                familyMember.getPersonID()
                );

    }

    private SearchResult eventToSearchResult(Event event){
        FamilyMember person = DataCache.getInstance().familyMemberMap.get(event.getPersonID());

        return new SearchResult(
                event.getEventType() + " " + event.getCity() + " " + event.getCountry() + " (" +event.getYear() + ")",
                person.getFirstName() + " " + person.getLastName(),
                SearchResult.IconType.event,
                event.getEventID()
                );
    }
}
