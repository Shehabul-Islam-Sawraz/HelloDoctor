package com.exercise.thesis.hellodoc.ui.doctor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.adapter.HospitalisationAdapter;
import com.exercise.thesis.hellodoc.model.Fiche;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Hospitalisation extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private HospitalisationAdapter adapter;
    View result;


    public Hospitalisation() {
        // Required empty public constructor
    }


    public static Hospitalisation newInstance() {
        Hospitalisation fragment = new Hospitalisation();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("PatientMedicalFolder");
        result = inflater.inflate(R.layout.fragment_hospitalisation, container, false);
        setUpRecyclerView();
        return result;
    }
    private void setUpRecyclerView() {
        String email_id = getActivity().getIntent().getExtras().getString("patient_email");
        Query query = reference.child(email_id).orderByChild("type").equalTo("Hospitalisation");

        FirebaseRecyclerOptions<Fiche> options = new FirebaseRecyclerOptions.Builder<Fiche>()
                .setQuery(query, Fiche.class)
                .build();

        adapter = new HospitalisationAdapter(options);

        RecyclerView recyclerView = result.findViewById(R.id.hospitalisationRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
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