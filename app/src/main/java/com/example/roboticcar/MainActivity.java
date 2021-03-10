package com.example.roboticcar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String datamsg;
    String datetimemsg;

    String loginusername;
    String msgdatapath;

    private long backPressedTime;
    private Toast backToast;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String previouskey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = getApplicationContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        loginusername = sharedPreferences.getString("username", "");
        previouskey = sharedPreferences.getString("key", "");

        msgdatapath = loginusername.replace(".", "_");


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationitemselectedlistener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new Home()).commit();


        databaseReference.child(msgdatapath).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String childkey = snapshot.getKey();
                if (!childkey.equals(previouskey)) {

                    datamsg = (String) snapshot.child("Data").getValue();
                    datetimemsg = (String) snapshot.child("DateTime").getValue();

                    Intent serviceIntent = new Intent(MainActivity.this, exservice.class);
                    serviceIntent.putExtra("datamsg", datamsg);
                    serviceIntent.putExtra("datetimemsg", datetimemsg);
                    startService(serviceIntent);
                    //ContextCompat.startForegroundService(MainActivity.this, serviceIntent);

                    editor = sharedPreferences.edit();
                    editor.putString("key", childkey).apply();

                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

    }


    private final BottomNavigationView.OnNavigationItemSelectedListener navigationitemselectedlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedfragment = null;
            switch (item.getItemId()) {
                case R.id.homebutton:
                    selectedfragment = new Home();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    break;

                case R.id.notificationbutton:
                    selectedfragment = new Notificationpage();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                    break;

                case R.id.internet_modebutton:
                    selectedfragment = new Onlinemode();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                    break;

                case R.id.profilebutton:
                    selectedfragment = new Profile();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                    break;
            }

            assert selectedfragment != null;

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, selectedfragment).commit();
            return true;
        }
    };


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}