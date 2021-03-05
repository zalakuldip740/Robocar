package com.example.roboticcar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Profile extends Fragment {
    View view;
    TextView ip_robocar;
    SharedPreferences sharedPreferences;
    String loginusername;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_profile, container, false);

        ip_robocar=view.findViewById(R.id.ip_robocar);

        sharedPreferences=this.getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        loginusername=sharedPreferences.getString("username","");

        ip_robocar.setText(loginusername);


        return view;
    }
}