package com.example.roboticcar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference controllerdata = databaseReference.child(("Controls/data"));

    ImageView Homebutton, Videobutton, Notificationbutton, Internetmodebutton, Forwardbutton, Backwardbutton, Leftbutton, Rightbutton;
    CoordinatorLayout coordinatorLayout;
    WebView webView;
    ToggleButton playpause;
    String datamsg;
    String datetimemsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.videoview);


        Homebutton = findViewById(R.id.homebutton);
        Videobutton = findViewById(R.id.videobutton);
        Notificationbutton = findViewById(R.id.notificationbutton);
        Internetmodebutton = findViewById(R.id.internet_modebutton);
        Forwardbutton = findViewById(R.id.forward);
        Backwardbutton = findViewById(R.id.backward);
        Leftbutton = findViewById(R.id.leftbutton);
        Rightbutton = findViewById(R.id.rightbutton);
        coordinatorLayout = findViewById(R.id.mainlayout);
        playpause = findViewById(R.id.playpausebutton);

        Homebutton.setOnClickListener(this);
        Videobutton.setOnClickListener(this);
        Notificationbutton.setOnClickListener(this);
        Internetmodebutton.setOnClickListener(this);
        Forwardbutton.setOnClickListener(this);
        Backwardbutton.setOnClickListener(this);
        Leftbutton.setOnClickListener(this);
        Rightbutton.setOnClickListener(this);
        playpause.setOnClickListener(this);




        databaseReference.child("businesses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                datamsg= (String) snapshot.child("Data").getValue();
                datetimemsg= (String) snapshot.child("DateTime").getValue();

                Intent serviceIntent = new Intent(MainActivity.this, exservice.class);
                serviceIntent.putExtra("datamsg", datamsg);
                serviceIntent.putExtra("datetimemsg",datetimemsg);
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



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.homebutton:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.videobutton:
                Videospage videospage = new Videospage();
                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.mainlayout, videospage);
                transaction1.commit();
                playpause.setVisibility(View.GONE);
                playpause.setChecked(false);
                break;

            case R.id.notificationbutton:
                Notificationpage notificationpage = new Notificationpage();
                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                transaction2.replace(R.id.mainlayout, notificationpage);
                transaction2.commit();
                playpause.setVisibility(View.GONE);
                playpause.setChecked(false);
                break;


            case R.id.internet_modebutton:

                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, R.style.CustomAlertDialog);
                View mView = getLayoutInflater().inflate(R.layout.internetmode_dialog, null);
                final EditText txt_inputText = mView.findViewById(R.id.txt_input);
                TextView btn_cancel = mView.findViewById(R.id.btn_cancel);
                Button btn_okay = mView.findViewById(R.id.btn_submit);
                alert.setView(mView);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.getWindow().setGravity(Gravity.BOTTOM);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btn_okay.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {

                        if (TextUtils.isEmpty(txt_inputText.getText().toString())) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Enter Valid Input", Toast.LENGTH_SHORT);
                            toast.show();

                        } else {
                            playpause.setChecked(true);
                            webView.loadUrl(txt_inputText.getText().toString());
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();
                break;


            case R.id.forward:
                controllerdata.setValue("Forward");
                break;

            case R.id.backward:
                controllerdata.setValue("Backward");
                break;

            case R.id.leftbutton:
                controllerdata.setValue("Left");
                break;

            case R.id.rightbutton:
                controllerdata.setValue("Right");
                break;


            case R.id.playpausebutton:
                if (playpause.isChecked()) {
                    webView.loadUrl("https://www.google.com/webhp?authuser=1");
                } else {
                    webView.stopLoading();
                }
                break;
        }
    }



    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}