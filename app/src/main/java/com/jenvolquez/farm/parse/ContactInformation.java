package com.jenvolquez.farm.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Jen Volquez on 3/16/2016.
 */

@ParseClassName("ContactInformation")
public class ContactInformation extends ParseObject{

    public String getName(){
        return getString("Name");
    }

    public void setName(String name){
        put("Name", name);
    }

    public String getSubjectInfor(){
        return getString("Subject");
    }

    public void setSubjectInfor(String subject){
        put("Subject", subject);
    }

    public String getInformation(){
        return getString("Information");
    }

    public void setInformation(String information){
        put("Information", information);
    }
}



