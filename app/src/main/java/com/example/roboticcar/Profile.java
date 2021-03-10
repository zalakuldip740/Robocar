package com.example.roboticcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Profile extends Fragment {
    View view;
    TextView ip_robocar,logout;
    SharedPreferences sharedPreferences;
    String loginusername,signin;
    SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_profile, container, false);

        ip_robocar=view.findViewById(R.id.ip_robocar);
        logout=view.findViewById(R.id.logout);

        sharedPreferences=this.getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        loginusername=sharedPreferences.getString("username","");
        signin=sharedPreferences.getString("is_sign","");

        ip_robocar.setText(loginusername);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = sharedPreferences.edit();
                editor.putString("is_sign","false").apply();
                getActivity().stopService(new Intent(getActivity(),exservice.class));
                startActivity(new Intent(getContext(), Splashscreen.class));
            }
        });


        return view;
    }
}