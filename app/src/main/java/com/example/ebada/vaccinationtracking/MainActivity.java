package com.example.ebada.vaccinationtracking;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import classes.ADAPTER;
import classes.Kids;
import classes.SessionMangement;
import classes.VolleyConnection;
import custom_font.MyTextView;

//URL TO CHANGE
public class MainActivity extends AppCompatActivity {
    TextView welcome,login;
    MyTextView create;
    RequestQueue requestQueue;
    EditText userName,password;
    String UserName,PassWord;
   // String URL = "http://192.168.1.103:1234/webservice/login.php";
      String URL = "http://f1w4.com/vaccinations/login.php";
    AlertDialog.Builder builder;
    SessionMangement sessionMangement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface custom_fonts = Typeface.createFromAsset(getAssets(), "Swistblnk Duwhoers Brush.ttf");
        welcome = (TextView) findViewById(R.id.zoo);
        welcome.setTypeface(custom_fonts);
        login = (TextView) findViewById(R.id.login);
        userName = (EditText) findViewById(R.id.UserName);
        password = (EditText) findViewById(R.id.PassWord);
        create = (MyTextView) findViewById(R.id.reg);
        builder = new AlertDialog.Builder(MainActivity.this);
        sessionMangement = new SessionMangement(MainActivity.this);
        if (sessionMangement.CheckLogin() != true)

        {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userName.getText().toString().equals("") || password.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                        userName.setText("");
                        password.setText("");
                    } else {
                        UserName = userName.getText().toString();
                        PassWord = password.getText().toString();
                        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e(" login response" , response);
                                if (response.toString().trim().equals("Empty")) {
                                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);

                                    ShowDialouge();
                                } else {
                                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);

                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        sessionMangement.createLoginSession(jsonObject.getString("name"), jsonObject.getString("email"), jsonObject.getString("password"), jsonObject.getString("phone"), jsonObject.getString("city"), jsonObject.getString("address"), jsonObject.getString("gender"), jsonObject.getString("id"));
                                        Intent intent = new Intent(MainActivity.this, HomeActiviy.class);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                                if (error != null) {
                                    ShowDialouge();
                                }
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> LOGIN = new HashMap<String, String>();
                                LOGIN.put("username", UserName);
                                LOGIN.put("password", PassWord);
                                return LOGIN;
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
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registeration.class);
                startActivity(intent);
            }
        });
    }
        else
        {
            Intent intent = new Intent(this,HomeActiviy.class);
            startActivity(intent);
            finish();
        }
    }
    public void ShowDialouge ()
    {
            builder.setTitle("Login Failed");
            builder.setMessage("please try again");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
