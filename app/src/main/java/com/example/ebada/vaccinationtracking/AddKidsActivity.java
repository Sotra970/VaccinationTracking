package com.example.ebada.vaccinationtracking;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import classes.CheckAge;
import classes.Kids;
import classes.Reminder;
import classes.SessionMangement;
import classes.VolleyConnection;
import classes.newReminder;
import custom_font.MyEditText;
import custom_font.MyTextView;
//URL TO CHANGE
// if you want to change alarm manger it is here at line 113
//copy and past the below line to test every 10 seconds
//alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),10000, operation);
public class AddKidsActivity extends AppCompatActivity {
    MyEditText name;
    MyTextView date;
    MyTextView add;
    RadioButton male, female;
    String gender;
    int years, month, day;
    Kids kid;
    SessionMangement sessionMangement;
    String URL="http://f1w4.com/vaccinations/add_kids.php";
   // String URL  = "http://192.168.18.2:1234/webservice/add_kids.php";
    AlertDialog.Builder builder;
    CheckAge checkAge;
    Calendar myCalendar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(AddKidsActivity.this);
        setContentView(R.layout.activity_add_kids);
        name = (MyEditText) findViewById(R.id.NameEdittext);
        date = (MyTextView) findViewById(R.id.date);
        add = (MyTextView) findViewById(R.id.Add);
        male = (RadioButton) findViewById(R.id.kmale);
        female = (RadioButton) findViewById(R.id.kfemale);

        View back = findViewById(R.id.Bhome);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        checkAge = new CheckAge(AddKidsActivity.this);
        myCalendar = Calendar.getInstance();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddKidsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                }, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("") || date.getText().toString().equals("") || male.getText().toString().equals("") || female.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Do not leave any fields", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (male.isChecked())
                    {
                        gender = "male";
                        female.setChecked(false);
                    }
                    else if (female.isChecked())
                    {
                        gender = "female";
                        male.setChecked(false);
                    }
                    addKids(new VolleyCallback() {
                        @Override
                        public void onSuccess(String r,Kids k) {

                            ShowDialouge(r);
                        }
                        });
                }
            }
        });
    }

    public void ShowDialouge (String value)
    {
        if (value.equals(" \nSuccess \n") == true)
        {

            builder.setMessage("Kid has added successfully");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent in  = new Intent(AddKidsActivity.this,HomeActiviy.class);
                    startActivity(in);
                }
            });
        }
        else if (value.equals(" \nSuccess \n"))
        {
            builder.setTitle("There Is Error");
            builder.setMessage("please try again");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void addKids(final VolleyCallback callback)
    {
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
        sessionMangement = new SessionMangement(AddKidsActivity.this);
        kid = new Kids();
        kid.setName(name.getText().toString());
        kid.setDate(date.getText().toString());
        kid.setGender(gender);
        kid.setParentID(sessionMangement.getID());
        StringRequest stringRequest  = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    callback.onSuccess(response,kid);
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String > kidd = new HashMap<String, String>();
                kidd.put("name",kid.getName());
                kidd.put("birth_date",kid.getDate());
                kidd.put("gender",kid.getGender());
                kidd.put("parent_id",kid.getParentID());
                return kidd;
            }
        };

        int socketTimeout = 10000; // 10 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                10,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);
        VolleyConnection.getsInsteance().getmRequestQueue().add(stringRequest);
    }
    public interface VolleyCallback{

        void onSuccess(String r , Kids kid);
    }
}