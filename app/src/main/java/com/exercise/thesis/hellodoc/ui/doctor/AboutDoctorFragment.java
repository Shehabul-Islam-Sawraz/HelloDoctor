package com.exercise.thesis.hellodoc.ui.doctor;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.model.Doctor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class AboutDoctorFragment extends Fragment {

    private MaterialTextView doctorName;
    private MaterialTextView doctorSpe;
    private MaterialTextView doctorPhone;
    private MaterialTextView doctorEmail;
    private MaterialTextView doctorAddress;
    private MaterialTextView doctorAbout;
    private MaterialTextView doctorFees;
    private MaterialTextView doctorRating;
    private ImageView doctorImage;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference imgReference;
    private View viewThis;
    private Uri profilePhoto = null;
    private Doctor doctor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_about_doctor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("doctor");
        this.imgReference = database.getReference("images");
        viewThis = view;



        doctorImage = view.findViewById(R.id.imageView3);
        doctorName = view.findViewById(R.id.doctor_name);
        doctorSpe = view.findViewById(R.id.doctor_specialities);
        doctorPhone = view.findViewById(R.id.doctor_phone);
        doctorEmail = view.findViewById(R.id.doctor_email);
        doctorAddress = view.findViewById(R.id.doctor_address);
        doctorAbout = view.findViewById(R.id.doctor_about);
        doctorFees = view.findViewById(R.id.doctor_fees);
        doctorRating = view.findViewById(R.id.rating_doctor);
        //AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setCancelable(true).build();
        //AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(view).setCancelable(true).create();
        //dialog.show();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctor = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")).getValue(Doctor.class);
                doctorName.setText(doctor.getFullName());
                doctorSpe.setText(doctor.getSpecialities());
                if(doctor.getAbout().equals("")){
                    doctorPhone.setText("Not Updated Yet");
                }
                else {
                    doctorPhone.setText(doctor.getPhoneNum());
                }
                doctorEmail.setText(doctor.getEmail());
                doctorAddress.setText(doctor.getAddress());
                if(doctor.getAbout().equals("")){
                    doctorAbout.setText("Not Updated Yet");
                }
                else {
                    doctorAbout.setText(doctor.getAbout());
                }
                if(doctor.getFees().equals("")){
                    doctorFees.setText("Not Updated Yet");
                }
                else{
                    doctorFees.setText(doctor.getFees() + " TK");
                }
                if(doctor.getAvgRating()=="0.0"){
                    doctorRating.setVisibility(View.GONE);
                }
                else{
                    doctorRating.setVisibility(View.VISIBLE);
                    doctorRating.setText("Performance rate: "+doctor.getAvgRating());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imgUrl = snapshot.child(doctor.getEmail().replace(".",",")).child("imageUri").getValue(String.class);
                //Toast.makeText(getActivity(), "ImgUrl: "+imgUrl, Toast.LENGTH_SHORT).show();
                if(imgUrl!=null){
                    profilePhoto = Uri.parse(imgUrl);
                    Picasso.with(getActivity())
                            .load(profilePhoto.toString())
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .centerCrop()
                            .into(doctorImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    //dialog.dismiss();
                                }

                                @Override
                                public void onError() {
                                    Toast.makeText(getActivity(), "Profile photo load error!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                    //Toast.makeText(getActivity(), "profile: "+profilePhoto, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.top_app_bar, menu);
        super.onCreateOptionsMenu(menu,((AppCompatActivity)getActivity()).getMenuInflater());
    }

    //Handling Action Bar button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //Back button
            case R.id.back:
                //If this activity started from other activity
                //((AppCompatActivity)getActivity()).finish();
                //Toast.makeText(getActivity(), "Back button pressed", Toast.LENGTH_SHORT).show();
                startHomeActivity();
                return true;

            case R.id.edit:
                //If the edit button is clicked.
                //Toast.makeText(getActivity(), "edit button pressed", Toast.LENGTH_SHORT).show();
                startEditActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startHomeActivity() {
        Navigation.findNavController(viewThis).navigate(R.id.action_aboutDoctorFragment_to_doctorProfileFragment);
        //((AppCompatActivity)getActivity()).finish();
    }

    private void startEditActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("doc_name", doctorName.getText().toString());
        bundle.putString("doc_phone", doctorPhone.getText().toString());
        bundle.putString("doc_hos", doctorAddress.getText().toString());
        if(profilePhoto==null){
            bundle.putString("doc_photo","null");
        }
        else{
            bundle.putString("doc_photo",profilePhoto.toString());
        }
        bundle.putString("doc_email",doctorEmail.getText().toString());
        bundle.putString("doc_specialities",doctorSpe.getText().toString());
        bundle.putString("doc_about",doctorAbout.getText().toString());
        bundle.putString("doc_fees",doctorFees.getText().toString());
        bundle.putString("doc_avg_rating", doctor.getAvgRating());
        bundle.putString("doc_no_rating", doctor.getNoOfRating()+"");
        getParentFragmentManager().setFragmentResult("DoctorEdit", bundle);
        Navigation.findNavController(viewThis).navigate(R.id.action_aboutDoctorFragment_to_editDoctorProfile);
    }
}