package com.jenvolquez.farm.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.wallet.Cart;
import com.jenvolquez.farm.R;
import com.jenvolquez.farm.parse.CartEntry;
import com.jenvolquez.farm.parse.Medicine;
import com.jenvolquez.farm.parse.Pharmacy;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bolts.Continuation;
import bolts.Task;

public class MedicineListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String pharmacyId = this.getArguments().getString("pharmacyId");
        ParseQuery<ParseObject> outerQuery = ParseQuery.getQuery("Pharmacy")
                  .whereEqualTo("objectId", pharmacyId);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("PharmacyMedicine");
        query.include("medicine");
        query.include("pharmacy");
        query.whereMatchesQuery("pharmacy", outerQuery);



        final MedicineListFragment self = this;

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> medicines, ParseException e) {

                MedicineAdapter medicineAdapter = new MedicineAdapter(self.getContext(), medicines);
                setListAdapter(medicineAdapter);
            }
        });

    }
}

class MedicineAdapter extends BaseAdapter {

    List<ParseObject> medicines;
    List<Bitmap> images;
    Context context;

    public MedicineAdapter(Context context, List<ParseObject> medicines) {
        this.medicines = medicines;
        images = new ArrayList<>(medicines.size());
        for (int i = 0; i < medicines.size(); i++) {
            images.add(null);
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return medicines.size();
    }

    @Override
    public Object getItem(int position) {
        return medicines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ParseObject pharmacyMedicine = medicines.get(position);
        final ParseObject medicine = pharmacyMedicine.getParseObject("medicine");
        final ParseObject pharmacyName = pharmacyMedicine.getParseObject("pharmacy");
        TextView nameTextView = null;
        TextView descriptionTextView = null;
        ImageView imageView = null;
        ImageButton button = null;
        TextView priceTextView = null;
        TextView pharmacynameTextView = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.medicine_list_item, parent, false);
        }

        nameTextView = (TextView) convertView.findViewById(R.id.med_name);
        descriptionTextView = (TextView) convertView.findViewById(R.id.med_description);
        imageView = (ImageView) convertView.findViewById(R.id.med_thumb);
        priceTextView = (TextView) convertView.findViewById(R.id.price_medicine);
        pharmacynameTextView= (TextView) convertView.findViewById(R.id.pharmacy_name);

        nameTextView.setText(medicine.getString("name"));
        descriptionTextView.setText(medicine.getString("description"));
        final Double price = pharmacyMedicine.getDouble("price");
        priceTextView.setText(String.valueOf(price));


        pharmacynameTextView.setText(pharmacyName.getString("name"));


        Bitmap currentImage = images.get(position);
        if (currentImage == null) {
            final ImageView finalImgView = imageView;
            final int finalPos = position;
            medicine.getParseFile("medphoto")
                    .getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
                            if (finalImgView != null)
                                finalImgView.setImageBitmap(image);
                            images.set(finalPos, image);
                        }
                    });
        } else {
            imageView.setImageBitmap(currentImage);
        }

        final Context context = this.context;
        button = (ImageButton) convertView.findViewById(R.id.buttons);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<CartEntry> query = new ParseQuery<>(CartEntry.class);
                query.whereEqualTo("pharmacyMedicine", pharmacyMedicine);
                  query.getFirstInBackground(new GetCallback<CartEntry>() {
                      @Override
                      public void done(CartEntry object, ParseException e) {
                          if (object != null) {
                              object.incrementQuantity();
                              object.saveInBackground(new SaveCallback() {
                                  @Override
                                  public void done(ParseException e) {
                                      Toast.makeText(context, "Se ha agregado este producto ", Toast.LENGTH_LONG).show();
                                  }
                              });
                          } else {
                              CartEntry cartEntry = new CartEntry();
                              cartEntry.put("pharmacyMedicine", pharmacyMedicine);
                              cartEntry.put("quantity", 1);
                              cartEntry.put("owner", ParseUser.getCurrentUser());
                              cartEntry.saveInBackground(new SaveCallback() {
                                  @Override
                                  public void done(ParseException e) {
                                      Toast.makeText(context, "Se ha agregado este producto ", Toast.LENGTH_LONG).show();
                                  }
                              });
                          }
                      }
                  });
            }
        });
        return convertView;
    }
}

