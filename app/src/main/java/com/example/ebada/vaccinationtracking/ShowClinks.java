package com.example.ebada.vaccinationtracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ebada.vaccinationtracking.MapUtiles.LatLngInterpolator;
import com.example.ebada.vaccinationtracking.MapUtiles.LocationReq;
import com.example.ebada.vaccinationtracking.MapUtiles.markerAnimator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import classes.ADAPTER;
import classes.Clinic;
import classes.ClinicAdapter;
import classes.Kids;
import classes.VolleyConnection;
//URL TO CHANGE
public class ShowClinks extends AppCompatActivity  implements OnMapReadyCallback {
    Intent intent;
    String city;
    String URL = "http://f1w4.com/vaccinations/clinic.php";
    ClinicAdapter adapter;
    ArrayList<Clinic> clinics;
    ListView Clinics;
    LinearLayout home;
    TextView YourClinics;
    private GoogleMap mMap;
    LocationReq gpsTracker;
    private Marker currentPosIcon;
    private ArrayList<Marker> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_clinks);
        Toolbar actionToolBar = (Toolbar) findViewById(R.id.my_action_bar_toolbar2);
        setSupportActionBar(actionToolBar);
        registerReceiver(mReceivedSMSReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
        checkGpsProvider();

        YourClinics = (TextView) findViewById(R.id.act);
        home = (LinearLayout) findViewById(R.id.home);
        YourClinics.setText("Clincs");
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });



        setupMap();



    }

    private void onCurrentLocationUpdated(Location location) {

        if (gpsTracker.getLatitude() != 0) {
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            //currentPosIcon.setRotation((float) SphericalUtil.computeHeading(currentPosIcon.getPosition(),currentLoc));

            get_clinics(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            new markerAnimator().animateMarkerToICS(currentPosIcon, currentLoc, new LatLngInterpolator.Spherical(), 1200);
            moveCamera(currentLoc);
            gpsTracker.disconnect();
        }

    }


    private void setupMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int x = markers.indexOf(marker);

            }
        });
        gpsTracker = new LocationReq(getApplicationContext() , new LocationReq.update_location() {
            @Override
            public void update_location(Location location) {
                onCurrentLocationUpdated(location);
            }
        },1000);
        createCurrentPosMarker();


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int temp_marker_tag =(int) marker.getTag() ;

                open_vac_info(clinics.get(temp_marker_tag));
            }
        });


    }

    private void open_vac_info(Clinic clinic) {
        Intent intent = new Intent(getApplicationContext() , ClinicDetailesActivity.class) ;
        intent.putExtra("extra_clinic" , clinic) ;
        startActivity(intent);
    }


    private void createCurrentPosMarker() {
        BitmapDrawable home =(BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_home_pin);
        home.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(home.getBitmap());

        MarkerOptions markerOptions = null;
        markerOptions = new MarkerOptions().position(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude())).icon(icon);


        currentPosIcon = mMap.addMarker(markerOptions);

    }
    private void moveCamera(LatLng current) {
        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(current)      // Sets the center of the map to Mountain View
                .zoom(13)                   // Sets the zoom
                .bearing(30)                // Sets the orientation of the camera to east
                .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000, null);
    }

    @Override
    protected void onDestroy() {
        gpsTracker.disconnect();
        super.onDestroy();
    }



    void get_clinics(final String lat , final String lung){
        findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response_clinic" , response);
                if (response.toString().equals("Empty"))
                {
                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext() , "no clincis nearby" , Toast.LENGTH_LONG).show();


                }
                else
                {
                    clinics = new ArrayList<>();
                    Clinic clinic;
                    try {
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0 ; i<jsonArray.length() ; i++) {
                            JSONObject  jsonObject  = jsonArray.getJSONObject(i);
                            clinic = new Clinic();
                            clinic.setID(jsonObject.getString("id"));
                            clinic.setName(jsonObject.getString("clinic_name"));
                            clinic.setPhone(jsonObject.getString("phone"));
                            clinic.setCity(jsonObject.getString("city"));
                            clinic.setAddress(jsonObject.getString("address"));
                            clinic.setDistance(jsonObject.getString("distance"));
                            ArrayList<String> vaccs = new ArrayList<>();
                            JSONArray vaccs_obg = jsonObject.getJSONArray("vacs") ;

                            for (int h=0 ; h<vaccs_obg.length() ; h++){
                                vaccs.add(vaccs_obg.getString(h));
                            }

                            clinic.setVacs(vaccs);

                            String snp = "Distance: " + ( (int)(Double.valueOf(jsonObject.getString("distance"))*1000) )+"meter" +"\r\n";
//                                    snp += "phone: " + jsonObject.getString("phone") +"\r\n";
//                                    snp += "adress: " + jsonObject.getString("address") +"\r\n";


                            Marker temp_marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(
                                                    Double.valueOf(jsonObject.getString("lat") )
                                                    ,Double.valueOf(jsonObject.getString("lung") )
                                            )
                                    )
                                    .title(clinic.getName())
                                    .snippet( snp));
                            temp_marker.setTag(i);
                            markers.add(temp_marker);
                            temp_marker.showInfoWindow();

                            clinics.add(clinic);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext() , "check your internet connection" , Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String > params = new HashMap<String, String>();
                params.put("lat",lat);
                params.put("lung",lung);
                return params;
            }
        };
        int socketTimeout = 10000; // 10 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                10,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);
        VolleyConnection.getsInsteance().getmRequestQueue().add(stringRequest);

    }

    private final BroadcastReceiver mReceivedSMSReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED"))
            {
                Log.e("GpsProviderReciver", String.valueOf(intent.getAction()));
                checkGpsProvider();
            }
        }
    };


    private  void  checkGpsProvider(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final android.app.AlertDialog alert = builder.create();
            alert.show();        }
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mReceivedSMSReceiver);
        super.onStop();
    }

    @Override
    protected void onResume() {
        registerReceiver(mReceivedSMSReceiver , new IntentFilter("android.location.PROVIDERS_CHANGED"));
        super.onResume();
    }


}
