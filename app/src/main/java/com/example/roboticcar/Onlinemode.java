package com.example.roboticcar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Onlinemode extends Fragment {

    View view;
    FloatingActionButton PlayPause;
    boolean flag = true;
    LinearLayout Instructor;
    WebView webView2;
    TextView  Instructor_text;
    String onmodeurl;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_onlinemode, container, false);
        webView2 = view.findViewById(R.id.videoview2);
        Instructor = view.findViewById(R.id.instructor);
        Instructor_text = view.findViewById(R.id.instructor_text);


        final AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog);
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
                    Instructor_text.setText("Click Play Button To Start Streaming.");
                    PlayPause.setVisibility(View.VISIBLE);
                }
            }
        });
        alertDialog.show();


        PlayPause = view.findViewById(R.id.playpause);

        PlayPause.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                if (flag) {

                    PlayPause.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pause));
                    Instructor.setVisibility(View.GONE);
                    webView2.loadUrl(onmodeurl);
                    flag = false;


                } else {

                    PlayPause.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.playbutton));
                    webView2.stopLoading();
                    Instructor.setVisibility(View.VISIBLE);
                    Instructor_text.setText("Click Play Button To Resume.");
                    flag = true;

                }

            }
        });

        return view;
    }

}