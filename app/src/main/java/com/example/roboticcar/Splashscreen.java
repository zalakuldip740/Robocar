package com.example.roboticcar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Splashscreen extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        firebaseAuth = FirebaseAuth.getInstance();


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
        if (firebaseAuth.getCurrentUser() != null) {

            finish();
            startActivity(new Intent(getApplicationContext(), Passcode.class));
        }
        else{
            startActivity(new Intent(getApplicationContext(), Loginpage.class));
            finish();
        }
    }


}