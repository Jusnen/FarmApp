package com.jenvolquez.farm.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jenvolquez.farm.R;
import com.jenvolquez.farm.parse.CartEntry;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;


public class CheckoutFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View myView= inflater.inflate(R.layout.checkout_layout, container, false);

        ParseQueryAdapter.QueryFactory<CartEntry> factory = new ParseQueryAdapter.QueryFactory<CartEntry>() {
            @Override
            public ParseQuery<CartEntry> create() {
                ParseQuery<CartEntry> innerQuery = new ParseQuery<>(CartEntry.class);
                ParseUser parseUser = ParseUser.getCurrentUser();
                innerQuery.whereEqualTo("owner", parseUser);
                innerQuery.include("pharmacyMedicine");
                innerQuery.include("pharmacyMedicine.medicine");

                return innerQuery;
            }
        };


        ParseQueryAdapter<CartEntry> parseQueryAdapter =
                new ParseQueryAdapter<CartEntry>(getContext(), factory) {
                    @Override
                    public View getItemView(final CartEntry object, View v, ViewGroup parent) {
                        if (v == null) {
                            v = View.inflate(getContext(), R.layout.checkout_list_item_layout, null);
                        }

                        TextView medNameTextView = (TextView)v.findViewById(R.id.medicine_name);
                        TextView medPriceTextView = (TextView)v.findViewById(R.id.medicine_price);
                        TextView medQuantityTextView = (TextView)v.findViewById(R.id.medicine_quantity);
                        int quantity = object
                                .getInt("quantity");
                        double total = object.getParseObject("pharmacyMedicine").getDouble("price") * quantity;

                        medNameTextView.setText(object.getMedicine().getName());
                        medPriceTextView.setText(String.valueOf(total));
                        medQuantityTextView.setText(String.valueOf(quantity));

                        return v;
                    }
                };

        ((ListView)myView.findViewById(R.id.cartListView)).setAdapter(parseQueryAdapter);
        getCartEntryQuery()
                .findInBackground(new FindCallback<CartEntry>() {
                    @Override
                    public void done(List<CartEntry> objects, ParseException e) {
                        double subtotal = 0;
                        for (CartEntry ce : objects) {
                            subtotal += ce.getQuantity() * ce.getParseObject("pharmacyMedicine")
                                    .getDouble("price");
                        }
                        ((TextView)myView.findViewById(R.id.medicine_total))
                                .setText(String.valueOf(subtotal));
                    }
                });
        return myView;
    }



    @NonNull
    private ParseQuery<CartEntry> getCartEntryQuery() {
        ParseQuery<CartEntry> innerQuery = new ParseQuery<>(CartEntry.class);
        ParseUser parseUser = ParseUser.getCurrentUser();
        innerQuery.whereEqualTo("owner", parseUser);
        innerQuery.include("pharmacyMedicine");
        innerQuery.include("pharmacyMedicine.medicine");
        return innerQuery;
    }
}
