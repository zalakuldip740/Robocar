package com.example.roboticcar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
    private long backPressedTime;
    private Toast backToast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationitemselectedlistener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, new Home()).commit();


        databaseReference.child("businesses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                datamsg = (String) snapshot.child("Data").getValue();
                datetimemsg = (String) snapshot.child("DateTime").getValue();

                Intent serviceIntent = new Intent(MainActivity.this, exservice.class);
                serviceIntent.putExtra("datamsg", datamsg);
                serviceIntent.putExtra("datetimemsg", datetimemsg);
                ContextCompat.startForegroundService(MainActivity.this, serviceIntent);

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


    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationitemselectedlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedfragment = null;
            switch (item.getItemId()) {
                case R.id.homebutton:
                    selectedfragment = new Home();
                    break;

                case R.id.notificationbutton:
                    selectedfragment = new Notificationpage();
                    break;

                case R.id.internet_modebutton:
                    selectedfragment = new onmode();
                    break;

                case R.id.profilebutton:
                    selectedfragment = new Profile();
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

}