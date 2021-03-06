package com.jenvolquez.farm.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;



@ParseClassName("CartEntry")
public class CartEntry extends ParseObject {

    public Medicine getMedicine() {
        return (Medicine)getParseObject("pharmacyMedicine").get("medicine");
    }

    public Pharmacy getPharmacy() {
        return (Pharmacy)getParseObject("pharmacyMedicine").get("pharmacy");
    }

    public Double getPrice() {
        return getParseObject("pharmacyMedicine").getDouble("price");
    }

    public int getQuantity() {
        return getInt("quantity");
    }

    public void incrementQuantity() {
        incrementQuantity(1);
    }

    public void incrementQuantity(int i) {
        put("quantity", getInt("quantity") + i);
    }
}
