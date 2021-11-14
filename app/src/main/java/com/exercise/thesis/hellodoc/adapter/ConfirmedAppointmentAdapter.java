package com.exercise.thesis.hellodoc.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.model.AppointmentInformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ConfirmedAppointmentAdapter extends FirebaseRecyclerAdapter<AppointmentInformation, ConfirmedAppointmentAdapter.ConfirmedAppointmentHolder> {

    StorageReference pathReference ;

    public ConfirmedAppointmentAdapter(@NonNull FirebaseRecyclerOptions<AppointmentInformation> options) {
        super(options);
    }

    @NonNull
    @Override
    public ConfirmedAppointmentAdapter.ConfirmedAppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmed_appointment_item, parent, false);
        return new ConfirmedAppointmentAdapter.ConfirmedAppointmentHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ConfirmedAppointmentHolder holder, int position, @NonNull AppointmentInformation appointmentInformation) {
        holder.dateAppointment.setText(appointmentInformation.getTime());
        holder.patientName.setText(appointmentInformation.getPatientName());
        holder.appointmentType.setText(appointmentInformation.getAppointmentType());

        String imageId = appointmentInformation.getPatientId().replace(".",",")+".jpg"; //add a title image
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(holder.patientImage.getContext())
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(holder.patientImage);//Image location

                //profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }


    class ConfirmedAppointmentHolder extends RecyclerView.ViewHolder{
        TextView dateAppointment;
        TextView patientName;
        TextView appointmentType;
        ImageView patientImage;
        public ConfirmedAppointmentHolder(@NonNull View itemView) {
            super(itemView);
            dateAppointment = itemView.findViewById(R.id.appointement_date);
            patientName = itemView.findViewById(R.id.patient_name);
            appointmentType = itemView.findViewById(R.id.appointment_type);
            patientImage = itemView.findViewById(R.id.patient_image);
        }
    }

}
