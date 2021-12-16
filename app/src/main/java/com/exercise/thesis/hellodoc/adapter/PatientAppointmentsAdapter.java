package com.exercise.thesis.hellodoc.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
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
import com.exercise.thesis.hellodoc.common.Common;
import com.exercise.thesis.hellodoc.model.AppointmentInformation;
import com.exercise.thesis.hellodoc.model.Doctor;
import com.exercise.thesis.hellodoc.model.Patient;
import com.exercise.thesis.hellodoc.viewmodel.TimeSlot;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class PatientAppointmentsAdapter extends FirebaseRecyclerAdapter<AppointmentInformation, PatientAppointmentsAdapter.PatientAppointmentsHolder> {

    StorageReference pathReference;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference imgReference;
    private DatabaseReference patientAppointToday;
    private DatabaseReference patientReference;
    private DatabaseReference myPatientsReference;
    private DatabaseReference doctorReference;
    private DatabaseReference patientDoctorReference;
    private DatabaseReference appointmentReference;
    private DatabaseReference patAppointReference;
    private DatabaseReference bookDateSlot;
    private Uri profilePhoto = null;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public PatientAppointmentsAdapter(@NonNull FirebaseRecyclerOptions<AppointmentInformation> options) {
        super(options);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("doctor");
        this.imgReference = database.getReference("images");
        this.patientAppointToday = database.getReference("PatientAppointmentToday");


        this.patAppointReference = database.getReference("PatientAppointment");
        this.patientReference = database.getReference("patient");
        this.myPatientsReference = database.getReference("MyPatients");
        this.doctorReference = database.getReference("doctor");
        this.patientDoctorReference = database.getReference("PatientDoctor");
        this.appointmentReference = database.getReference("appointment");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onBindViewHolder(@NonNull PatientAppointmentsHolder patientAppointmentsHolder, @SuppressLint("RecyclerView") int position, @NonNull final AppointmentInformation appointmentInformation) {
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

        patientAppointmentsHolder.cancelAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shiftAppointment(appointmentInformation, position, patientAppointmentsHolder);
            }
        });
    }

    private void shiftAppointment(AppointmentInformation appointmentInformation, int position, PatientAppointmentsHolder patientAppointmentsHolder) {
        Calendar instance = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        String[] dates = appointmentInformation.getTime().split(" ");
        String date = dates[2].replace("/","_");
        String currentDate = Common.simpleFormat.format(Common.currentDate.getTime());
        if(currentDate.compareTo(date)>0){
            Toast.makeText(patientAppointmentsHolder.image.getContext(), "Appointment Date Exceeded!! Can't Delete the Appointment!!", Toast.LENGTH_SHORT).show();
        }
        else{
            int hour24hrs = instance.get(Calendar.HOUR_OF_DAY);
            int minutes = instance.get(Calendar.MINUTE);
            String[] times = dates[0].split("-");
            String[] hourMin = times[0].split(":");
            int appointHour = Integer.parseInt(hourMin[0]);
            int appointMin = Integer.parseInt(hourMin[1]);
            long  appointTimeLong = (appointHour*60*60) + (appointMin*60);
            long currentTimeLong = (hour24hrs*60*60) + (minutes*60);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(patientAppointmentsHolder.patientName.getContext());
            alertDialogBuilder.setTitle("Delete Appointment");
            if(currentTimeLong>appointTimeLong){
                // taile ekebare last e move koro jodi shombhob hoy
            }
            else if((appointTimeLong-currentTimeLong)>=(3*60*60)){ // if cancel before 3 hour
                // shift it to next day without any charge
                alertDialogBuilder.setMessage("Are you sure you want to cancel this appointment?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        shiftNextDay(false, appointmentInformation, patientAppointmentsHolder);
                        dialogInterface.cancel();
                        // Appoint instance ta k remove korte hobe
                        getSnapshots().getSnapshot(position).getRef().removeValue();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            }
            else{
                // cut some charge
                shiftNextDay(true, appointmentInformation,patientAppointmentsHolder);
                alertDialogBuilder.setMessage("Are you sure you want to cancel this appointment? Some charges will be taken!!");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        shiftNextDay(true, appointmentInformation, patientAppointmentsHolder);
                        dialogInterface.cancel();
                        // Appoint instance ta k remove korte hobe
                        getSnapshots().getSnapshot(position).getRef().removeValue();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertDialogBuilder.show();
            }

        }
    }

    private void shiftNextDay(boolean charge, AppointmentInformation appointmentInformation, PatientAppointmentsHolder patientAppointmentsHolder) {
        AppointmentInformation information = new AppointmentInformation();
        if(charge){
            information.setIsChargeApplicable("Yes");
        }
        else{
            information.setIsChargeApplicable("No");
        }
        information.setType(appointmentInformation.getType());
        information.setAppointmentType(appointmentInformation.getAppointmentType());
        information.setPatientId(appointmentInformation.getPatientId());
        information.setDoctorId(appointmentInformation.getDoctorId());
        information.setDoctorName(appointmentInformation.getDoctorName());
        information.setPatientName(appointmentInformation.getPatientName());
        String[] chain = appointmentInformation.getChain().split("/");
        String currentDoctor = chain[1];
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String timeSlot = chain[3];
        information.setChain("bookdate/"+currentDoctor+"/"+Common.simpleFormat.format(calendar.getTime())+"/"+timeSlot);
        String[] timeChain = appointmentInformation.getTime().split(" ");
        information.setTime(new StringBuilder(timeChain[0])
                .append(" at ")
                .append(simpleDateFormat.format(calendar.getTime())).toString());
        information.setSlot(appointmentInformation.getSlot());
        TimeSlot appointTimeSlot = appointmentInformation.getTimeSlot();

        TimeSlot slot = new TimeSlot();
        slot.setSlot((long)appointTimeSlot.getSlot());
        slot.setType(appointTimeSlot.getType());
        slot.setChain("bookdate/"+currentDoctor+"/"+Common.simpleFormat.format(calendar.getTime())+"/"+timeSlot);
        information.setTimeSlot(slot);

        //Main instructions starts here for removing

        patAppointReference.child(appointmentInformation.getPatientId().replace(".",",")).child("calendar")
                .child(appointmentInformation.getTime().replace("/","_"))
                .removeValue();

        patientAppointToday.child(appointmentInformation.getPatientId().replace(".",",")).child("calendar")
                .child(timeChain[2].replace("/","_")).child(timeChain[0]).removeValue();

        database.getReference(appointmentInformation.getChain()).removeValue();

        appointmentReference.child(appointmentInformation.getDoctorId().replace(".",",")).child("calendar").
                child(appointmentInformation.getTime().replace("/","_")).removeValue();

        patientReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Patient patient = snapshot.child(appointmentInformation.getPatientId().replace(".",",")).getValue(Patient.class);
                if(patient!=null){
                    myPatientsReference.child(appointmentInformation.getDoctorId().replace(".",","))
                            .child(appointmentInformation.getPatientId().replace(".",","))
                            .removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        doctorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doctor doctor = snapshot.child(appointmentInformation.getDoctorId().replace(".",",")).getValue(Doctor.class);
                patientDoctorReference.child(appointmentInformation.getPatientId().replace(".",","))
                        .child(System.currentTimeMillis()+"").removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // For adding new appointment in next day

        patAppointReference.child(information.getPatientId().replace(".",",")).child("calendar")
                .child(information.getTime().replace("/","_")).
                setValue(information).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(patientAppointmentsHolder.patientName.getContext(), "", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(patientAppointmentsHolder.patientName.getContext(), "Can't add info successfully!!", Toast.LENGTH_SHORT).show();
            }
        });

        patientAppointToday.child(information.getPatientId().replace(".",",")).child("calendar")
                .child((information.getTime().split(" "))[2].replace("/","_"))
                .child((information.getTime().split(" "))[0]).setValue(information);

        bookDateSlot = database.getReference(information.getChain());

        bookDateSlot.setValue(information).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(patientAppointmentsHolder.patientName.getContext(), "Appointment Updated Successfully!! ", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(patientAppointmentsHolder.patientName.getContext(), "Can't Update Info Successfully!!", Toast.LENGTH_SHORT).show();
            }
        });

        appointmentReference.child(information.getDoctorId().replace(".",",")).child("calendar").
                child(information.getTime().replace("/","_")).setValue(information).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(patientAppointmentsHolder.patientName.getContext(), "Appointment Set Successfully!!", Toast.LENGTH_SHORT).show();
                    }
                });
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
        Button cancelAppoint;

        public PatientAppointmentsHolder(@NonNull View itemView) {
            super(itemView);
            dateAppointment = itemView.findViewById(R.id.appointment_date_patient);
            patientName = itemView.findViewById(R.id.patient_name2);
            appointmentType = itemView.findViewById(R.id.appointment_type_patient);
            type = itemView.findViewById(R.id.type);
            phone = itemView.findViewById(R.id.patient_phone2);
            image = itemView.findViewById(R.id.patient_image2);
            cancelAppoint = itemView.findViewById(R.id.cancelAppoint);
        }
    }
}
