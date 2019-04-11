package com.example.familymapclient.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.activities.EventActivity;
import com.example.familymapclient.activities.PersonActivity;
import com.example.familymapclient.helpers.Search;
import com.example.familymapclient.helpers.SearchResult;
import com.example.familymapclient.model.DataCache;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private static final String LOG_TAG = "SearchFragment";
    private EditText queryView;
    private RecyclerView resultView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        queryView = view.findViewById(R.id.query_field);
        resultView = view.findViewById(R.id.results_field);
        resultView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration mDivider = new DividerItemDecoration(resultView.getContext(),1);
        resultView.addItemDecoration(mDivider);

        //  Starts with an empty result view
        resultView.setAdapter(new ResultAdapter(new ArrayList<SearchResult>()));

        queryView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(LOG_TAG, "Sending search query");
                String query = queryView.getText().toString();
                if (query.isEmpty()) return;

                Search search = new Search(query);

                ResultAdapter adapter = new ResultAdapter(search.results);
                resultView.swapAdapter(adapter, false);
            }
        });

        return view;

    }


    private class ResultHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private SearchResult searchResult;

        TextView topText;
        TextView botText;
        ImageView icon;


        public ResultHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.icon_2_field, parent,false));

            this.topText = itemView.findViewById(R.id.top_text);
            this.botText = itemView.findViewById(R.id.bot_text);
            this.icon = itemView.findViewById(R.id.icon);
        }

        void bind(SearchResult searchResult){
            this.searchResult = searchResult;

            this.topText.setText(searchResult.getTopString());
            this.botText.setText(searchResult.getBotString());

            //  Set Icon and Color depending on type of result
            FontAwesomeIcons iconType;
            int color;

            switch (searchResult.getIconType()){

                case male:
                    iconType = FontAwesomeIcons.fa_male;
                    color = R.color.colorMale;
                    break;

                case female:
                    iconType = FontAwesomeIcons.fa_female;
                    color = R.color.colorFemale;
                    break;

                case event:
                    iconType = FontAwesomeIcons.fa_navicon;
                    color = R.color.colorWhite;
                    break;

                default:
                    iconType = FontAwesomeIcons.fa_battery_empty;
                    color = R.color.colorWhite;

            }

            //  Apply Icon
            Drawable myicon = new IconDrawable(getActivity(),iconType).colorRes(color).sizeDp(40);
            this.icon.setImageDrawable(myicon);
        }

        @Override
        public void onClick(View v) {
            switch (this.searchResult.getIconType()){
                case event:
                    //  Start the event activity
                    Intent eventIntent = new Intent(getActivity(), EventActivity.class);
                    eventIntent.putExtra(EventActivity.EXTRA_EVENT_ID,searchResult.getResourceID());
                    startActivity(eventIntent);
                    break;

                case female:
                case male:
                    //  Start the person activity
                    Intent personIntent = new Intent(getActivity(), PersonActivity.class);
                    personIntent.putExtra(PersonActivity.EXTRA_PERSON_ID,searchResult.getResourceID());
                    startActivity(personIntent);
                    break;

            }
        }
    }

    private class ResultAdapter extends RecyclerView.Adapter<ResultHolder> {

        private final List<SearchResult> resultList;

        private ResultAdapter(List<SearchResult> resultList) {
            this.resultList = resultList;
        }

        @NonNull
        @Override
        public ResultHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ResultHolder(layoutInflater,viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull ResultHolder resultHolder, int i) {
            SearchResult result = resultList.get(i);
            resultHolder.bind(result);
        }

        @Override
        public int getItemCount() {
            return resultList.size();
        }
    }

}
