package com.exercise.thesis.hellodoc.ui.patient;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.exercise.thesis.hellodoc.model.Patient;
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
import com.squareup.picasso.Picasso;

import dmax.dialog.SpotsDialog;

public class ProfilePatientFragment extends Fragment {

    private MaterialTextView patientName;
    private MaterialTextView patientPhone;
    private MaterialTextView patientEmail;
    private MaterialTextView patientBloodGroup;
    private MaterialTextView patientAbout;
    private ImageView patientImage;
    private StorageReference pathReference ;
    final String patientId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("patient");
    private View viewThis;
    private boolean hasPhoto = false;
    Patient patient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_profile_patient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewThis = view;
        this.patientName = view.findViewById(R.id.patient_name_profile);
        this.patientPhone = view.findViewById(R.id.patient_phone_profile);
        this.patientEmail = view.findViewById(R.id.patient_email_profile);
        this.patientBloodGroup = view.findViewById(R.id.patient_blood_grp);
        this.patientAbout = view.findViewById(R.id.patient_about_profile);
        this.patientImage = view.findViewById(R.id.imageView3_patient);
        Drawable defaultImage = getResources().getDrawable(R.drawable.ic_anon_user_48dp); //default user image
        /*AlertDialog dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(true).build();
        dialog.show();*/

        //display profile image
        String imageId = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",");
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId+".jpg");
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getActivity())
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(patientImage);//hna fin kayn Image view
                //dialog.dismiss();
                // profileImage.setImageURI(uri);
                hasPhoto = true;
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patient = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")).getValue(Patient.class);
                patientName.setText(patient.getFullName());
                patientPhone.setText(patient.getMblNum());
                patientEmail.setText(patient.getEmail());
                patientBloodGroup.setText(patient.getBloodType());
                if(patient.getWeight()==null || patient.getWeight().trim().equals("")){
                    if(patient.getHeight()==null || patient.getHeight().trim().equals("")){
                        patientAbout.setText("");
                    }
                    else {
                        patientAbout.setText("Height: "+patient.getHeight());
                    }
                }
                else if(patient.getHeight()==null || patient.getHeight().trim().equals("")){
                    patientAbout.setText("Weight: "+patient.getWeight());
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
        Navigation.findNavController(viewThis).navigate(R.id.action_profilePatientFragment_to_homepageFragment);
        //((AppCompatActivity)getActivity()).finish();
    }

    private void startEditActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("pat_name", patient.getFullName());
        bundle.putString("pat_phone", patient.getMblNum());
        bundle.putString("pat_bg", patient.getBloodType());
        bundle.putString("pat_email",patient.getEmail());
        bundle.putString("pat_weight",patient.getWeight());
        bundle.putString("pat_height",patient.getHeight());
        if(patient.getPhotoUrl()==null){
            bundle.putString("pat_photo","null");
        }
        else{
            bundle.putString("pat_photo",patient.getPhotoUrl().toString());
        }

        System.out.println("Sending patient email: "+patient.getEmail());
        getParentFragmentManager().setFragmentResult("PatientEdit", bundle);
        Navigation.findNavController(viewThis).navigate(R.id.action_profilePatientFragment_to_editPatientProfile);
    }
}

