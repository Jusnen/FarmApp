package com.jenvolquez.farm.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("User")
public class RegisterLog extends ParseObject {

    public String getName(){
        return getString("Name");
    }

    public void setName(String name){
        put("Name", name);
    }

    public String getUsername(){
        return getString("Username");
    }

    public void setUsername(String username){
        put("Username", username);
    }

    public String getPassword(){
        return getString("Password");
    }

    public void setPassword(String password){
        put("Password", password);
    }

    public String getEmail(){
        return getString("Email");
    }

    public void setEmail(String email){
        put("Email", email);
    }

    public String getPhone(){
        return getString("Phone");
    }

    public void setPhone(String phone){
        put("Phone", phone);
    }




}
