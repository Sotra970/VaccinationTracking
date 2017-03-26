package com.example.ebada.vaccinationtracking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import classes.Clinic;

public class ClinicDetailesActivity extends AppCompatActivity {
    TextView name , phone  , adress , distance ;
    ExpandableHeightListView vacs_list ;
    Clinic extra_clinic ;
    private TextView YourClinics;
    private LinearLayout home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_detailes);

        YourClinics = (TextView) findViewById(R.id.act);
        home = (LinearLayout) findViewById(R.id.home);
        YourClinics.setText("Clincs");
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });




        extra_clinic = (Clinic) getIntent().getExtras().get("extra_clinic");

        name = (TextView) findViewById(R.id.name) ;
        phone = (TextView) findViewById(R.id.phone) ;
        adress = (TextView) findViewById(R.id.address) ;
        distance = (TextView) findViewById(R.id.distance) ;

        name.setText(extra_clinic.getName());
        phone.setText(extra_clinic.getPhone());
        adress.setText(extra_clinic.getAddress());
        distance.setText(extra_clinic.getDistance());

        vacs_list = (ExpandableHeightListView) findViewById(R.id.vacs_list) ;
        vacs_list.setExpanded(true);
        ClinicAdapter adapter =  new ClinicAdapter(getApplicationContext(),R.id.vacName,extra_clinic.getVacs());
        vacs_list.setAdapter(adapter);


    }


    private class ClinicAdapter extends ArrayAdapter<String> {

        private ArrayList<String> items;

        public ClinicAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                //inflate a new view for your list item
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.clinic_vac_item, null);
            }
            String item = items.get(position);
            //set text to view
            TextView vacName = (TextView) v.findViewById(R.id.vacName);
            vacName.setText(item);
            return v;
        }
    }

}
