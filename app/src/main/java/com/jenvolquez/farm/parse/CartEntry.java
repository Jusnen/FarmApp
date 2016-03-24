package com.jenvolquez.farm.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Jen Volquez on 3/23/2016.
 */

@ParseClassName("CartEntry")
public class CartEntry extends ParseObject {

    public Medicine getMedicine() {
        return (Medicine)get("medicine");
    }

}
