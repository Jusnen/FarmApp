package com.jenvolquez.farm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jenvolquez.farm.R;

/**
 * Created by Jen Volquez on 3/10/2016.
 */
public class SufferingFragment extends Fragment{

    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.suffering_layout, container, false);
        return myView;
    }
}
