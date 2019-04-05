package com.example.familymapclient.fragments;

import android.app.Activity;
import android.content.Context;
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
import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Event;
import com.example.familymapclient.model.FamilyMember;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PersonFragment extends Fragment {
    private static final String LOG_TAG = "PersonFragment";
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
        Log.d(LOG_TAG,"Creating PersonFragment view");
        View view = inflater.inflate(R.layout.fragment_person, container, false);


        //  Populate person data fields
        TextView firstName = (TextView) view.findViewById(R.id.personFirstName);
        firstName.setText(person.getFirstName());

        TextView lastName = (TextView) view.findViewById(R.id.personLastName);
        lastName.setText(person.getLastName());

        TextView gender = (TextView) view.findViewById(R.id.personGender);
        gender.setText(person.getGender().equals("m") ? "Male" : "Female");

        //  Get events, family members for person
        DataCache dataCache = DataCache.getInstance();

        //  Get events related to the person, then sort birth < else < death
        List<Event> events = dataCache.personEventListMap.get(person.getPersonID());
        Collections.sort(events, new BirthDeathSort());

        //  Get a list of all their relations
        List<Relation> relations = generateRelations(dataCache);

        //  Compile these into one expandable list view
        this.eventsAndFamily = view.findViewById(R.id.events_and_family);
        eventsAndFamily.setAdapter(new EventsAndFamilyAdapter(events, relations, getActivity()));

        //  Add onclick functionality to each person
        eventsAndFamily.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //  Create a new PersonFragment with the clicked person or event
                switch (groupPosition){
                    case EventsAndFamilyAdapter.EVENT_GROUP_POSITION:
                        // TODO: 4/5/2019 Link to EventActivity 
                        break;

                    case EventsAndFamilyAdapter.PERSON_GROUP_POSITION:
                        // TODO: 4/5/2019 Link to new PersonFragment 
                        break;

                    default:
                        throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
                }

                return true;
            }
        });

        return view;
    }

    public static PersonFragment newInstance(FamilyMember person){
        Bundle args = new Bundle();
        args.putParcelable(ARG_PERSON,person);

        PersonFragment personFragment = new PersonFragment();
        personFragment.setArguments(args);
        return personFragment;
    }

    private class BirthDeathSort implements Comparator<Event> {

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

    private List<Relation> generateRelations(DataCache dataCache) {

        List<Relation> relations = new ArrayList<>();
        if (person.getMotherID() != null){
            relations.add(new Relation(dataCache.familyMemberMap.get(person.getMotherID()), "Mother"));
        }
        if (person.getFatherID() != null){
            relations.add(new Relation(dataCache.familyMemberMap.get(person.getFatherID()), "Father"));
        }
        if (person.getSpouseID() != null){
            relations.add(new Relation(dataCache.familyMemberMap.get(person.getSpouseID()), "Spouse"));
        }

        for(String childID: person.getChildrenIDList()){
            relations.add(new Relation(dataCache.familyMemberMap.get(childID), "Child"));
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

        private final List<Event> eventList;
        private final List<Relation> familyMemberList;

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
            Relation familyMember = familyMemberList.get(childPosition);

            // Create Drawable gender icon
            Drawable genderIcon = new IconDrawable(getActivity(),
                    familyMember.person.getGender().equals("f") ? FontAwesomeIcons.fa_female : FontAwesomeIcons.fa_male)
                    .colorRes(R.color.male_icon).sizeDp(40);

            //  Apply attributes
            top_text.setText( familyMember.person.getFirstName() + " " + familyMember.person.getLastName() );
            bot_text.setText( familyMember.relation);
            genderIconView.setImageDrawable(genderIcon);
        }

        private void initializeLifeEventView(View itemView, final int childPosition) {
            //  Fill the right info based on the position

            TextView top_text = itemView.findViewById(R.id.top_text);
            TextView bot_text = itemView.findViewById(R.id.bot_text);
            ImageView iconView = itemView.findViewById(R.id.icon);

            //  Fill in based on Event
            Event event = eventList.get(childPosition);

            // Apply attributes
            top_text.setText(event.getEventType() + ": " +
                    event.getCity() + ", " + event.getCountry() + " (" +event.getYear()+")");

            bot_text.setText(person.getFirstName() + " " + person.getLastName());

            iconView.setImageDrawable(
                    new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker)
                            .colorRes(R.color.label).sizeDp(40));
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
    ////    END EXPANDABLE LIST ADAPTER


}
