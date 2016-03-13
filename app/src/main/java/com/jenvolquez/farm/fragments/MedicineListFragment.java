package com.jenvolquez.farm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jenvolquez.farm.R;
import com.jenvolquez.farm.parse.Medicine;
import com.jenvolquez.farm.parse.Pharmacy;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Jen Volquez on 3/10/2016.
 */
public class MedicineListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseQuery<Medicine> query = ParseQuery.getQuery(Medicine.class);
        final  MedicineListFragment self= this;
        query.findInBackground(new FindCallback<Medicine>() {
            @Override
            public void done(List<Medicine> medicines, ParseException e) {
                ArrayAdapter<Medicine> adapter = new ArrayAdapter<>(self.getContext(),
                        android.R.layout.simple_list_item_1,
                        medicines);
                setListAdapter(adapter);
            }
        });

    }

}
