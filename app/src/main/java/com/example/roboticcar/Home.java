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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Home extends Fragment implements View.OnTouchListener {
    View view;
    ImageButton Forwardbutton, Backwardbutton, Leftbutton, Rightbutton;
    //DatabaseReference controllerdata = FirebaseDatabase.getInstance().getReference().child(("Controls/data"));
    FloatingActionButton PlayPause;
    boolean flag = true;
    LinearLayout Controller, Instructor;
    WebView webView;
    TextView Instructor_text;
    String urlforward, urlbackward, urlleft, urlright, urlstop;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        webView = view.findViewById(R.id.videoview);
        Instructor = view.findViewById(R.id.instructor);
        Instructor_text = view.findViewById(R.id.instructor_text);



        urlforward = "http://192.168.43.213:5000/carControl/Forward";
        urlbackward = "http://192.168.43.213:5000/carControl/Backward";
        urlleft = "http://192.168.43.213:5000/carControl/Left";
        urlright = "http://192.168.43.213:5000/carControl/Right";
        urlstop = "http://192.168.43.213:5000/carControl/Stop";


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

                    PlayPause.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pause));
                    Controller.setVisibility(View.VISIBLE);
                    Instructor.setVisibility(View.GONE);
                    webView.loadUrl("http://192.168.43.213:5000/");
                    flag = false;

                    //controllerdata.setValue("Stop");


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

        return view;
    }


    @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.forward:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    //controllerdata.setValue("Stop");
                    controllingreq(urlstop);
                    Forwardbutton.setBackgroundColor(0);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    controllingreq(urlforward);
                    // controllerdata.setValue("Forward");
                    Forwardbutton.setBackgroundColor(0x310E68FF);
                }
                break;
            case R.id.backward:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    controllingreq(urlstop);
                    // controllerdata.setValue("Stop");
                    Backwardbutton.setBackgroundColor(0);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    controllingreq(urlbackward);
                    // controllerdata.setValue("Backward");
                    Backwardbutton.setBackgroundColor(0x310E68FF);
                }
                break;

            case R.id.leftbutton:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    controllingreq(urlstop);
                    // controllerdata.setValue("Stop");
                    Leftbutton.setBackgroundColor(0);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    controllingreq(urlleft);
                    // controllerdata.setValue("Left");
                    Leftbutton.setBackgroundColor(0x310E68FF);
                }
                break;

            case R.id.rightbutton:
                if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    controllingreq(urlstop);
                    // controllerdata.setValue("Stop");
                    Rightbutton.setBackgroundColor(0);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    controllingreq(urlright);
                    // controllerdata.setValue("Right");
                    Rightbutton.setBackgroundColor(0x310E68FF);
                }
                break;

        }
        return true;
    }

    private void controllingreq(String url) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


}