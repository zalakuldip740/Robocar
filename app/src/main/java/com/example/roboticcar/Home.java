package com.example.roboticcar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Home extends Fragment implements View.OnTouchListener {
    View view;
    ImageButton Forwardbutton, Backwardbutton, Leftbutton, Rightbutton;

    FloatingActionButton PlayPause;
    boolean flag = true;
    LinearLayout Controller, Instructor;
    WebView webView;
    TextView Instructor_text;
    String urlforward, urlbackward, urlleft, urlright, urlstop, usernamedata, streamingurl, generalurl;
    String tokenvalue;
    LottieAnimationView animationloading;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        webView = view.findViewById(R.id.videoview);
        Instructor = view.findViewById(R.id.instructor);
        Instructor_text = view.findViewById(R.id.instructor_text);


        animationloading = view.findViewById(R.id.animation_loading);


        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        usernamedata = sharedPreferences.getString("username", "");
        tokenvalue = sharedPreferences.getString("token", "");


        generalurl = "http://" + usernamedata + ":5000/";
        streamingurl = generalurl + "video_feed";


        urlforward = generalurl + "carControl/Forward";
        urlbackward = generalurl + "carControl/Backward";
        urlleft = generalurl + "carControl/Left";
        urlright = generalurl + "carControl/Right";
        urlstop = generalurl + "carControl/Stop";


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
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onClick(View view) {

                if (flag) {

                    PlayPause.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pause));
                    animationloading.setVisibility(View.VISIBLE);
                    animationloading.playAnimation();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            animationloading.setVisibility(View.GONE);
                            animationloading.pauseAnimation();

                        }
                    }, 2000);
                    Controller.setVisibility(View.VISIBLE);
                    Instructor.setVisibility(View.GONE);
                    Map<String, String> extraHeaders = new HashMap<>();
                    extraHeaders.put("Authorization", tokenvalue);
                    webView.loadUrl(streamingurl, extraHeaders);
                    webView.getSettings().setJavaScriptEnabled(true);
                    flag = false;


                } else {

                    PlayPause.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.playbutton));
                    Controller.setVisibility(View.GONE);
                    webView.stopLoading();
                    Instructor.setVisibility(View.VISIBLE);
                    Instructor_text.setText("Click Play Button To Resume.");
                    flag = true;


                }

            }
        });


        // if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
        //   LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.MATCH_PARENT);
        // params.gravity=Gravity.END| Gravity.BOTTOM;
        // Controller.setLayoutParams(params);
        // Controller.setBackgroundColor(0x00000000);
        //  }


        return view;
    }


    @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.forward:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    controllingreq(urlstop);
                    Forwardbutton.setBackgroundColor(0);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    controllingreq(urlforward);

                    Forwardbutton.setBackgroundColor(0x310E68FF);
                }
                break;
            case R.id.backward:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    controllingreq(urlstop);

                    Backwardbutton.setBackgroundColor(0);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    controllingreq(urlbackward);

                    Backwardbutton.setBackgroundColor(0x310E68FF);
                }
                break;

            case R.id.leftbutton:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    controllingreq(urlstop);

                    Leftbutton.setBackgroundColor(0);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    controllingreq(urlleft);

                    Leftbutton.setBackgroundColor(0x310E68FF);
                }
                break;

            case R.id.rightbutton:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    controllingreq(urlstop);

                    Rightbutton.setBackgroundColor(0);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    controllingreq(urlright);

                    Rightbutton.setBackgroundColor(0x310E68FF);
                }
                break;

        }


        return true;
    }

    private void controllingreq(final String url) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", tokenvalue)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {


            }
        });
    }


}