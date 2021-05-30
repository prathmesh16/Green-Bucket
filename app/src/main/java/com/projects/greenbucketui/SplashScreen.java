package com.projects.greenbucketui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    private SharedPreferences adr_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adr_save=getSharedPreferences("profile",MODE_PRIVATE);

        String stat =adr_save.getString("status","Default");

        if(stat.equalsIgnoreCase("true"))
        {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }
        else{
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            finish();
        }

    }
}