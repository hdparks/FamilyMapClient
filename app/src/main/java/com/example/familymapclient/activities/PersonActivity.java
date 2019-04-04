package com.example.familymapclient.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.familymapclient.model.Person;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PersonActivity extends AppCompatActivity {

    public static final String EXTRA_PERSON_ID = "person";

    private FamilyMember person;

    private ExpandableListView eventsAndFamily;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Intent intent = getIntent();

        this.person = intent.getParcelableExtra(EXTRA_PERSON_ID);
        this.eventsAndFamily = findViewById(R.id.events_and_family);


        DataCache dataCache = DataCache.getInstance();


        //  Get events, family members for person
        List<Event> events = dataCache.personEventListMap.get(person.getPersonID());
        Collections.sort(events, new BirthDeathSort());

        List<Relation> relations = generateRelations(dataCache);

        //  Set up ExpandableListAdapter
        eventsAndFamily.setAdapter(new ExpandableListAdapter(events, relations, this));

    }

    private class BirthDeathSort implements Comparator<Event>{

        @Override
        public int compare(Event o1, Event o2) {
            if (o1.getEventType().toLowerCase().equals("birth")) return -1;
            if (o1.getEventType().toLowerCase().equals("death")) return 1;
            if (o2.getEventType().toLowerCase().equals("birth")) return 1;
            if (o2.getEventType().toLowerCase().equals("death")) return -1;
            return 0;
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
    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private static final int EVENT_GROUP_POSITION = 0;
        private static final int PERSON_GROUP_POSITION = 1;

        private final List<Event> eventList;
        private final List<Relation> familyMemberList;

        public Activity getActivity() {
            return activity;
        }

        private final Activity activity;

        ExpandableListAdapter(List<Event> eventList, List<Relation> personList, Activity activity){
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
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            top_text.setText( person.getFirstName() + " " + person.getLastName() );
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
