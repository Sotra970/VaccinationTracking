package com.example.ebada.vaccinationtracking;

import android.*;
import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import classes.CheckAge;
import classes.Kids;
import classes.Reminder;
import classes.SessionMangement;
import classes.Vaccination;
import classes.VolleyConnection;
import classes.newReminder;
import custom_font.MyEditText;
import custom_font.MyTextView;
//URL TO CHANGE IN getVaccinations()
// URL TO CHANGE IN
public class HomeActiviy extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_GPS = 66;

    TextView nn;
    SessionMangement sessionMangement;
    LinearLayout l1,l2,l3,l4,logiut;
    MyTextView button;
    MyEditText textView;
    CheckAge checkAge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_activiy);
        Toolbar actionToolBar = (Toolbar) findViewById(R.id.my_action_bar_toolbar);
        setSupportActionBar(actionToolBar);
        sessionMangement = new SessionMangement(HomeActiviy.this);
        checkAge = new CheckAge(HomeActiviy.this);


        getVaccinations();
        if (sessionMangement.CheckLogin() == true) {
            Calendar calendar = Calendar.getInstance();
            Intent intent = new Intent(HomeActiviy.this,newReminder.class);
            intent.putExtra("id",sessionMangement.getID());
            intent.putParcelableArrayListExtra("paraam",checkAge.getVaccinations());
            PendingIntent pendingIntent = PendingIntent.getService(HomeActiviy.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),20000,pendingIntent);
            l1 = (LinearLayout) findViewById(R.id.l1);
            l2 = (LinearLayout) findViewById(R.id.l2);
            l3 = (LinearLayout) findViewById(R.id.l3);
            l4 = (LinearLayout) findViewById(R.id.l4);
            logiut = (LinearLayout) findViewById(R.id.logoutt);
            l1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(HomeActiviy.this, AddKidsActivity.class);
                    startActivity(in);
                }
            });

            l3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inn = new Intent(HomeActiviy.this, ShowKids.class);
                    startActivity(inn);
                }
            });

            l4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActiviy.this, EditUserActivity.class);
                    startActivity(intent);
                }
            });

            l2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    no_storage_permission();

                }
            });

            logiut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sessionMangement.logoutUser();
                    Intent intent = new Intent(HomeActiviy.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }
        else
        {

        }
    }



    private void getVaccinations() {
        if (checkAge.getVaccinations() == null) {
            String url = "http://f1w4.com/vaccinations/vaccinations.php";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ArrayList<Vaccination> vaccinations = new ArrayList<>();
                    JSONObject jsonObject;
                    Vaccination vaccination;
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            vaccination = new Vaccination();
                            vaccination.setId(jsonObject.getString("id"));
                            vaccination.setNum_month(jsonObject.getInt("num_month"));
                            vaccination.setName_vacc(jsonObject.getString("name_vacc"));
                            vaccination.setDescription(jsonObject.getString("description"));
                            vaccinations.add(vaccination);
                            checkAge.addToVaccinations(vaccination);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            VolleyConnection.getsInsteance().getmRequestQueue().add(stringRequest);
        }
        else
        {}
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_GPS: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    start_show_clinci();
                } else {
                    //  no_gps_permition();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission needed to run your app", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void start_show_clinci() {
        Intent intent = new Intent(HomeActiviy.this, ShowClinks.class);
        startActivity(intent);
    }

    private void no_storage_permission() {
        // Here, thisActivity is the current activity
        if ( (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_GPS);


        }else{
            start_show_clinci();
        }


    }




}


