package com.jenvolquez.farm;

import android.app.Application;

import com.jenvolquez.farm.parse.Medicine;
import com.jenvolquez.farm.parse.Pharmacy;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Jen Volquez on 3/9/2016.
 */
public class StarterApplication extends Application {

    public void onCreate() {
        super.onCreate();
        // Add your initialization code here
        ParseObject.registerSubclass(Pharmacy.class);
        ParseObject.registerSubclass(Medicine.class);
        Parse.initialize(this);
    }
}
