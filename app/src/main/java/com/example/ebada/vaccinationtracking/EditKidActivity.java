package com.example.ebada.vaccinationtracking;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

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
import classes.VolleyConnection;
import custom_font.MyEditText;
import custom_font.MyTextView;
// URL TO CHANGE
public class EditKidActivity extends AppCompatActivity {
    Intent intent;
    Kids kid;
    MyEditText name;
    MyTextView date;
    MyTextView Update;
    int years, month, day;
    String URL= "http://f1w4.com/vaccinations/update_kids.php";

    AlertDialog.Builder builder;
    CheckAge checkAge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kid);
        builder = new AlertDialog.Builder(EditKidActivity.this);
        intent = getIntent();
        kid = intent.getParcelableExtra("kid");
        name = (MyEditText) findViewById(R.id.EName);
        date = (MyTextView) findViewById(R.id.Edate);
        Update = (MyTextView) findViewById(R.id.Update);
        checkAge  =  new CheckAge(EditKidActivity.this);
        View back = findViewById(R.id.Bhome);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        name.setText(kid.getName());
        date.setText(kid.getDate());
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                years = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditKidActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },
                        calendar.get(Calendar.DAY_OF_MONTH),
                 calendar.get(Calendar.YEAR),
                 calendar.get(Calendar.MONTH)
                );
                datePickerDialog.show();
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        ShowDialouge(response);
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
                        kidd.put("name",name.getText().toString());
                        kidd.put("birth_date",date.getText().toString());
                        kidd.put("gender",kid.getGender());
                        kidd.put("parent_id",kid.getParentID());
                        kidd.put("id",kid.getID());
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
        });
    }

    public void ShowDialouge (String value)
    {
        if (value.equals(" \nSuccess \n") == true)
        {
            builder.setMessage("Kidd has Updated successfully");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent in  = new Intent(EditKidActivity.this,HomeActiviy.class);
                    startActivity(in);
                }
            });
        }
        else if (value.equals(" \nSuccess \n"))
        {
            builder.setTitle("There is Error");
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
}
