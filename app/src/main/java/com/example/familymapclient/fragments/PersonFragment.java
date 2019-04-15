package com.example.familymapclient.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.activities.EventActivity;
import com.example.familymapclient.activities.PersonActivity;
import com.example.familymapclient.helpers.Logger;
import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.FamilyMember;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class PersonFragment extends Fragment {
    private static Logger log = new Logger("PersonFragment");
    public static final String ARG_PERSON = "person";

    private FamilyMember person;

    private ExpandableListView eventsAndFamily;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.person = getArguments().getParcelable(ARG_PERSON);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        log.d("Creating PersonFragment view");
        View view = inflater.inflate(R.layout.fragment_person, container, false);


        //  Populate person data fields
        TextView firstName = view.findViewById(R.id.personFirstName);
        firstName.setText(person.getFirstName());

        TextView lastName = view.findViewById(R.id.personLastName);
        lastName.setText(person.getLastName());

        TextView gender = view.findViewById(R.id.personGender);
        gender.setText(person.getGender().equals("m") ? "Male" : "Female");

        //  Get events, family members for person

        //  Get events related to the person, then sort birth < else < death
        log.d("Getting all events associated with "+ person.getPersonID());
        List<String> eventIDs = DataCache.getInstance().personEventListMap.get(person.getPersonID());
        List<Event> events = new ArrayList<>();

        if (eventIDs == null ) {
            log.d("NO EVENTS FOUND");
            eventIDs = new ArrayList<>();
        }

        for (String eventID : eventIDs){
            Event e = DataCache.getInstance().eventMap.get(eventID);
            if (e != null){
                events.add(e);
            }
        }

        Collections.sort(events, new BirthDeathSort());

        //  Get a list of all their relations
        List<Relation> relations = generateRelations();

        //  Compile these into one expandable list view
        this.eventsAndFamily = view.findViewById(R.id.events_and_family);
        eventsAndFamily.setAdapter(new EventsAndFamilyAdapter(events, relations, getActivity()));

        return view;
    }

    public static PersonFragment newInstance(FamilyMember person){
        Bundle args = new Bundle();
        args.putParcelable(ARG_PERSON,person);

        PersonFragment personFragment = new PersonFragment();
        personFragment.setArguments(args);
        return personFragment;
    }



    private List<Relation> generateRelations() {
        Map<String,FamilyMember> familyMemberMap = DataCache.getInstance().familyMemberMap;

        List<Relation> relations = new ArrayList<>();
        if (person.getMotherID() != null){
            relations.add(new Relation(familyMemberMap.get(person.getMotherID()), "Mother"));
        }
        if (person.getFatherID() != null){
            relations.add(new Relation(familyMemberMap.get(person.getFatherID()), "Father"));
        }
        if (person.getSpouseID() != null){
            relations.add(new Relation(familyMemberMap.get(person.getSpouseID()), "Spouse"));
        }

        for(String childID: person.getChildrenIDList()){
            relations.add(new Relation(familyMemberMap.get(childID), "Child"));
        }

        return relations;
    }

    private class Relation {
        FamilyMember person;
        String relation;

        Relation(FamilyMember person, String relation){
            this.person = person;
            this.relation = relation;
        }
    }


    ////    BEGIN EXPANDABLE LIST ADAPTER
    private class EventsAndFamilyAdapter extends BaseExpandableListAdapter {
        private static final int EVENT_GROUP_POSITION = 0;
        private static final int PERSON_GROUP_POSITION = 1;

        List<Event> eventList;
        List<Relation> familyMemberList;

        public Activity getActivity() {
            return activity;
        }

        private final Activity activity;

        EventsAndFamilyAdapter(List<Event> eventList, List<Relation> personList, Activity activity){
            this.eventList = eventList;
            this.familyMemberList = personList;
            this.activity = activity;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition){
                case EVENT_GROUP_POSITION:
                    return eventList.size();
                case PERSON_GROUP_POSITION:
                    return familyMemberList.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);

            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition){
                case EVENT_GROUP_POSITION:
                    return getString(R.string.lifeEvents);
                case PERSON_GROUP_POSITION:
                    return getString(R.string.family);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition){
                case EVENT_GROUP_POSITION:
                    return eventList.get(childPosition);
                case PERSON_GROUP_POSITION:
                    return familyMemberList.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.icon_2_field,parent,false);
            }

            TextView title_view = convertView.findViewById(R.id.top_text);

            switch (groupPosition){
                case EVENT_GROUP_POSITION:
                    title_view.setText(R.string.lifeEvents);
                    break;
                case PERSON_GROUP_POSITION:
                    title_view.setText(R.string.family);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView;
            itemView = layoutInflater.inflate(R.layout.icon_2_field, parent, false);

            switch (groupPosition){
                case EVENT_GROUP_POSITION:
                    initializeLifeEventView(itemView, childPosition);
                    break;
                case PERSON_GROUP_POSITION:
                    initializeFamilyView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return itemView;
        }

        private void initializeFamilyView(View itemView, final int childPosition) {
            TextView top_text = itemView.findViewById(R.id.top_text);
            TextView bot_text = itemView.findViewById(R.id.bot_text);
            ImageView genderIconView = itemView.findViewById(R.id.icon);

            //  Fill the right info based on the position
            final Relation familyMember = familyMemberList.get(childPosition);

            // Create Drawable gender icon
            boolean isFemale = familyMember.person.getGender().equals("f");
            FontAwesomeIcons iconType = isFemale ? FontAwesomeIcons.fa_female : FontAwesomeIcons.fa_male;
            int color = isFemale ? R.color.colorFemale : R.color.colorMale;
            Drawable genderIcon = new IconDrawable(getActivity(),iconType).colorRes(color).sizeDp(40);

            //  Apply attributes
            top_text.setText( familyMember.person.getFirstName() + " " + familyMember.person.getLastName() );
            bot_text.setText( familyMember.relation);
            genderIconView.setImageDrawable(genderIcon);

            //  Set click to open new PersonActivity
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent personIntent = new Intent(getActivity(), PersonActivity.class);
                    personIntent.putExtra(PersonActivity.EXTRA_PERSON_ID, familyMember.person.getPersonID());
                    startActivity(personIntent);
                }
            });

        }

        private void initializeLifeEventView(View itemView, final int childPosition) {
            //  Fill the right info based on the position

            TextView top_text = itemView.findViewById(R.id.top_text);
            TextView bot_text = itemView.findViewById(R.id.bot_text);
            ImageView iconView = itemView.findViewById(R.id.icon);

            //  Fill in based on Event
            final Event event = eventList.get(childPosition);

            // Apply attributes
            top_text.setText(event.getEventType() + ": " +
                    event.getCity() + ", " + event.getCountry() + " (" +event.getYear()+")");

            bot_text.setText(person.getFirstName() + " " + person.getLastName());

            iconView.setImageDrawable(
                    new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker)
                            .colorRes(R.color.label).sizeDp(40));

            //  Set click to create event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Create a new EventActivity with the given EventID
                    Intent eventIntent = new Intent(getActivity(), EventActivity.class);
                    eventIntent.putExtra(EventActivity.EXTRA_EVENT_ID, event.getEventID());
                    startActivity(eventIntent);
                }
            });

        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
    ////    END EXPANDABLE LIST ADAPTER


}
