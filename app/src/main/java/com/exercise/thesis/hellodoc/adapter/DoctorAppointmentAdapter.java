package com.exercise.thesis.hellodoc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.model.AppointmentInformation;
import com.exercise.thesis.hellodoc.model.Doctor;
import com.exercise.thesis.hellodoc.model.Patient;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DoctorAppointmentAdapter extends FirebaseRecyclerAdapter<AppointmentInformation, DoctorAppointmentAdapter.MyDoctorAppointmentHolder> {

    StorageReference pathReference;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference patientReference;
    private DatabaseReference myPatientsReference;
    private DatabaseReference doctorReference;
    private DatabaseReference patientDoctorReference;
    private DatabaseReference appointmentReference;
    private DatabaseReference bookDateSlot;

    public DoctorAppointmentAdapter(@NonNull FirebaseRecyclerOptions<AppointmentInformation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyDoctorAppointmentHolder myDoctorAppointmentHolder, @SuppressLint("RecyclerView") int position, @NonNull final AppointmentInformation appointmentInformation) {
        myDoctorAppointmentHolder.dateAppointment.setText(appointmentInformation.getTime());
        myDoctorAppointmentHolder.patientName.setText(appointmentInformation.getPatientName());
        myDoctorAppointmentHolder.appointmentType.setText(appointmentInformation.getAppointmentType());
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("PatientAppointment");
        this.patientReference = database.getReference("patient");
        this.myPatientsReference = database.getReference("MyPatients");
        this.doctorReference = database.getReference("doctor");
        this.patientDoctorReference = database.getReference("PatientDoctor");
        this.appointmentReference = database.getReference("appointment");
        myDoctorAppointmentHolder.approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentInformation.setType("Accepted");

                reference.child(appointmentInformation.getPatientId()).child("calendar").child(appointmentInformation.getTime().replace("/","_")).
                        setValue(appointmentInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(v.getContext(), "", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Can't add info successfully!!", Toast.LENGTH_SHORT).show();
                    }
                });

                bookDateSlot = database.getReference(appointmentInformation.getChain());

                bookDateSlot.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AppointmentInformation info = snapshot.getValue(AppointmentInformation.class);
                        info.setType("Accepted");
                        bookDateSlot.setValue(info).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(v.getContext(), "Appointment Updated Successfully!! ", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Can't Update Info Successfully!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                appointmentReference.child(appointmentInformation.getDoctorId()).child("calendar").
                        child(appointmentInformation.getTime().replace("/","_")).setValue(appointmentInformation).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(v.getContext(), "Appointment Set Successfully!!", Toast.LENGTH_SHORT).show();
                            }
                        });


                patientReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Patient patient = snapshot.child(appointmentInformation.getPatientId()).getValue(Patient.class);
                        if(patient!=null){
                            myPatientsReference.child(appointmentInformation.getDoctorId()).child(appointmentInformation.getPatientId()).
                                    setValue(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(v.getContext(), "Patient added successfully!!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(v.getContext(), "Can't add patient successfully!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                doctorReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Doctor doctor = snapshot.child(appointmentInformation.getDoctorId()).getValue(Doctor.class);
                        patientDoctorReference.child(appointmentInformation.getPatientId()).setValue(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(v.getContext(), "Doctor added successfully!!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Can't add doctor successfully!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                getSnapshots().getSnapshot(position).getRef().removeValue();
            }
        });
        myDoctorAppointmentHolder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentInformation.setType("Refused");

                reference.child(appointmentInformation.getPatientId()).child("calendar").child(appointmentInformation.getTime().replace("/","_")).
                        setValue(appointmentInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(v.getContext(), "", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Can't add info successfully in holder!!", Toast.LENGTH_SHORT).show();
                    }
                });


                bookDateSlot = database.getReference(appointmentInformation.getChain());
                bookDateSlot.removeValue();

                getSnapshots().getSnapshot(position).getRef().removeValue();
            }
        });

        String imageId = appointmentInformation.getPatientId()+".jpg"; //add a title image
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(myDoctorAppointmentHolder.patient_image.getContext())
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(myDoctorAppointmentHolder.patient_image);//Image location

                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    private void openPage(Context wf, Doctor d){
        Toast.makeText(wf, "Sorry, this functionality is not available for now!!", Toast.LENGTH_SHORT).show();
    }


    @NonNull
    @Override
    public MyDoctorAppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_appointment_item, parent, false);
        return new MyDoctorAppointmentHolder(v);
    }

    class MyDoctorAppointmentHolder extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorAppointmentItems
        TextView dateAppointment;
        TextView patientName;
        Button approveBtn;
        Button cancelBtn;
        TextView appointmentType;
        ImageView patient_image;
        public MyDoctorAppointmentHolder(@NonNull View itemView) {
            super(itemView);
            dateAppointment = itemView.findViewById(R.id.appointement_date);
            patientName = itemView.findViewById(R.id.patient_name);
            approveBtn = itemView.findViewById(R.id.btn_accept);
            cancelBtn = itemView.findViewById(R.id.btn_decline);
            appointmentType = itemView.findViewById(R.id.appointment_type);
            patient_image = itemView.findViewById(R.id.patient_image);
        }
    }

}
