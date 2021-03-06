package com.example.roboticcar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Notificationpage extends Fragment {
    View view;
    DatabaseReference databaseReference;
    Notification_dataAdaptor adapter;
    SharedPreferences sharedPreferences;
    String msgdata,msgdatapath;
    LottieAnimationView animationloading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notificationpage, container, false);
        animationloading=view.findViewById(R.id.animation_loading);

        animationloading.setVisibility(View.VISIBLE);
        animationloading.playAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                animationloading.setVisibility(View.GONE);
                animationloading.pauseAnimation();

            }
        }, 2000);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());

        sharedPreferences=this.getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        msgdata=sharedPreferences.getString("username","");

        msgdatapath=msgdata.replace(".","_");
        databaseReference= FirebaseDatabase.getInstance().getReference().child(msgdatapath);


        RecyclerView recyclerView = view.findViewById(R.id.notification_recycleview);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<notification_data> options
                = new FirebaseRecyclerOptions.Builder<notification_data>()
                .setQuery(databaseReference, notification_data.class)
                .build();

        adapter = new Notification_dataAdaptor(options);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
