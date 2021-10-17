package com.exercise.thesis.hellodoc.ui.doctor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.common.Common;
import com.exercise.thesis.hellodoc.model.Doctor;
import com.exercise.thesis.hellodoc.model.Image;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class EditDoctorProfile extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "EditProfileDoctorActivity";
    private ImageView profileImage;
    private ImageButton selectImage;
    private Button updateProfile;
    private TextInputEditText doctorName;
    private TextInputEditText doctorEmail;
    private TextInputEditText doctorPhone;
    private TextInputEditText doctorAddress;
    private TextInputEditText doctorSpe;
    private TextInputEditText doctorAbout;
    final String currentDoctorUID = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
    final String doctorID = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
    private Uri uriImage = null;
    private String docId;
    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;
    private View viewThis;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference imageRef;
    private StorageReference storage;
    //private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private ActivityResultLauncher<String> getPhoto;
    private ActivityResultLauncher<String> requestPermissionLauncher;

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
                        .placeholder(R.drawable.doctor)
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
        return inflater.inflate(R.layout.fragment_edit_doctor_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewThis = view;
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("doctor");
        this.imageRef = database.getReference("images");
        this.storage = FirebaseStorage.getInstance().getReference("uploads");
        this.docId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading photo...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        //progressBar.setVisibility(View.INVISIBLE);
        profileImage = view.findViewById(R.id.image_profile);
        selectImage = view.findViewById(R.id.select_image);
        updateProfile = view.findViewById(R.id.update);
        doctorName = view.findViewById(R.id.nameText);
        doctorPhone = view.findViewById(R.id.phoneText);
        doctorEmail = view.findViewById(R.id.emailText);
        doctorAddress = view.findViewById(R.id.addressText);
        doctorSpe = view.findViewById(R.id.specialitiesText);
        doctorAbout = view.findViewById(R.id.aboutText);

        getParentFragmentManager().setFragmentResultListener("DoctorEdit", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String current_name = result.getString("doc_name");
                String current_phone = result.getString("doc_phone");
                String current_address = result.getString("doc_hos");
                String current_photo = result.getString("doc_photo");
                String current_email = result.getString("doc_email");
                String current_spe = result.getString("doc_specialities");
                String current_about = result.getString("doc_about");

                //Set the default information in the text fields
                doctorName.setText(current_name);
                doctorPhone.setText(current_phone);
                doctorAddress.setText(current_address);
                doctorEmail.setText(current_email);
                doctorSpe.setText(current_spe);
                doctorAbout.setText(current_about);

                if(!current_photo.equals("null")){
                    Picasso.with(getActivity())
                            .load(Uri.parse(current_photo))
                            .placeholder(R.drawable.doctor)
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
                // profileImage.setImageURI(uri);
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermissionAndBrowseFile();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updateAddress = doctorAddress.getText().toString();
                String updateName = doctorName.getText().toString();
                String updateEmail = doctorEmail.getText().toString();
                String updatePhone = doctorPhone.getText().toString();
                String updateSpe = doctorSpe.getText().toString();
                String updateAbout = doctorAbout.getText().toString();
                if(updateName==null || updateName.equals("") || updateEmail==null || updateEmail.equals("") || updateAddress==null || updateAddress.equals("")){
                    Toast.makeText(getActivity(), "Please provide correct information!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(updatePhone==null){
                        updatePhone = "";
                    }
                    if(updateSpe==null){
                        updateSpe = "";
                    }
                    if(updateAbout==null){
                        updateAbout = "";
                    }
                    if(uriImage!=null){
                        uploadImageToFirebase(uriImage);
                    }
                    updateDoctorInfo(updateName, updateAddress, updatePhone,updateEmail,updateSpe,updateAbout);
                }

            }
        });
    }

    /* Update the doctor info in the database */
    private void updateDoctorInfo(String name, String address, String phone, String email, String spe, String about) {
        /*DocumentReference documentReference = doctorRef.collection("Doctor").document("" + doctorID + "");
        documentReference.update("adresse", address);
        //documentReference.update("email", email);
        documentReference.update("name", name);
        documentReference.update("tel", phone)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfileDoctorActivity.this, "Infos Updated", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileDoctorActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Androidview", e.getMessage());
                    }
                });*/

        Doctor doc = new Doctor(name,email,address,uriImage);
        doc.setAbout(about);
        doc.setPhoneNum(phone);
        doc.setSpecialities(spe);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(doc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //Toast.makeText(getActivity(), name + "." + address + "." + phone + "." + email + "." + spe, Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Profile Updated Successfully!!", Toast.LENGTH_SHORT).show();
                Common.CurrentDoctor = email;
                Navigation.findNavController(viewThis).navigate(R.id.action_editDoctorProfile_to_aboutDoctorFragment);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Can't update profile successfully!!", Toast.LENGTH_SHORT).show();
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
                /*this.requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_PERMISSION
                );*/

                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        this.doBrowseFile();
    }

    private void doBrowseFile()  {
        /*Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("image/*");
        // Only return URIs that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);*/


        getPhoto.launch("image/*");
    }
    private void uploadImageToFirebase(Uri uri){
        StorageReference ref = storage.child(System.currentTimeMillis() + "." + getImageType(uri));
        progressDialog.show();
        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Image image = new Image(uri.toString());
                        imageRef.child(docId).setValue(image);
                        Toast.makeText(getActivity(), "Upload Successful!!", Toast.LENGTH_SHORT).show();
                    }
                });
                //Toast.makeText(getActivity(), "Upload Successful!!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                //progressBar.setVisibility(View.VISIBLE);
                double progressPercent = (100* snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                progressDialog.setMessage("Progress: "+ (int)progressPercent + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //progressBar.setVisibility(View.INVISIBLE);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Can't upload image to database!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getImageType(Uri uri) {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mp = MimeTypeMap.getSingleton();
        return mp.getExtensionFromMimeType(cr.getType(uri));
    }







    // When you have the request results
    /*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case MY_REQUEST_CODE_PERMISSION: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (CALL_PHONE).
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i(LOG_TAG,"Permission granted!");
                    Toast.makeText(this.getContext(), "Permission granted!", Toast.LENGTH_SHORT).show();

                    this.doBrowseFile();
                }
                // Cancelled or denied.
                else {
                    Log.i(LOG_TAG,"Permission denied!");
                    Toast.makeText(this.getContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }*/


    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MY_RESULT_CODE_FILECHOOSER:
                if (resultCode == Activity.RESULT_OK ) {
                    if(data != null)  {
                        Uri fileUri = data.getData();
                        Log.i(LOG_TAG, "Uri: " + fileUri);

                        String filePath = null;
                        try {
                            filePath = FileUtils.getPath(this.getContext(),fileUri);
                        } catch (Exception e) {
                            Log.e(LOG_TAG,"Error: " + e);
                            Toast.makeText(this.getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                        this.editTextPath.setText(filePath);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/






    /* Used to choose a file */
    /*private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }*/




    /* used to get the data back */
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriImage = data.getData();
            Picasso.with(this).load(uriImage).into(profileImage);
        }
    }*/




}
