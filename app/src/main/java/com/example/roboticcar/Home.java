package com.example.roboticcar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Home extends Fragment implements View.OnTouchListener {
    View view;
    ImageButton Forwardbutton, Backwardbutton, Leftbutton, Rightbutton;
    DatabaseReference controllerdata = FirebaseDatabase.getInstance().getReference().child(("Controls/data"));
    FloatingActionButton PlayPause;
    boolean flag = true;
    LinearLayout Controller,Instructor;
    WebView webView;
    TextView link;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        link = view.findViewById(R.id.extra);
        webView = view.findViewById(R.id.videoview);
        Instructor=view.findViewById(R.id.instructor);

        link.setText("https://www.google.com/webhp?authuser=1");




        final OkHttpClient client = new OkHttpClient();
        String url = "http://192.168.43.213:5000/carControl";
        final Request request = new Request.Builder()
                .url(url)
                .build();





        Forwardbutton = view.findViewById(R.id.forward);
        Backwardbutton = view.findViewById(R.id.backward);
        Leftbutton = view.findViewById(R.id.leftbutton);
        Rightbutton = view.findViewById(R.id.rightbutton);

        PlayPause = view.findViewById(R.id.playpause);
        Controller = view.findViewById(R.id.controller);


        Forwardbutton.setOnTouchListener(this);
        Backwardbutton.setOnTouchListener(this);
        Leftbutton.setOnTouchListener(this);
        Rightbutton.setOnTouchListener(this);


        PlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag) {

                    PlayPause.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.pause));
                    Controller.setVisibility(View.VISIBLE);
                    Instructor.setVisibility(View.GONE);
                    webView.loadUrl("https://www.google.com/webhp?authuser=1");
                    flag = false;

                    controllerdata.setValue("Stop");



                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                        }
                    });

                } else {

                    PlayPause.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.playbutton));
                    Controller.setVisibility(View.GONE);
                    webView.stopLoading();
                    flag = true;

                    client.newCall(request).cancel();

                }

            }
        });

        return view;
    }

    @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.forward:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    controllerdata.setValue("Stop");
                    Forwardbutton.setBackgroundColor(0);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    controllerdata.setValue("Forward");
                    Forwardbutton.setBackgroundColor(0x310E68FF);
                }
                break;
            case R.id.backward:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    controllerdata.setValue("Stop");
                    Backwardbutton.setBackgroundColor(0);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    controllerdata.setValue("Backward");
                    Backwardbutton.setBackgroundColor(0x310E68FF);
                }
                break;

            case R.id.leftbutton:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    controllerdata.setValue("Stop");
                   Leftbutton.setBackgroundColor(0);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    controllerdata.setValue("Left");
                    Leftbutton.setBackgroundColor(0x310E68FF);
                }
                break;

            case R.id.rightbutton:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    controllerdata.setValue("Stop");
                    Rightbutton.setBackgroundColor(0);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    controllerdata.setValue("Right");
                    Rightbutton.setBackgroundColor(0x310E68FF);
                }
                break;

        }
        return true;
    }
}