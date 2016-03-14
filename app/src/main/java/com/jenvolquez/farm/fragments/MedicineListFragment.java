package com.jenvolquez.farm.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jenvolquez.farm.R;
import com.jenvolquez.farm.parse.Medicine;
import com.jenvolquez.farm.parse.Pharmacy;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class MedicineListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseQuery<Medicine> query = ParseQuery.getQuery(Medicine.class);
        final  MedicineListFragment self= this;
        query.findInBackground(new FindCallback<Medicine>() {
            @Override
            public void done(List<Medicine> medicines, ParseException e) {
                MedicineAdapter medicineAdapter = new MedicineAdapter(self.getContext(), medicines);
                setListAdapter(medicineAdapter);
            }
        });

    }

}

class MedicineAdapter extends BaseAdapter {

    List<Medicine> medicines;
    List<Bitmap>  images;
    Context context;
    public MedicineAdapter (Context context, List<Medicine> medicines) {
        this.medicines = medicines;
        images = new ArrayList<>(medicines.size());
        for(int i =0; i < medicines.size(); i ++) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Medicine medicine = medicines.get(position);

        TextView nameTextView = null;
        TextView descriptionTextView = null;
        ImageView imageView = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.medicine_list_item, parent, false);
        }

        nameTextView = (TextView) convertView.findViewById(R.id.med_name);
        descriptionTextView = (TextView) convertView.findViewById(R.id.med_description);
        imageView = (ImageView) convertView.findViewById(R.id.med_thumb);

        nameTextView.setText(medicine.getName());
        descriptionTextView.setText(medicine.getDescription());
        Bitmap currentImage = images.get(position);
        if (currentImage == null) {
           final ImageView finalImgView = imageView;
            final int finalPos = position;
            medicine.getPhoto()
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

