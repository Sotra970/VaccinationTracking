package com.example.ebada.vaccinationtracking;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import classes.ADAPTER;
import classes.Kids;
import classes.VaccinationAdapter;
import classes.VolleyConnection;
import classes.kidsVaccination;
// URL TO CHANGE
public class ShowvaccinationActivity extends AppCompatActivity {
    Intent intent;
    String id;
    String URL = "http://f1w4.com/vaccinations/kids_vaccination.php";
    ArrayList<kidsVaccination> vacss;
    VaccinationAdapter adapter;
    ListView vacs;
    int status ;
    private TextView YourKids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showvaccination);
        intent = getIntent();
        id = intent.getStringExtra("kidId");
        vacs = (ListView) findViewById(R.id.listVac);

        Toolbar actionToolBar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(actionToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout home = (LinearLayout) findViewById(R.id.home);
        YourKids = (TextView) findViewById(R.id.act);
        YourKids.setText("Vaccinations ");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   = new Intent(getApplicationContext(),HomeActiviy.class);
                ComponentName cn = intent.getComponent();
                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                startActivity(mainIntent);
            }
        });
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);
                if (!response.toString().trim().equals("Empty"))
                {
                    kidsVaccination kd ;
                    vacss = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject;
                        for (int i = 0 ; i < jsonArray.length() ; i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            kd = new kidsVaccination();
                            kd.setName(jsonObject.getString("name_vacc"));
                            kd.setMonth_num(jsonObject.getString("num_month"));
                            kd.setDescription(jsonObject.getString("description"));
                            status = jsonObject.getInt("status");
                            if (status == 0)
                                kd.setStatus("not taken");
                            else if(status == 1)
                                kd.setStatus("taken");
                            vacss.add(kd);
                        }
                        adapter = new VaccinationAdapter(ShowvaccinationActivity.this,vacss);
                        vacs.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowvaccinationActivity.this);
                    builder.setMessage("There is no vaccination for your kid");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //Intent in  = new Intent(ShowvaccinationActivity.this,HomeActiviy.class);
                          //  in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          //  startActivity(in);
                            finish();
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
                param.put("kids_id",id);
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

