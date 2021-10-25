package com.exercise.thesis.hellodoc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.model.Doctor;
import com.exercise.thesis.hellodoc.model.Fiche;
import com.exercise.thesis.hellodoc.ui.doctor.FicheInfo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class HospitalisationAdapter extends FirebaseRecyclerAdapter<Fiche,HospitalisationAdapter.FicheHolder2> {

    private FirebaseDatabase database;
    private DatabaseReference reference;

    public HospitalisationAdapter(@NonNull FirebaseRecyclerOptions<Fiche> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FicheHolder2 holder, int position, @NonNull final Fiche model) {
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("doctor");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doctor doctor = snapshot.child(model.getDoctor()).getValue(Doctor.class);
                holder.doctor_name.setText(doctor.getFullName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.type.setText(model.getType());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(v.getContext(),model);
            }
        });
        String[] date ;
        if(model.getDateCreated() != null) {
            date = model.getDateCreated().toString().split(" ");
            // Thu Jun 04 14:46:12 GMT+01:00 2020
            holder.appointment_day_name.setText(date[0]);
            holder.appointment_day.setText(date[2]);
            holder.appointment_month.setText(date[1]);
            holder.doctor_view_title.setText(date[3]);
        }

    }

    private void openPage(Context wf, Fiche m){
        Intent i = new Intent(wf, FicheInfo.class);
        i.putExtra("dateCreated", m.getDateCreated().toString());
        i.putExtra("doctor",m.getDoctor());
        i.putExtra("description",m.getDescription());
        wf.startActivity(i);
    }

    @NonNull
    @Override
    public FicheHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospitalisation_item,
                parent, false);
        return new FicheHolder2(v);
    }

    class FicheHolder2 extends RecyclerView.ViewHolder {
        TextView doctor_name;
        TextView type;
        Button btn;
        TextView appointment_month;
        TextView appointment_day;
        TextView appointment_day_name;
        TextView doctor_view_title;

        public FicheHolder2(View itemView) {
            super(itemView);
            doctor_name = itemView.findViewById(R.id.doctor_name2);
            type = itemView.findViewById(R.id.text_view_description3);
            btn = itemView.findViewById(R.id.see_records_btn2);
            appointment_month = itemView.findViewById(R.id.appointment_month2);
            appointment_day = itemView.findViewById(R.id.appointment_day2);
            appointment_day_name = itemView.findViewById(R.id.appointment_day_name2);
            doctor_view_title = itemView.findViewById(R.id.doctor_view_title2);
        }
    }

}
