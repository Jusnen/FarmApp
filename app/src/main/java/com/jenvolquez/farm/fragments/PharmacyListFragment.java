package com.jenvolquez.farm.fragments;

import android.os.Bundle;

import com.jenvolquez.farm.parse.Pharmacy;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;


import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

import java.util.List;


/**
 * Created by Jen Volquez on 3/10/2016.
 */
public class PharmacyListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ParseQuery<Pharmacy> query = ParseQuery.getQuery(Pharmacy.class);
        final PharmacyListFragment self = this;
        query.findInBackground(new FindCallback<Pharmacy>() {

            @Override
            public void done(List<Pharmacy> pharmacies, ParseException e) {
                ArrayAdapter<Pharmacy> adapter = new ArrayAdapter<>(self.getContext(),
                        android.R.layout.simple_list_item_1,
                        pharmacies);
                setListAdapter(adapter);
            }
        });

    }

}