package com.example.roboticcar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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


public class onmode extends Fragment implements View.OnTouchListener {

    View view;
    ImageButton Forwardbutton, Backwardbutton, Leftbutton, Rightbutton;
    DatabaseReference controllerdata = FirebaseDatabase.getInstance().getReference().child(("Controls/data"));
    FloatingActionButton PlayPause;
    boolean flag = true;
    LinearLayout Controller, Instructor;
    WebView webView2;
    TextView link,Instructor_text;
    String onmodeurl;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_onmode, container, false);
        link = view.findViewById(R.id.extra);
        webView2 = view.findViewById(R.id.videoview2);
        Instructor = view.findViewById(R.id.instructor);
        Instructor_text=view.findViewById(R.id.instructor_text);


        final AlertDialog.Builder alert = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.CustomAlertDialog);
        View mView = getLayoutInflater().inflate(R.layout.onmodedialog, null);
        final EditText txt_inputText = mView.findViewById(R.id.txt_input);
        TextView btn_cancel = mView.findViewById(R.id.btn_cancel);
        Button btn_okay = mView.findViewById(R.id.btn_submit);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                alertDialog.dismiss();
                Instructor.setVisibility(View.VISIBLE);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), MainActivity.class);
                //startActivity(intent);
                //Fragment fragment = new Home();
                //FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentTransaction.replace(R.id.fragmentcontainer, fragment);
                //fragmentTransaction.addToBackStack(null);
                //fragmentTransaction.commit();
                alertDialog.dismiss();
                Instructor.setVisibility(View.VISIBLE);


            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(txt_inputText.getText().toString())) {
                    Toast toast = Toast.makeText(getContext(), "Enter Valid Input", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    onmodeurl = txt_inputText.getText().toString();
                    alertDialog.dismiss();
                    Instructor.setVisibility(View.VISIBLE);
                    Instructor_text.setText("Click Play Button To Start Streaming and Controlling.");
                    PlayPause.setVisibility(View.VISIBLE);
                    link.setText(onmodeurl);
                }
            }
        });
        alertDialog.show();


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
                    webView2.loadUrl(onmodeurl);
                    flag = false;


                    controllerdata.setValue("Stop");

                    OkHttpClient client = new OkHttpClient();
                    String url = "http://192.168.43.213:5000/carControl";
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

                } else {

                    PlayPause.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.playbutton));
                    Controller.setVisibility(View.GONE);
                    webView2.stopLoading();
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
                    controllerdata.setValue("Stop");
                    Forwardbutton.setBackgroundColor(0);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    controllerdata.setValue("Forward");
                    Forwardbutton.setBackgroundColor(0x310E68FF);
                    // Forwardbutton.setElevation(4 / Objects.requireNonNull(getContext()).getResources().getDisplayMetrics().density);
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