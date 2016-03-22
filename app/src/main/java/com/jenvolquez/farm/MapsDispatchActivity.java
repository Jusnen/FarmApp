package com.jenvolquez.farm;

import com.parse.ui.ParseLoginDispatchActivity;

/**
 * Created by Jen Volquez on 3/21/2016.
 */
public class MapsDispatchActivity extends ParseLoginDispatchActivity {
    @Override
    protected Class<?> getTargetClass() {
        return MapsActivity.class;
    }
}
