package com.exercise.thesis.hellodoc.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.common.Common;
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
                Doctor doctor = snapshot.child(model.getDoctor().replace(".",",")).getValue(Doctor.class);
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

        if(model.isRated()){
            holder.rateBtn.setVisibility(View.GONE);
        }
        holder.rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRating(view.getContext(), model);
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

    private void getRating(Context context, Fiche f){
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.rating_dialog);
        dialog.show();
        RatingBar ratingBar = dialog.findViewById(R.id.rating_bar);
        TextView tvRating = dialog.findViewById(R.id.tv_rating);
        Button submit = dialog.findViewById(R.id.rate_submit);
        final boolean[] ratingChanged = {false};
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                tvRating.setText(String.format("(%s)",v));
                ratingChanged[0] = true;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ratingChanged[0]){
                    dialog.dismiss();
                }
                else{
                    String sRating = String.valueOf(ratingBar.getRating());
                    //Updating doctor rating
                    final Doctor[] doctor = {null};
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            doctor[0] = snapshot.child(f.getDoctor().replace(".",",")).getValue(Doctor.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    if(doctor[0]!=null){
                        Doctor doctor1 = new Doctor(doctor[0].getFullName(), doctor[0].getEmail(), doctor[0].getAddress(), doctor[0].getImage());
                        doctor1.setPhoneNum(doctor[0].getPhoneNum());
                        doctor1.setFees(doctor[0].getFees());
                        doctor1.setSpecialities(doctor[0].getSpecialities());
                        doctor1.setAbout(doctor[0].getAbout());
                        doctor1.setNoOfRating(doctor[0].getNoOfRating()+1);
                        double d = Double.parseDouble(doctor[0].getAvgRating())*doctor[0].getNoOfRating()*1.0;
                        d+= Double.parseDouble(sRating);
                        d=(d)/((doctor[0].getNoOfRating()+1)*1.0);
                        doctor1.setAvgRating(String.valueOf(d));
                        reference.child(doctor[0].getEmail().replace(".",",")).setValue(doctor1);
                    }
                    else{
                        Toast.makeText(context, "Couldn't rate doctor successfully!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //Updating fiche
                    Fiche records = new Fiche(f.getDisease(), f.getDescription(), f.getTreatment(), f.getType(), f.getDoctor(), f.getDateCreated(), f.getPrescription());
                    records.setRated(true);
                    String id = f.getId();
                    records.setId(id);
                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")).child(id).setValue(records);
                }
            }
        });
    }

    private void openPage(Context wf, Fiche m){
        Intent i = new Intent(wf, FicheInfo.class);
        i.putExtra("dateCreated", m.getDateCreated().toString());
        i.putExtra("disease",m.getDisease());
        i.putExtra("description",m.getDescription());
        i.putExtra("treatment", m.getTreatment());
        if(m.getPrescription()==null){
            i.putExtra("prescription","null");
        }
        else{
            i.putExtra("prescription", m.getPrescription().toString());
        }
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
        Button btn,rateBtn;
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
            rateBtn = itemView.findViewById(R.id.rate_btn2);
            if(Common.CurrentUserType.equals("doctor")){
                rateBtn.setVisibility(View.GONE);
            }
        }
    }

}
