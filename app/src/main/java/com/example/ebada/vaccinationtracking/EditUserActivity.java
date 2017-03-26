package com.example.ebada.vaccinationtracking;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import classes.SessionMangement;
import classes.Validation;
import classes.VolleyConnection;
import custom_font.MyEditText;
import custom_font.MyTextView;
//URL TO CHANGE
public class EditUserActivity extends AppCompatActivity {
    SessionMangement sessionMangement;
    Validation validation;
    MyEditText name,email,password,RePassword,phone,city,address;
    MyTextView update;
    String URL = "http://f1w4.com/vaccinations/update_user_profile.php";
    AlertDialog.Builder builder;
    String Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        builder = new AlertDialog.Builder(EditUserActivity.this);
        name = (MyEditText) findViewById(R.id.Ename);
        email = (MyEditText) findViewById(R.id.Edmail);
        password = (MyEditText) findViewById(R.id.Epass);
        RePassword = (MyEditText) findViewById(R.id.ERePass);
        phone = (MyEditText) findViewById(R.id.EPhoneNum);
        city = (MyEditText) findViewById(R.id.ECity);
        address = (MyEditText) findViewById(R.id.Eaddress);
        update = (MyTextView) findViewById(R.id.Eupdate);
        sessionMangement = new SessionMangement(EditUserActivity.this);
        validation = new Validation(EditUserActivity.this);
        View back = findViewById(R.id.Bhome);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (sessionMangement.CheckLogin() == true)
        {
            name.setText(sessionMangement.getName());
            email.setText(sessionMangement.getEmail());
            phone.setText(sessionMangement.getphone());
            city.setText(sessionMangement.getCity());
            address.setText(sessionMangement.getAddress());
            password.setText(sessionMangement.getPassword());
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     if (password.getText().toString().equals(RePassword.getText().toString()) == false)
                        RePassword.setError("passwords do not matches");
                    else
                    {
                        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                                ShowDialouge(response.toString());
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
                                Map<String , String > param = new HashMap<String, String>();
                                if (password.getText().toString().equals(""))
                                {Password =sessionMangement.getPassword();}
                                else
                                {Password = password.getText().toString();}
                                param.put("user_id",sessionMangement.getID());
                                param.put("name",name.getText().toString());
                                param.put("email",email.getText().toString());
                                param.put("phone",phone.getText().toString());
                                param.put("password",Password);
                                param.put("gender",sessionMangement.getGender());
                                param.put("city",city.getText().toString());
                                param.put("address",address.getText().toString());
                                return param;
                            }
                        };

                        int socketTimeout = 10000; // 10 seconds. You can change it
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                10,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                        stringRequest.setRetryPolicy(policy);
                        VolleyConnection.getsInsteance().getmRequestQueue().add(stringRequest);
                    }

                }
            });
        }
        else
        {
            Toast.makeText(EditUserActivity.this, "Sorry you have to login first", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(EditUserActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
    public void ShowDialouge (String value)
    {
        if (value.equals(" \nSuccess \n") == true)
        {
            String a = "true";
            builder.setMessage("Update has done successfully");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent in  = new Intent(EditUserActivity.this,HomeActiviy.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                }
            });
        }
        else if (value.equals(" \nSuccess \n"))
        {
            builder.setMessage("There is an error");
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
