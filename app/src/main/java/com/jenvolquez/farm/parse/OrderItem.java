package com.jenvolquez.farm.parse;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("OrderItem")
public class OrderItem  extends ParseObject {

    public Double getPrice(){
        return getDouble("price");
    }

    public void setPrice (Double Price){
        put("price", Price);
    }

    public int getQuantity(){
        return getInt("quantity");
    }

    public void setQuantity(int Quantity){
        put ("quantity", Quantity);
    }

    public void setMedicine(Medicine medicine) {
        put("medicine", medicine);
    }

    public void setOrder(Order order){
        put ("order", order);
    }


}
