package com.jenvolquez.farm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.jenvolquez.farm.R;
import com.jenvolquez.farm.parse.ContactInformation;
import com.parse.ParseException;
import com.parse.SaveCallback;

/**
 * Created by Jen Volquez on 3/9/2016.
 */
public class ContactFragment extends Fragment implements View.OnClickListener {

    View myView;
    private EditText nameedit;
    private EditText subjectedit;
    private EditText messageedit;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.contact_layout, container, false);

  nameedit = (EditText) myView.findViewById(R.id.name_edit);
        nameedit.setOnClickListener(this);

        subjectedit = (EditText) myView.findViewById(R.id.subject_edit);
        subjectedit.setOnClickListener(this);

        messageedit = (EditText) myView.findViewById(R.id.message_edit);
        messageedit.setOnClickListener(this);
        button = (Button) myView.findViewById(R.id.send_button);
        button.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onClick(View v) {
        String name = nameedit.getText().toString();
        String subject = subjectedit.getText().toString();
        String information = messageedit.getText().toString();

        ContactInformation contactInfo = new ContactInformation();
        contactInfo.setName(name);
        contactInfo.setSubjectInfor(subject);
        contactInfo.setInformation(information);

        contactInfo.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getContext(), "Se ha enviado exitosamente", Toast.LENGTH_LONG).show();
            }
        });
    }
}
