package com.exercise.thesis.hellodoc.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.model.Patient;
import com.exercise.thesis.hellodoc.ui.doctor.DossierMedical;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MyPatientsAdapter extends FirebaseRecyclerAdapter<Patient, MyPatientsAdapter.MyPatientsHolder>{

    StorageReference pathReference ;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param options
     */
    public MyPatientsAdapter(@NonNull FirebaseRecyclerOptions<Patient> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final MyPatientsHolder myPatientsHolder, int position, @NonNull final Patient patient) {
        myPatientsHolder.textViewTitle.setText(patient.getFullName());
        myPatientsHolder.textViewTelephone.setText("Tel : "+patient.getMblNum());
        myPatientsHolder.contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(v.getContext(),patient);
            }
        });

        myPatientsHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPatientMedicalFolder(v.getContext(),patient);

            }
        });
        myPatientsHolder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(myPatientsHolder.contactButton.getContext(),patient.getMblNum());
            }
        });

        String imageId = patient.getEmail().replace(".",",")+".jpg"; //add a title image
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(myPatientsHolder.imageViewPatient.getContext())
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(myPatientsHolder.imageViewPatient);//Image location

                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }
    private void openPage(Context wf, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        wf.startActivity(intent);
    }

    private void openPatientMedicalFolder(Context medicalFolder, Patient patient){
        Intent intent = new Intent(medicalFolder, DossierMedical.class);
        intent.putExtra("patient_name", patient.getFullName());
        intent.putExtra("patient_email",patient.getEmail().replace(".",","));
        intent.putExtra("patient_phone", patient.getMblNum());
        medicalFolder.startActivity(intent);

        /*Bundle bundle = new Bundle();
        bundle.putString("patient_name", patient.getFullName());
        bundle.putString("patient_email",patient.getEmail());
        bundle.putString("patient_phone", patient.getMblNum());
        AppCompatActivity activity = new AppCompatActivity();
        DossierMedicalFragment fragment = new DossierMedicalFragment();
        fragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.dossier_frame, fragment).commit();*/
    }

    private void openPage(Context wf,Patient p){
        Toast.makeText(wf, "Sorry, this functionality is unavailable for now!!", Toast.LENGTH_SHORT).show();
    }
    @NonNull
    @Override
    public MyPatientsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_patients_item, parent, false);
        return new MyPatientsHolder(v);
    }

    class MyPatientsHolder extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorItems
        Button callBtn;
        TextView textViewTitle;
        TextView textViewTelephone;
        ImageView imageViewPatient;
        Button contactButton;
        RelativeLayout parentLayout;
        public MyPatientsHolder(@NonNull View itemView) {
            super(itemView);
            callBtn = itemView.findViewById(R.id.callBtn);
            textViewTitle = itemView.findViewById(R.id.patient_view_title);
            textViewTelephone = itemView.findViewById(R.id.text_view_telephone);
            imageViewPatient = itemView.findViewById(R.id.patient_item_image);
            contactButton = itemView.findViewById(R.id.contact);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

}
