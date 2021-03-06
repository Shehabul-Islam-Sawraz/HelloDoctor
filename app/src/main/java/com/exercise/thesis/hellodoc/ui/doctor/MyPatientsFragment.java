package com.exercise.thesis.hellodoc.ui.doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.adapter.ConfirmedAppointmentAdapter;
import com.exercise.thesis.hellodoc.adapter.MyPatientsAdapter;
import com.exercise.thesis.hellodoc.model.AppointmentInformation;
import com.exercise.thesis.hellodoc.model.Patient;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MyPatientsFragment extends Fragment{

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private MyPatientsAdapter adapter;
    private View viewThis;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_patients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("MyPatients");
        this.viewThis = view;
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        getActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setUpRecyclerView();
    }

    public void setUpRecyclerView(){

        final String doctorID = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",");
        Query query = reference.child(doctorID).orderByChild("fullName");

        FirebaseRecyclerOptions<Patient> options = new FirebaseRecyclerOptions.Builder<Patient>()
                .setQuery(query, Patient.class)
                .build();

        FirebaseRecyclerOptions<Patient> newList = options;


        adapter = new MyPatientsAdapter(newList);
        //ListMyPatients
        RecyclerView recyclerView = viewThis.findViewById(R.id.ListMyPatients);
        recyclerView.getRecycledViewPool().clear();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

    @Override
    public void onResume() {
        super.onResume();
        setUpRecyclerView();
        adapter.startListening();
    }
}