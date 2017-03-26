package com.example.ebada.vaccinationtracking;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

import classes.Parent;
import classes.Validation;
import classes.VolleyConnection;
import custom_font.MyEditText;
import custom_font.MyTextView;
//URL TO CHANBGE
public class Registeration extends AppCompatActivity {
    MyTextView signin1;
    MyEditText name,mail,pass,RePass,phoneNum,city,address;
    RadioButton male,female;
    MyTextView Rehisteration;
    //String URL = "http://192.168.56.1:1234/webservice/Reg.php";
   // String URL = "http://experttk.com/vaccinations/register.php";
    String URL = "http://f1w4.com/vaccinations/register.php";

    AlertDialog.Builder builder;
    Parent parent;
    Validation validation;
    String gender ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        builder = new AlertDialog.Builder(Registeration.this);
        name = (MyEditText) findViewById(R.id.name);
        mail = (MyEditText) findViewById(R.id.mail);
        pass = (MyEditText) findViewById(R.id.pass);
        RePass = (MyEditText) findViewById(R.id.RePass);
        phoneNum = (MyEditText) findViewById(R.id.PhoneNum);
        city = (MyEditText) findViewById(R.id.city);
        address = (MyEditText) findViewById(R.id.address);
        male= (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        Rehisteration = (MyTextView) findViewById(R.id.Rehisteration);
        validation = new Validation(Registeration.this);
        signin1 = (MyTextView)findViewById(R.id.signin1);
        signin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registeration.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Rehisteration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("") ||mail.getText().toString().equals("") || pass.getText().toString().equals("") || pass.getText().toString().equals("") || RePass.getText().toString().equals("")||
                        phoneNum.getText().toString().equals("") || address.getText().toString().equals("") || male.getText().toString().equals("") || female.getText().toString().equals("") || city.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                    pass.setText("");
                    RePass.setText("");
                }
                else if (!(pass.getText().toString().equals(RePass.getText().toString()))){
                    RePass.setError("passwords do not matches");
                }
                else if ((validation.EmailValidtaion(mail.getText().toString())) == false)
                                 mail.setError("Please enter a valid mail");
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
                            findViewById(R.id.loadingSpinner).setVisibility(View.VISIBLE);
                            parent = new Parent();
                            parent.setName(name.getText().toString());
                            parent.setEmail(mail.getText().toString());
                            parent.setPassword(pass.getText().toString());
                            parent.setPhone(phoneNum.getText().toString());
                            parent.setAddress(address.getText().toString());
                            parent.setCity(city.getText().toString());
                            parent.setGender(gender);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                                            String a = response;
                                            ShowDialouge(response);
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    findViewById(R.id.loadingSpinner).setVisibility(View.GONE);
                                    String hh = error.toString();
                                    if (error.toString() != null)
                                    {}
                                }
                            })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String , String > user = new HashMap<String, String>();
                                    user.put("name",parent.getName());
                                    user.put("email",parent.getEmail());
                                    user.put("password",parent.getPassword());
                                    user.put("phone",parent.getPhone());
                                    user.put("city",parent.getCity());
                                    user.put("address",parent.getAddress());
                                    user.put("gender",parent.getGender());
                                    return user;
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
    public void ShowDialouge (String value)
    {

        if (value.equals("Success ") == true)
        {
            builder.setMessage("you have just registered successfully");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent in  = new Intent(Registeration.this,MainActivity.class);
                    startActivity(in);
                }
            });
        }
        else if (value.equals("Success "))
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
}



