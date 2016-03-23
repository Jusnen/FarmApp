package com.jenvolquez.farm.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.jenvolquez.farm.parse.Pharmacy;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;


import com.jenvolquez.farm.R;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jen Volquez on 3/10/2016.
 */
public class PharmacyListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ParseQuery<Pharmacy> query = ParseQuery.getQuery(Pharmacy.class);
        final PharmacyListFragment self = this;
        query.findInBackground(new FindCallback<Pharmacy>() {

            @Override
            public void done(List<Pharmacy> pharmacies, ParseException e) {
                PharmacyAdapter pharmacyAdapter = new PharmacyAdapter(self.getContext(), pharmacies);
                setListAdapter(pharmacyAdapter);

            }
        });


    }

}

class PharmacyAdapter extends BaseAdapter {

    List<Pharmacy> pharmacies;
    List<Bitmap> images;
    Context context;
    public  PharmacyAdapter(Context context, List<Pharmacy> pharmacies){
        this.pharmacies= pharmacies;
        images= new ArrayList<>(pharmacies.size());
        for (int i = 0; i<pharmacies.size(); i++){
            images.add(null);
        }
        this.context= context;
    }


    @Override
    public int getCount() {
        return pharmacies.size();
    }

    @Override
    public Object getItem(int position) {
        return pharmacies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pharmacy pharmacy = pharmacies.get(position);

        TextView nameTextView = null;
        TextView addressTextView = null;
        ImageView imageView = null;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.farm_list_item, parent,false);
        }

        nameTextView = (TextView) convertView.findViewById(R.id.farm_name);
        addressTextView = (TextView) convertView.findViewById(R.id.farm_address);
        imageView = (ImageView) convertView.findViewById(R.id.med_thumb);

        nameTextView.setText(pharmacy.getName());
        addressTextView.setText(pharmacy.getAddress());
        Bitmap currentImage = images.get(position);
        if (currentImage == null) {
            final ImageView finalImgView = imageView;
            final int finalPos = position;
            pharmacy.getPhoto()
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

        return convertView;
    }
}


