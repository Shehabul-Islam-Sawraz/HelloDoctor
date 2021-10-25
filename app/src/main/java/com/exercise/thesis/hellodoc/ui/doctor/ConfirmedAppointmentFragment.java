package com.exercise.thesis.hellodoc.ui.doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.adapter.ConfirmedAppointmentAdapter;
import com.exercise.thesis.hellodoc.model.AppointmentInformation;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class ConfirmedAppointmentFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference appointmentReference;
    private ConfirmedAppointmentAdapter adapter;
    private View viewThis;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirmed_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewThis = view;
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("doctor");
        this.appointmentReference = database.getReference("appointment");
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        String doctorID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query =appointmentReference.child(doctorID).child("calendar").orderByChild("time");
        FirebaseRecyclerOptions<AppointmentInformation> options = new FirebaseRecyclerOptions.Builder<AppointmentInformation>().
                setQuery(query, AppointmentInformation.class).build();

        adapter = new ConfirmedAppointmentAdapter(options);
        //List current appointments
        RecyclerView recyclerView = viewThis.findViewById(R.id.confirmed_appointments_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

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