package com.example.roboticcar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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


public class onmode extends Fragment implements View.OnClickListener{

    View view;
    ImageView Forwardbutton, Backwardbutton, Leftbutton, Rightbutton, Stopbutton;
    DatabaseReference controllerdata = FirebaseDatabase.getInstance().getReference().child(("Controls/data"));
    FloatingActionButton PlayPause;
    boolean flag = true;
    LinearLayout Controller;
    WebView webView2;
    TextView link;
    String onmodeurl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_onmode, container, false);
        link = view.findViewById(R.id.extra);
        webView2 = view.findViewById(R.id.videoview2);




        final AlertDialog.Builder alert = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.CustomAlertDialog);
        View mView = getLayoutInflater().inflate(R.layout.onmodedialog, null);
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
                Fragment fragment = new Home();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentcontainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                alertDialog.dismiss();

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
                    onmodeurl =txt_inputText.getText().toString();
                    alertDialog.dismiss();
                    link.setText(onmodeurl);
                }
            }
        });
        alertDialog.show();



        Forwardbutton = view.findViewById(R.id.forward);
        Backwardbutton = view.findViewById(R.id.backward);
        Leftbutton = view.findViewById(R.id.leftbutton);
        Rightbutton = view.findViewById(R.id.rightbutton);
        Stopbutton = view.findViewById(R.id.stopbutton);
        PlayPause = view.findViewById(R.id.playpause);
        Controller = view.findViewById(R.id.controller);


        Forwardbutton.setOnClickListener(this);
        Backwardbutton.setOnClickListener(this);
        Leftbutton.setOnClickListener(this);
        Rightbutton.setOnClickListener(this);
        Stopbutton.setOnClickListener(this);
        PlayPause.setOnClickListener(this);

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forward:
                controllerdata.setValue("Forward");


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

            case R.id.stopbutton:
                controllerdata.setValue("Stop");
                break;

            case R.id.playpause:

                if (flag) {

                    PlayPause.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.pause));
                    Controller.setVisibility(View.VISIBLE);
                    webView2.loadUrl(onmodeurl);
                    flag = false;

                } else {

                    PlayPause.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.playbutton));
                    Controller.setVisibility(View.GONE);
                    webView2.stopLoading();
                    flag = true;

                }

                break;
        }
    }



}