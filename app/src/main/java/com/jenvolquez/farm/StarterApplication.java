package com.jenvolquez.farm;

import android.app.Application;

import com.google.android.gms.vision.barcode.Barcode;
import com.jenvolquez.farm.parse.ContactInformation;
import com.jenvolquez.farm.parse.Medicine;
import com.jenvolquez.farm.parse.Pharmacy;
import com.parse.Parse;
import com.parse.ParseObject;

public class StarterApplication extends Application {

    public void onCreate() {
        super.onCreate();
        // Add your initialization code here
        ParseObject.registerSubclass(Pharmacy.class);
        ParseObject.registerSubclass(Medicine.class);
        ParseObject.registerSubclass(ContactInformation.class);
        Parse.initialize(this);
    }
}
