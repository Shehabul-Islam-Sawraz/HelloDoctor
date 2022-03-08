package com.exercise.thesis.hellodoc.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.common.Common;
import com.exercise.thesis.hellodoc.model.Doctor;
import com.exercise.thesis.hellodoc.model.RequestNote;
import com.exercise.thesis.hellodoc.ui.patient.TestActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class DoctorAdapterFiltred extends RecyclerView.Adapter<DoctorAdapterFiltred.DoctorHolder2> implements Filterable {
    public static boolean specialistSearch = false;
    static String doc;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference imgReference;
    private List<Doctor> mTubeList = new ArrayList<>();
    private List<Doctor> mTubeListFiltered = new ArrayList<>();
    StorageReference pathReference ;
    private Uri profilePhoto = null;


    public DoctorAdapterFiltred(List<Doctor> tubeList){
        mTubeList = tubeList;
        mTubeListFiltered = tubeList;
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("Request");
        this.imgReference = database.getReference("images");
    }

    @NonNull
    @Override
    public DoctorHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item,
                parent, false);
        return new DoctorHolder2(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorHolder2 doctorHolder, int i) {
        final Doctor doctor = mTubeListFiltered.get(i);
        final TextView t = doctorHolder.title ;
        doctorHolder.title.setText(doctor.getFullName());

        imgReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imgUrl = snapshot.child(doctor.getEmail().replace(".",",")).child("imageUri").getValue(String.class);
                //Toast.makeText(getActivity(), "ImgUrl: "+imgUrl, Toast.LENGTH_SHORT).show();
                if(imgUrl!=null){
                    profilePhoto = Uri.parse(imgUrl);
                    Picasso.with(doctorHolder.image.getContext())
                            .load(profilePhoto.toString())
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .centerCrop()
                            .into(doctorHolder.image, new Callback() {
                                @Override
                                public void onSuccess() {
                                    //dialog.dismiss();
                                }

                                @Override
                                public void onError() {
                                    //Toast.makeText(getActivity(), "Profile photo load error!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                    //Toast.makeText(getActivity(), "profile: "+profilePhoto, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        doctorHolder.specialities.setText("Speciality : "+doctor.getSpecialities());
        if(!doctor.getAbout().equals("")){
            doctorHolder.showAbout.setVisibility(View.VISIBLE);
            doctorHolder.showAbout.setText(doctor.getAbout());
        }
        else {
            doctorHolder.showAbout.setVisibility(View.GONE);
        }
        doctorHolder.showFees.setText("Fees : " + doctor.getFees().trim() + " Tk");
        if(!doctor.getPhoneNum().equals("")) {
            doctorHolder.showPhone.setVisibility(View.VISIBLE);
            doctorHolder.showPhone.setText("Phone Number : " + doctor.getPhoneNum());
        }
        else {
            doctorHolder.showPhone.setVisibility(View.GONE);
        }
        doctorHolder.showAddress.setText("Hospital : " + doctor.getAddress());

        final String idPat = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final String idDoc = doctor.getEmail();
        doctorHolder.addDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestNote note = new RequestNote(idPat,idDoc);
                reference.child(System.currentTimeMillis()+"").setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(t.getContext(), "Request Confirmed!!", Toast.LENGTH_SHORT).show();
                    }
                });
                doctorHolder.addDoc.setVisibility(View.INVISIBLE);
            }
        });
        doctorHolder.appointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour24hrs = Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(Calendar.HOUR_OF_DAY);
                if(hour24hrs<16){
                    doc= doctor.getEmail();
                    Common.CurrentDoctor = doctor.getEmail();
                    Common.CurrentDoctorName = doctor.getFullName();
                    Common.CurrentPhone = doctor.getPhoneNum();
                    openPage(v.getContext());
                }
                else{
                    Toast.makeText(v.getContext(), "Now hour is: " + Integer.toString(hour24hrs), Toast.LENGTH_SHORT).show();
                    Toast.makeText(v.getContext(), "Sorry, Appointment Booking Time Ended. Try Again Tomorrow Before 12 PM!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTubeListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String pattern = constraint.toString().toLowerCase();
                if(pattern.isEmpty()){
                    mTubeListFiltered = mTubeList;
                } else {
                    List<Doctor> filteredList = new ArrayList<>();
                    for(Doctor tube: mTubeList){
                        if(specialistSearch == false) {
                            if (tube.getFullName().toLowerCase().contains(pattern) || tube.getFullName().toLowerCase().contains(pattern)) {
                                filteredList.add(tube);
                            }
                        }
                        else{
                            if (tube.getSpecialities().toLowerCase().contains(pattern) || tube.getSpecialities().toLowerCase().contains(pattern)) {
                                filteredList.add(tube);
                            }
                        }
                    }
                    mTubeListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mTubeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mTubeListFiltered = (ArrayList<Doctor>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    class DoctorHolder2 extends RecyclerView.ViewHolder {

        Button appointmentBtn;
        TextView title;
        TextView specialities;
        TextView showAbout;
        TextView showFees;
        TextView showPhone;
        TextView showAddress;
        ImageView image;
        Button addDoc;
        Button load;
        public DoctorHolder2(@NonNull View itemView) {
            super(itemView);
            addDoc = itemView.findViewById(R.id.addDocBtn);
            title= itemView.findViewById(R.id.doctor_view_title3);
            specialities=itemView.findViewById(R.id.text_view_description4);
            image=itemView.findViewById(R.id.doctor_item_image);
            appointmentBtn=itemView.findViewById(R.id.appointmentBtn);
            showAbout = itemView.findViewById(R.id.showAbout);
            showFees = itemView.findViewById(R.id.showFees);
            showPhone = itemView.findViewById(R.id.showPhone);
            showAddress = itemView.findViewById(R.id.showAddress);
        }
    }
    private void openPage(Context wf){
        Intent i = new Intent(wf, TestActivity.class);
        wf.startActivity(i);
    }
}
