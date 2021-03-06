package com.example.roboticcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class Splashscreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                login();

            }
        }, 1000);
    }

    private void login() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        String logedin = sharedPreferences.getString("is_sign", "");

        if (logedin.equalsIgnoreCase("true")) {

            startActivity(new Intent(getApplicationContext(), Passcode.class));
        } else {
            startActivity(new Intent(getApplicationContext(), Loginpage.class));
        }
        finish();
    }


}