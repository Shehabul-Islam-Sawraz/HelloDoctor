package com.exercise.thesis.hellodoc.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.model.AppointmentInformation;
import com.exercise.thesis.hellodoc.model.Doctor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PatientAppointmentsAdapter extends FirebaseRecyclerAdapter<AppointmentInformation, PatientAppointmentsAdapter.PatientAppointmentsHolder> {

    StorageReference pathReference;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference imgReference;
    private Uri profilePhoto = null;

    public PatientAppointmentsAdapter(@NonNull FirebaseRecyclerOptions<AppointmentInformation> options) {
        super(options);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("doctor");
        this.imgReference = database.getReference("images");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onBindViewHolder(@NonNull PatientAppointmentsHolder patientAppointmentsHolder, int position, @NonNull final AppointmentInformation appointmentInformation) {
        patientAppointmentsHolder.dateAppointment.setText(appointmentInformation.getTime());
        patientAppointmentsHolder.patientName.setText(appointmentInformation.getDoctorName());
        patientAppointmentsHolder.appointmentType.setText(appointmentInformation.getAppointmentType());
        patientAppointmentsHolder.type.setText(appointmentInformation.getType());
        String doctorEmail = appointmentInformation.getDoctorId();
        Log.d("doctor id", doctorEmail);

        Query query = reference.orderByChild("email").equalTo(doctorEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot shot: snapshot.getChildren()){
                        Doctor doctor = shot.getValue(Doctor.class);
                        //System.out.println(shot.getValue().toString());
                        patientAppointmentsHolder.phone.setText(doctor.getPhoneNum());
                    }
                    //Log.d("telephone num", doctor.getPhoneNum());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imgUrl = snapshot.child(appointmentInformation.getDoctorId().replace(".",",")).child("imageUri").getValue(String.class);
                //Toast.makeText(getActivity(), "ImgUrl: "+imgUrl, Toast.LENGTH_SHORT).show();
                if(imgUrl!=null){
                    profilePhoto = Uri.parse(imgUrl);
                    Picasso.with(patientAppointmentsHolder.image.getContext())
                            .load(profilePhoto.toString())
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .centerCrop()
                            .into(patientAppointmentsHolder.image, new Callback() {
                                @Override
                                public void onSuccess() {
                                    //dialog.dismiss();
                                }

                                @Override
                                public void onError() {
                                    //Toast.makeText(getActivity(), "Profile photo load error!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (appointmentInformation.getAppointmentType().equals("Consultation")) {
            patientAppointmentsHolder.appointmentType.setBackground(patientAppointmentsHolder.appointmentType.getContext().getResources().getDrawable(R.drawable.button_radius_primary_color));
        }
        if (appointmentInformation.getType().equals("Accepted")) {
            patientAppointmentsHolder.type.setTextColor(Color.parseColor("#20bf6b"));
        } else if (appointmentInformation.getType().equals("Checked")) {
            patientAppointmentsHolder.type.setTextColor(Color.parseColor("#8854d0"));
        } else {
            patientAppointmentsHolder.type.setTextColor(Color.parseColor("#eb3b5a"));
        }
    }

    @NonNull
    @Override
    public PatientAppointmentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_appointments_item, parent, false);
        return new PatientAppointmentsHolder(v);
    }

    class PatientAppointmentsHolder extends RecyclerView.ViewHolder {
        TextView dateAppointment;
        TextView patientName;
        TextView appointmentType;
        TextView type;
        TextView phone;
        ImageView image;

        public PatientAppointmentsHolder(@NonNull View itemView) {
            super(itemView);
            dateAppointment = itemView.findViewById(R.id.appointment_date_patient);
            patientName = itemView.findViewById(R.id.patient_name2);
            appointmentType = itemView.findViewById(R.id.appointment_type_patient);
            type = itemView.findViewById(R.id.type);
            phone = itemView.findViewById(R.id.patient_phone2);
            image = itemView.findViewById(R.id.patient_image2);
        }
    }
}
