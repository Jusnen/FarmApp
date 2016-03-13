package com.jenvolquez.farm.parse;


import android.media.Image;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
/**
 * Created by Jen Volquez on 3/13/2016.
 */

@ParseClassName("Medicine")
public class Medicine extends ParseObject{

    public String getName(){
        return getString("name");
    }

    public void setName(String name){
        put("name", name);
    }

    public String getDescription(){
        return getString("description");
    }

    public void setDescription(String description){
        put("description", description);
    }

    public ParseFile getPhoto(){return getParseFile("medphoto");}

    public void setPhoto(ParseFile photo){put("medphoto", photo); }



    @Override
    public String toString(){
        return getString("name") + "\n" + getString("description") ;
    }



    public void add(Medicine newMed) {

    }
}
