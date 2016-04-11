package com.jenvolquez.farm.fragments;

import android.app.AlertDialog;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jenvolquez.farm.R;
import com.jenvolquez.farm.parse.CartEntry;
import com.jenvolquez.farm.parse.Order;
import com.jenvolquez.farm.parse.OrderItem;
import com.jenvolquez.farm.parse.Pharmacy;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class CheckoutFragment extends Fragment{

    private Pharmacy pharmacy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View myView= inflater.inflate(R.layout.checkout_layout, container, false);

        ParseQueryAdapter.QueryFactory<CartEntry> factory = new ParseQueryAdapter.QueryFactory<CartEntry>() {
            @Override
            public ParseQuery<CartEntry> create() {
                return getAllEntriesOnCartQuery();
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
                        ((TextView) myView.findViewById(R.id.medicine_total))
                                .setText(String.valueOf(subtotal));
                    }
                });


        Button checkoutButton = (Button)myView.findViewById(R.id.checkout_button);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCartEntryQuery().findInBackground(new FindCallback<CartEntry>() {
                    @Override
                    public void done(final List<CartEntry> entries, ParseException e) {
                        final Order order = createOrder();
                        order.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                ArrayList<ParseObject> orderItems = new ArrayList<ParseObject>();
                                for (CartEntry entry : entries) {
                                    ParseObject orderItem = mapToOrderItem(entry, order);
                                    orderItems.add(orderItem);
                                }

                                ParseObject.saveAllInBackground(orderItems, new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Toast.makeText(getContext(), "Creando orden", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });

            }
        });


       TextView pharmacyNameTextView = (TextView)myView.findViewById(R.id.pharmacy_name);
        pharmacyNameTextView.setText(pharmacy.getName());

        return myView;
    }

    private Order createOrder() {
        Order order = new Order();

        View myView = getView();

        RadioButton orderEfectivoRadioButton = (RadioButton)myView.findViewById(R.id.pago_efectivo);
        RadioButton  orderTarjetaRadioButton = (RadioButton)myView.findViewById(R.id.pago_tarjeta);
        TextView  orderReferenceTextView = (TextView)myView.findViewById(R.id.address_reference);
        RadioButton  orderRecogerPedidoRadioButton = (RadioButton)myView.findViewById(R.id.recoger_pedido);
        RadioButton  orderActualAddressRadioButton = (RadioButton)myView.findViewById(R.id.actual_address);
        EditText addressReference = (EditText) myView.findViewById(R.id.address_reference);

        boolean esEfectivo = orderEfectivoRadioButton.isChecked();
        order.setPagoEfectivo(esEfectivo);

        boolean esTarjeta = orderTarjetaRadioButton.isChecked();
        order.setPagoEfectivo(esTarjeta);

        boolean recogerPedido = orderRecogerPedidoRadioButton.isChecked();
        order.setPasarRecoger(recogerPedido);

        boolean  actualAddress = orderActualAddressRadioButton.isChecked();
        order.setPasarRecoger(actualAddress);


        order.setUser(ParseUser.getCurrentUser());

        if (!recogerPedido) {
            String address = addressReference.getText().toString();
            order.setAddressReference(address);
        }
        return order;
    }

    private OrderItem mapToOrderItem(CartEntry entry, Order order) {
        OrderItem orderItem = new OrderItem();

        orderItem.setQuantity(entry.getQuantity());
        orderItem.setPrice(entry.getPrice());
        orderItem.setMedicine(entry.getMedicine());
        orderItem.setOrder(order);

        return orderItem;
    }


    @NonNull
    private ParseQuery<CartEntry> getAllEntriesOnCartQuery() {

        ParseQuery query = ParseQuery.getQuery("PharmacyMedicine")
                  .whereEqualTo("pharmacy", pharmacy);


        ParseQuery<CartEntry> innerQuery = new ParseQuery<>(CartEntry.class);
        ParseUser parseUser = ParseUser.getCurrentUser();
        innerQuery.whereEqualTo("owner", parseUser);
        innerQuery.include("pharmacyMedicine.pharmacy");
        innerQuery.include("pharmacyMedicine.medicine");

        innerQuery.whereMatchesQuery("pharmacyMedicine", query);
        return innerQuery;
    }

    @NonNull
    private ParseQuery<CartEntry> getCartEntryQuery() {
        ParseQuery<CartEntry> innerQuery = getAllEntriesOnCartQuery();

        return innerQuery;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }
}
