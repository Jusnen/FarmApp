package com.jenvolquez.farm.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

import com.google.android.gms.wallet.Cart;
import com.jenvolquez.farm.R;
import com.jenvolquez.farm.parse.CartEntry;
import com.jenvolquez.farm.parse.Medicine;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class CartFragment extends Fragment{

    View myView;
    ListView listView;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.cart_layout, container, false);

        listView = (ListView)myView.findViewById(R.id.cart_list);

        ParseQueryAdapter.QueryFactory<CartEntry> factory = new ParseQueryAdapter.QueryFactory<CartEntry>() {
            @Override
            public ParseQuery<CartEntry> create() {
                ParseQuery<CartEntry> innerQuery = getCartEntryQuery();

                return innerQuery;
            }
        };

        ParseQueryAdapter<CartEntry> parseQueryAdapter =
                new ParseQueryAdapter<CartEntry>(getContext(), factory) {
                    @Override
                    public View getItemView(final CartEntry object, View v, ViewGroup parent) {
                        if (v == null) {
                            v = View.inflate(getContext(), R.layout.cart_list_item_layout, null);
                        }

                        TextView medNameTextView = (TextView)v.findViewById(R.id.med_name);
                        TextView medDescriptionTextView = (TextView)v.findViewById(R.id.med_description);
                        final ImageView medImageView = (ImageView)v.findViewById(R.id.med_thumb);
                        medNameTextView.setText(object.getMedicine().getName());
                        medDescriptionTextView.setText(object.getMedicine().getDescription());
                        object.getMedicine().getPhoto().getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                medImageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
                            }
                        });


                        ImageButton button;
                        button = (ImageButton)v.findViewById(R.id.removeItem);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                object.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Toast
                                                .makeText(getContext(), "Se ha elimiando del carrito", Toast.LENGTH_LONG)
                                                .show();
                                        loadObjects();
                                    }
                                });
                            }
                        });

                        EditText quantityEdit = (EditText)v.findViewById(R.id.quantityEdit);
                        quantityEdit.setText(String.valueOf(object.getQuantity()));

                        return v;
                    }
                };

        Button button;
        button = (Button) myView.findViewById(R.id.buy_button);
        button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View V){
                CheckoutFragment fragment = new CheckoutFragment();
                getFragmentManager().beginTransaction()
                        .addToBackStack("CheckoutFragment")
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });

        parseQueryAdapter.setImageKey("medicine");

        listView.setAdapter(parseQueryAdapter);

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
