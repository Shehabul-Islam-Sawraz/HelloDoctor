package com.exercise.thesis.hellodoc.ui.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.common.Common;
import com.exercise.thesis.hellodoc.model.Patient;
import com.exercise.thesis.hellodoc.repository.PatientAuthRepository;
import com.exercise.thesis.hellodoc.ui.DrawerLocker;
import com.exercise.thesis.hellodoc.ui.doctor.DossierMedical;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomepageFragment extends Fragment {

    private static int storagePermission = 1;
    private static int contactPermission = 1;
    private static int callPermission = 1;

    private Button SignOutBtn;
    private Button searchPatBtn;
    private Button myDoctors;
    private Button BtnRequest;
    private Button profile;
    private Button appointment, easyAccess;
    private Button callEmg;
    private View viewThis;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("patient");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((DrawerLocker) getActivity()).setDrawerEnabled(true);

        this.viewThis = view;
        Common.CurrentUserType = "patient";
        appointment = view.findViewById(R.id.appointment2);
        callEmg = view.findViewById(R.id.callEmergencyPatient);
        searchPatBtn = view.findViewById(R.id.searchBtn);
        SignOutBtn=view.findViewById(R.id.signOutBtnPatient);
        myDoctors = view.findViewById(R.id.myDoctors);
        BtnRequest = view.findViewById(R.id.btnRequestPatient);
        profile = view.findViewById(R.id.profilePatient);
        easyAccess = view.findViewById(R.id.easyAccess);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((DrawerLocker) getActivity()).setDrawerEnabled(false);

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    //Nothing to do. All permission granted
                }
                else{
                    callPermissionRequest();
                }
            }
            else{
                contactPermissionRequest();
            }
        }
        else{
            requestExternalPermission();
        }

        BtnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DossierMedical.class);
                intent.putExtra("patient_email",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                startActivity(intent);
            }
        });
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(viewThis).navigate(R.id.action_homepageFragment_to_patientAppointmentFragment);
            }
        });
        searchPatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(viewThis).navigate(R.id.action_homepageFragment_to_allCategories);
            }
        });
        SignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatientAuthRepository.getInstance(getActivity().getApplication()).getFirebaseAuth().signOut();
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(viewThis).navigate(R.id.action_homepageFragment_to_confirmationFragment);
            }
        });
        myDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(viewThis).navigate(R.id.action_homepageFragment_to_myDoctorsFragment);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(viewThis).navigate(R.id.action_homepageFragment_to_profilePatientFragment);
            }
        });
        easyAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(viewThis).navigate(R.id.action_homepageFragment_to_easyAccess2);
            }
        });
        callEmg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    String number = "999";
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+number));
                    startActivity(intent);
                }
                else{
                    callPermissionRequest();
                }
            }
        });

        Common.CurrentUserid= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        reference.child(Common.CurrentUserid.replace(".",",")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Patient patient = snapshot.getValue(Patient.class);
                Common.CurrentUserName = patient.getFullName();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == storagePermission){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                contactPermissionRequest();
            }
            else{
                Toast.makeText(getActivity(), "Permission Denied!!", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == contactPermission){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                callPermissionRequest();
            }
            else{
                Toast.makeText(getActivity(), "Permission Denied!!", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == callPermission){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
            else{
                Toast.makeText(getActivity(), "Permission Denied!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestExternalPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission Needed")
                    .setMessage("This permission will help us to make your usage more compatible!!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, storagePermission);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    contactPermissionRequest();
                }
            }).create().show();
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, storagePermission);
        }
    }
    private void contactPermissionRequest()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission Needed")
                    .setMessage("This permission will help us to make your usage more compatible!!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, contactPermission);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    callPermissionRequest();
                }
            }).create().show();
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, contactPermission);
        }
    }

    private void callPermissionRequest()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission Needed")
                    .setMessage("This permission will help us to make your usage more compatible!!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, callPermission);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, callPermission);
        }
    }
}