package com.exercise.thesis.hellodoc.ui.doctor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.adapter.ConsultationAdapter;
import com.exercise.thesis.hellodoc.model.Fiche;
import com.exercise.thesis.hellodoc.model.Patient;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConsultationFragmentPage extends Fragment {

    private ConsultationAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    View result;

    public ConsultationFragmentPage() {
        // Required empty public constructor
    }

    public static ConsultationFragmentPage newInstance() {
        ConsultationFragmentPage fragment = new ConsultationFragmentPage();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        result = inflater.inflate(R.layout.fragment_consultation_page, container, false);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("PatientMedicalFolder");
        setUpRecyclerView();
        return result;
    }

    private void setUpRecyclerView() {

        String email_id = getActivity().getIntent().getExtras().getString("patient_email").replace(".",",");
        Query query = reference.child(email_id).orderByChild("type").equalTo("Consultation");

        FirebaseRecyclerOptions<Fiche> options = new FirebaseRecyclerOptions.Builder<Fiche>()
                .setQuery(query, Fiche.class)
                .build();

        FirebaseRecyclerOptions<Fiche> newList = options;

        RecyclerView recyclerView = result.findViewById(R.id.conslutationRecycleView);
        recyclerView.getRecycledViewPool().clear();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new ConsultationAdapter(newList);
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

    @Override
    public void onResume() {
        super.onResume();
        setUpRecyclerView();
        adapter.startListening();
    }
}