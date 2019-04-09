package com.example.familymapclient.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private RecyclerView mFilterView;
    private FilterAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        mAdapter = new FilterAdapter(filterList);
        mFilterView.setAdapter(mAdapter);


    }

    public static FilterFragment newInstance(){
        return new FilterFragment();
    }

    private class FilterHolder extends RecyclerView.ViewHolder {

        private Filter filter;

        private TextView filterTitle;
        private TextView filterDescription;
        private Switch filterSwitch;


        public FilterHolder(LayoutInflater inflater, ViewGroup itemView) {
            super(inflater.inflate(R.layout.filter_layout, itemView, false));

            filterTitle = itemView.findViewById(R.id.filter_title);
            filterDescription = itemView.findViewById(R.id.filter_description);
            filterSwitch = itemView.findViewById(R.id.filter_switch);
        }

        void bind(Filter filter){
            this.filter = filter;
            filterTitle.setText(filter.getTitle());
            filterDescription.setText(filter.getDescription());
            filterSwitch.setChecked(filter.getActive());

            filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //  Get the FilterHolder
                    FilterHolder holder = (FilterHolder) buttonView.getParent();

                    //  Set the checked value on the given filter
                    holder.filter.setActive(isChecked);
                }
            });
        }
    }

    public class FilterAdapter extends RecyclerView.Adapter<FilterHolder>{
        List<Filter> filterList;

        public FilterAdapter(List<Filter> filterList){
            this.filterList = filterList;
        }

        @NonNull
        @Override
        public FilterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FilterHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull FilterHolder filterHolder, int i) {
            filterHolder.bind(filterList.get(i));
        }

        @Override
        public int getItemCount() {
            return filterList.size();
        }

    }

}
