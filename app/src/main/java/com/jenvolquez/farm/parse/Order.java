package com.jenvolquez.farm.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("Order")
public class Order extends ParseObject {

    public String getAddressReference(){
        return getString("addressReference");
    }

    public void setAddressReference(String AddressReference){
        put("addressReference", AddressReference);
    }
}
