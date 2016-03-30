package com.jenvolquez.farm.parse;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Medicine")
public class Medicine extends ParseObject{

    public String getName(){
        return getString("name");
    }

    public void setName(String name){
        put("name", name);
    }

    public ParseFile getPhoto() {
        return getParseFile("medphoto");
    }

    @Override
    public String toString(){
        return getString("name") + "\n" + getString("description") ;
    }

    public String getDescription() {
        return getString("description");
    }

    public double getPrice() {
        return getDouble("price");
    }
}
