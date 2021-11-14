package com.exercise.thesis.hellodoc.adapter;

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MyDoctorsAdapter extends FirebaseRecyclerAdapter<Doctor, MyDoctorsAdapter.MyDoctorAppointmentHolder> {

    StorageReference pathReference ;
    private DatabaseReference imgReference = FirebaseDatabase.getInstance().getReference("images");
    private Uri profilePhoto = null;

    public MyDoctorsAdapter(@NonNull FirebaseRecyclerOptions<Doctor> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyDoctorAppointmentHolder myDoctorsHolder, int position, @NonNull final Doctor doctor) {
        myDoctorsHolder.textViewTitle.setText(doctor.getFullName());
        myDoctorsHolder.textViewDescription.setText("Specialities : "+doctor.getSpecialities());
        myDoctorsHolder.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(v.getContext(),doctor);
            }
        });
        myDoctorsHolder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(myDoctorsHolder.sendMessageButton.getContext(),doctor.getPhoneNum());
            }
        });

        /*String imageId = doctor.getEmail().replace(".",",")+".jpg"; //add a title image
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(myDoctorsHolder.imageViewDoctor.getContext())
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(myDoctorsHolder.imageViewDoctor);//Image location

                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });*/


        imgReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imgUrl = snapshot.child(doctor.getEmail().replace(".",",")).child("imageUri").getValue(String.class);
                //Toast.makeText(getActivity(), "ImgUrl: "+imgUrl, Toast.LENGTH_SHORT).show();
                if(imgUrl!=null){
                    profilePhoto = Uri.parse(imgUrl);
                    Picasso.with(myDoctorsHolder.imageViewDoctor.getContext())
                            .load(profilePhoto.toString())
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .centerCrop()
                            .into(myDoctorsHolder.imageViewDoctor, new Callback() {
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



    }

    private void openPage(Context wf, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        wf.startActivity(intent);
    }

    private void openPage(Context wf, Doctor d){
        Toast.makeText(wf, "Sorry, This functionality is unavailable for now!!", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public MyDoctorAppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_doctor_item, parent, false);
        return new MyDoctorAppointmentHolder(v);
    }

    class MyDoctorAppointmentHolder extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorItems
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewStatus;
        ImageView imageViewDoctor;
        Button sendMessageButton;
        Button callBtn;
        public MyDoctorAppointmentHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.doctor_view_title_doctor);
            textViewDescription = itemView.findViewById(R.id.text_view_description_doctor);
            textViewStatus = itemView.findViewById(R.id.onlineState);
            imageViewDoctor = itemView.findViewById(R.id.doctor_item_image_doctor);
            sendMessageButton = itemView.findViewById(R.id.contact_btn);
            callBtn = itemView.findViewById(R.id.callBtnDoctor);
        }
    }
}
