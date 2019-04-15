package com.example.familymapclient.fragments;

import com.example.familymapclient.model.Event;

import java.util.Comparator;

public class BirthDeathSort implements Comparator<Event> {

    @Override
    public int compare(Event o1, Event o2) {

        String o1Type = o1.getEventType().toLowerCase();
        String o2Type = o2.getEventType().toLowerCase();

        //  Events are sorted primarily by birth < else < death
        if (o1Type.equals("birth")) return -1;
        if (o1Type.equals("death")) return 1;
        if (o2Type.equals("birth")) return 1;
        if (o2Type.equals("death")) return -1;

        //  Events are then sorted by year
        if (o1.getYear() < o2.getYear()) return -1;
        if (o1.getYear() > o2.getYear()) return 1;

        //  Events are finally sorted alphabetically by lower-cased type
        return o1Type.compareTo(o2Type);

    }
}
