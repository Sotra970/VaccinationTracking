package com.example.ebada.vaccinationtracking;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import classes.KidHistoryAdapter;
import classes.Kids;
import classes.VacHistory;
import classes.VolleyConnection;
import custom_font.MyTextView;

public class ShowKidHistory extends AppCompatActivity {
    ListView listView;
    Intent intent;
    String kidID;
    KidHistoryAdapter kidHistoryAdapter;
    ArrayList<VacHistory> histories;
    LinearLayout home;
    String URL = "http://f1w4.com/vaccinations/kids_history.php";
    private TextView YourKids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_kid_history);
        Toolbar actionToolBar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(actionToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.historylist);
        home = (LinearLayout) findViewById(R.id.home);
        intent = getIntent();
        kidID = intent.getStringExtra("kidId");
        YourKids = (TextView) findViewById(R.id.act);
        YourKids.setText("History ");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   = new Intent(ShowKidHistory.this,HomeActiviy.class);
                ComponentName cn = intent.getComponent();
                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                startActivity(mainIntent);
            }
        });
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String aa = response;
                if ((response.toString().equals("Empty")) == false)
                {
                    histories = new ArrayList<>();
                    VacHistory vacHistory;
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0 ; i <jsonArray.length() ; i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            vacHistory = new VacHistory();
                            vacHistory.setSymptom(jsonObject.getString("symptoms"));
                            vacHistory.setPharmaceutical(jsonObject.getString("pharmaceutical"));
                            histories.add(vacHistory);
                        }
                        kidHistoryAdapter = new KidHistoryAdapter(ShowKidHistory.this,histories);
                        listView.setAdapter(kidHistoryAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowKidHistory.this);
                    builder.setMessage("There is no History for your kid");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                           supportFinishAfterTransition();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
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
                param.put("kids_id",kidID);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
