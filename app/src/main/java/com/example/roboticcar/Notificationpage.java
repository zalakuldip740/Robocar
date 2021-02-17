package com.example.roboticcar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Notificationpage extends Fragment {
    View view;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("businesses");
    Notification_dataAdaptor adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notificationpage, container, false);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());


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
