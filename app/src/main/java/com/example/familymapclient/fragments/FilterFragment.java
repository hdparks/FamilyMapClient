package com.example.familymapclient.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.model.DataCache;
import com.example.familymapclient.model.Filter;

import java.util.List;


public class FilterFragment extends Fragment {
    private static final String LOG_TAG = "FilterFragment";

    private RecyclerView mFilterView;
    private FilterAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG,"Creating Fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        mFilterView = view.findViewById(R.id.filter_list_view);
        mFilterView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
    
    public void updateUI(){
        //  Pull out the current list of filters
        DataCache dataCache = DataCache.getInstance();

        List<Filter> filterList = dataCache.eventMap.filterList;

        Log.d(LOG_TAG,"Updating UI: filter list has length"+filterList.size());

        for (Filter filter : filterList){
            Log.d(LOG_TAG,filter.getTitle());
        }

        mAdapter = new FilterAdapter(filterList);
        mFilterView.setAdapter(mAdapter);

        DividerItemDecoration mDivider = new DividerItemDecoration(mFilterView.getContext(),1);
        mFilterView.addItemDecoration(mDivider);


    }

    public static FilterFragment newInstance(){
        return new FilterFragment();
    }

    private class FilterHolder extends RecyclerView.ViewHolder {

        private Filter filter;

        TextView filterTitle;
        TextView filterDescription;
        Switch filterSwitch;


        public FilterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.filter_layout, parent,false));

            this.filterTitle = itemView.findViewById(R.id.filter_title);
            this.filterDescription = itemView.findViewById(R.id.filter_description);
            this.filterSwitch = itemView.findViewById(R.id.filter_switch);

        }

        void bind(Filter filter){
            this.filter = filter;
            this.filterTitle.setText(filter.getTitle());
            this.filterDescription.setText(filter.getDescription());
            this.filterSwitch.setChecked(filter.getActive());

            this.filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //  Set the checked value on the given filter
                    updateFilter(isChecked);
                }
            });
        }

        void updateFilter(boolean active){
            filter.setActive(active);
        }
    }

    public class FilterAdapter extends RecyclerView.Adapter<FilterHolder>{

        private final List<Filter> filterList;

        FilterAdapter(List<Filter> filterList){
            this.filterList = filterList;
        }


        @Override
        public FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            Log.d(LOG_TAG, "Creating FilterHolder");
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FilterHolder(layoutInflater,parent);

        }

        @Override
        public void onBindViewHolder(@NonNull FilterHolder filterHolder, int i) {
            Filter filter = filterList.get(i);
            filterHolder.bind(filter);
        }

        @Override
        public int getItemCount() {
            return filterList.size();
        }

    }

}
