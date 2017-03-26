package com.example.ebada.vaccinationtracking;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SpalshScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView text ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(),"PassionOne-Black.ttf");
        text = (TextView) findViewById(R.id.font);
        text.setTypeface(myTypeface);
        Thread SplashThread  =  new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        SplashThread.start();
    }
}
