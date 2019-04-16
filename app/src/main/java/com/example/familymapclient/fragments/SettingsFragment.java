package com.example.familymapclient.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.familymapclient.R;
import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Settings;

import java.util.Set;


public class SettingsFragment extends Fragment {

    Settings settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = DataCache.getInstance().getSettings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        //  Pull the current settings object

        Spinner mapSpinner = view.findViewById(R.id.map_type_spinner);

        //  Set up map spinners
        ArrayAdapter<CharSequence> mapAdapter = ArrayAdapter.createFromResource(getContext(),R.array.maps, android.R.layout.simple_spinner_item);
        mapAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapSpinner.setAdapter(mapAdapter);

        mapSpinner.setSelection(settings.getMapTypePosition());

        mapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: settings.setMapType(Settings.MapType.Normal); return;
                    case 1: settings.setMapType(Settings.MapType.Satellite); return;
                    case 2: settings.setMapType(Settings.MapType.Hybrid); return;
                    case 3: settings.setMapType(Settings.MapType.Terrain); return;
                    default: return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner lifeStorySpinner = view.findViewById(R.id.life_story_spinner);
        Spinner familyTreeSpinner = view.findViewById(R.id.family_tree_spinner);
        Spinner spouseSpinner = view.findViewById(R.id.spouse_lines_spinner);



        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(getContext(),R.array.colors, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        lifeStorySpinner.setAdapter(colorAdapter);
        familyTreeSpinner.setAdapter(colorAdapter);
        spouseSpinner.setAdapter(colorAdapter);

        lifeStorySpinner.setSelection(settings.getLifeStoryLinePosition());
        lifeStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setLifeStoryLineColor(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        familyTreeSpinner.setSelection(settings.getFamilyLinePosition());
        familyTreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setFamilyTreeLineColor(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spouseSpinner.setSelection(settings.getSpouseColorPosition());
        spouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setSpouseLineColor(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Switch lifeStorySwitch = view.findViewById(R.id.life_story_switch);
        Switch familyTreeSwitch = view.findViewById(R.id.family_tree_switch);
        Switch spouseSwitch = view.findViewById(R.id.spouse_lines_switch);

        lifeStorySwitch.setChecked(settings.isLifeStoryLineOn());
        familyTreeSwitch.setChecked(settings.isFamilyTreeLineOn());
        spouseSwitch.setChecked(settings.isSpouseLineOn());

        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setLifeStoryLineOn(isChecked);
            }
        });

        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFamilyTreeLineOn(isChecked);
            }
        });

        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setSpouseLineOn(isChecked);
            }
        });


        return view;
    }

}
