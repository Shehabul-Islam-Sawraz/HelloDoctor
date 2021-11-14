package com.exercise.thesis.hellodoc.ui.patient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.common.Common;
import com.exercise.thesis.hellodoc.model.Patient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class EditPatientProfile extends Fragment {

    private ImageView profileImage;
    private ImageButton selectImage;
    private Button updateProfile;
    private TextInputEditText patientPhone;
    private TextInputEditText patientBg;
    private View viewThis;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ActivityResultLauncher<String> getPhoto;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ProgressDialog progressDialog;
    private StorageReference pathReference;
    private String pat_infected, pat_situation, pat_height, pat_weight, pat_name, pat_email;
    private Uri pat_photo;
    private Uri uriImage;
    private StorageReference pStorageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private String current_phone,current_bg;
    private static int storagePermission = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted->{
            if(isGranted==false){
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            else{
                return;
            }
        });

        getPhoto = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>(){
            @Override
            public void onActivityResult(Uri result) {
                uriImage = result;
                Picasso.with(getActivity())
                        .load(uriImage)
                        .placeholder(R.drawable.doctor1)
                        .fit()
                        .centerCrop()
                        .into(profileImage, new Callback() { //Store here the imageView
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getActivity(), "Profile picture showed successfully!!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(getActivity(), "Can't show profile picture!!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_patient_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewThis = view;
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("patient");
        pStorageRef = FirebaseStorage.getInstance().getReference("DoctorProfile");
        profileImage = view.findViewById(R.id.image_profile_patient);
        selectImage = view.findViewById(R.id.select_image_patient);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading photo...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        //progressBar.setVisibility(View.INVISIBLE);
        updateProfile = view.findViewById(R.id.update);
        patientPhone = view.findViewById(R.id.phoneText);
        patientBg = view.findViewById(R.id.bgText);
        //Set the default information in the text fields
        patientPhone.setText(current_phone);
        patientBg.setText(current_bg);
        getParentFragmentManager().setFragmentResultListener("PatientEdit", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                current_phone = result.getString("pat_phone");
                current_bg = result.getString("pat_bg");
                pat_situation  = result.getString("pat_situation");
                pat_infected = result.getString("pat_infected");
                pat_height = result.getString("pat_height");
                pat_weight = result.getString("pat_weight");
                pat_name = result.getString("pat_name");
                pat_email = result.getString("pat_email");
                if(result.getString("pat_photo").equals("null")){
                    pat_photo = null;
                }
                else{
                    pat_photo = Uri.parse(result.getString("pat_photo"));
                }
            }
        });
        //System.out.println("Patient Email: "+pat_email);
        new Handler(Looper.myLooper()).postDelayed(()->{
            String userPhotoPath = pat_email.replace(".",",") + ".jpg";
            pathReference = storageRef.child("DoctorProfile/" + userPhotoPath); //Doctor photo in database
            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(getActivity())
                            .load(uri)
                            .placeholder(R.drawable.doctor1)
                            .fit()
                            .centerCrop()
                            .into(profileImage);//Store here the imageView

                    // profileImage.setImageURI(uri);
                }
            });
        }, 1500);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updateBg = patientBg.getText().toString();
                String updatePhone = patientPhone.getText().toString();
                if(updatePhone==null || updatePhone.equals("") || updateBg==null || updateBg.equals("")){
                    Toast.makeText(getActivity(), "Please provide correct information!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(uriImage!=null){
                        uploadProfileImage();
                    }
                    updatePatientInfo(pat_name, updateBg, updatePhone,pat_email,pat_photo);
                }
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermissionAndBrowseFile();
            }
        });
    }

    private void askPermissionAndBrowseFile()  {
        // With Android Level >= 21, you have to ask the user
        // for permission to access External Storage.
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // Level 21

            // Check if we have Call permission
            int permission = ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            permission+= ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        this.doBrowseFile();
    }

    private void doBrowseFile()  {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            getPhoto.launch("image/*");
        }
        else{
            requestExternalPermission();
        }
    }

    private String getImageType(Uri uri) {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mp = MimeTypeMap.getSingleton();
        return mp.getExtensionFromMimeType(cr.getType(uri));
    }


    private void updatePatientInfo(String name, String bg, String phone, String email, Uri photo) {
        Patient pat;
        if(uriImage==null){
            pat = new Patient(name,email,phone,photo);
        }
        else{
            pat = new Patient(name,email,phone,uriImage);
        }
        pat.setBloodType(bg);
        pat.setWeight(pat_weight);
        pat.setHeight(pat_height);
        reference.child(email.replace(".",",")).setValue(pat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //Toast.makeText(getActivity(), name + "." + address + "." + phone + "." + email + "." + spe, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Profile Updated Successfully!!", Toast.LENGTH_SHORT).show();
                Common.CurrentUserid = email;
                Navigation.findNavController(viewThis).navigate(R.id.action_editPatientProfile_to_profilePatientFragment);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Can't update profile successfully!!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /* Used to upload the doctor image in the DataBase */
    private void uploadProfileImage() {
        /* check if the image is not null */
        if (uriImage != null) {
            StorageReference storageReference = pStorageRef.child(pat_email.replace(".",",")
                    + "." + getImageType(uriImage));
            storageReference.putFile(uriImage).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return pStorageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        //Log.e(TAG, "then: " + downloadUri.toString());
                        Toast.makeText(getActivity(), "Image Saved Successfully!!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), "Upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
                }
            }).create().show();
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, storagePermission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == storagePermission) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(getActivity(), "Permission Denied!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
