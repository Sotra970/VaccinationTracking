package com.example.ebada.vaccinationtracking;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import classes.ADAPTER;
import classes.Kids;
import classes.SessionMangement;
import classes.VolleyConnection;

// URL TO CHANGE
public class ShowKids extends AppCompatActivity {
    SessionMangement sessionMangement;
    ArrayList<Kids> kids;
    ADAPTER adapter;
    String URL = "http://f1w4.com/vaccinations/kids.php";
    ListView listView;
    TextView YourKids;
    LinearLayout home;
    AlertDialog.Builder builder ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_kids2);
        Toolbar actionToolBar = (Toolbar) findViewById(R.id.my_action_bar_toolbar2);
        setSupportActionBar(actionToolBar);
        listView = (ListView) findViewById(R.id.MainList);
        YourKids = (TextView) findViewById(R.id.act);
        home = (LinearLayout) findViewById(R.id.home);
        YourKids.setText("Your Kids ");
        sessionMangement = new SessionMangement(ShowKids.this);
        builder = new AlertDialog.Builder(ShowKids.this);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Empty") == true) {
                    builder.setTitle("Sorry");
                    builder.setMessage("you do not have kids yet");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(ShowKids.this,HomeActiviy.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else
                {
                    Kids kid;
                    kids = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject;
                        for (int i = 0 ; i<jsonArray.length() ; i++) {
                            jsonObject  = jsonArray.getJSONObject(i);
                            kid = new Kids();
                            kid.setID(jsonObject.getString("id"));
                            kid.setName(jsonObject.getString("name"));
                            kid.setDate(jsonObject.getString("birth_date"));
                            kid.setGender(jsonObject.getString("gender"));
                            kid.setParentID(jsonObject.getString("parent_id"));
                            kids.add(kid);
                        }
                        adapter = new ADAPTER(ShowKids.this,kids);
                        listView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String > param = new HashMap<String, String>();
                param.put("parent_id",sessionMangement.getID());
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
