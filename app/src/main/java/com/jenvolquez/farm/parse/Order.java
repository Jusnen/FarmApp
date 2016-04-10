package com.jenvolquez.farm.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("Order")
public class Order extends ParseObject {

    public String getAddressReference(){
        return getString("addressReference");
    }

    public void setAddressReference(String AddressReference){
        put("addressReference", AddressReference);
    }

    public boolean getPagoEfectivo(){
        return getBoolean("pagoEfectivo");
    }


    public void setPagoEfectivo(boolean PagoEfectivo){
        put("pagoEfectivo", PagoEfectivo);

    }


    public boolean getPasarRecoger(){
        return getBoolean("pasarRecoger");
    }


    public void setPasarRecoger(boolean PasarRecoger){
        put("pasarRecoger", PasarRecoger);

    }

    public void setPharmacy(Pharmacy pharmacy) {
        put("pharmacy", pharmacy);
    }

    public void setUser(ParseUser user){
        put ("cliente", user);
    }

}
