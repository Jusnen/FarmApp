package com.jenvolquez.farm.parse;


import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;


@ParseClassName("Pharmacy")
public class Pharmacy extends ParseObject{

    public String getName(){
        return getString("name");
    }

    public void setName(String name){
        put("name", name);
    }

    public String getHour(){
        return getString("hour");
    }

    public void setHour(String Hour){
        put("hour", Hour);
    }






    public String getAddress(){
        return getString("address");
    }

    public void setAddress(String address){
        put("address", address);
    }

    public LatLng getLocation() {
        ParseGeoPoint location = getParseGeoPoint("location");
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public String getPhoneNumber(String phoneNumber){
        return getString("phoneNumber");
    }

    public void setPhoneNumber(String phoneNumber){
        put("phoneNumber", phoneNumber);
    }

    public ParseFile getPhoto() {
        return getParseFile("farmPhoto");
    }

    @Override
    public String toString(){
        return getString("name") + "\n" + getString("address") + "\n" + getPhoneNumber("phoneNumber");
    }


    public void add(Pharmacy newFarm) {

    }

}
