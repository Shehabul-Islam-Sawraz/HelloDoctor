package com.exercise.thesis.hellodoc.ui.doctor;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.common.Common;
import com.exercise.thesis.hellodoc.model.Fiche;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import id.zelory.compressor.Compressor;

public class FicheActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText disease;
    private EditText description;
    private EditText treatment;
    private Spinner recordsType;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private StorageReference storage;
    SimpleDateFormat simpleDateFormat;
    private boolean isImageSelected = false;
    private ImageView addImage, imgPreview;
    private File compressedFile = null;
    private Uri uriImage = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("PatientMedicalFolder");
        this.storage = FirebaseStorage.getInstance().getReference("uploads");
        disease = findViewById(R.id.disease);
        description = findViewById(R.id.disease_description);
        treatment = findViewById(R.id.disease_treatment);
        recordsType = findViewById(R.id.records_type_spinner);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        addImage = findViewById(R.id.add_pres_img);
        imgPreview = findViewById(R.id.pres_img_preview);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setTitle("Saving Medical Records...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        //Spinner to choose fiche type
        Spinner spinner = findViewById(R.id.records_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.records_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Add fiche
        Button addFicheButton = findViewById(R.id.button_add_records);
        addFicheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFiche();
            }
        });
    }

    private void selectImage() {
        ImagePicker.create(this).single().start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(ImagePicker.shouldHandle(requestCode,resultCode,data)){
            Image selectedImg = ImagePicker.getFirstImageOrNull(data);
            try {
                compressedFile = new Compressor(this).setQuality(75)
                        .compressToFile(new File(selectedImg.getPath()));

                isImageSelected = true;
                addImage.setVisibility(View.GONE);
                imgPreview.setVisibility(View.VISIBLE);

                Glide.with(FicheActivity.this)
                        .load(selectedImg.getPath())
                        .error(R.drawable.ef_image_placeholder)
                        .placeholder(R.drawable.ef_image_placeholder)
                        .into(imgPreview);
                if(selectedImg!=null){
                    uriImage = selectedImg.getUri();
                }
            }catch (Exception e){
                imgPreview.setVisibility(View.GONE);
                addImage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String SelectedFicheType = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private String getImageType(Uri uri) {
        ContentResolver cr = getApplicationContext().getContentResolver();
        MimeTypeMap mp = MimeTypeMap.getSingleton();
        return mp.getExtensionFromMimeType(cr.getType(uri));
    }

    private void addFiche(){
        String diseaseRecord;
        String descriptionRecords;
        String treatmentRecords;
        String typeRecords = recordsType.getSelectedItem().toString();
        if(disease.getText()==null || disease.getText().equals("")){
            diseaseRecord = "";
        }
        else{
            diseaseRecord = disease.getText().toString();
        }
        if(description.getText()==null || description.getText().equals("")){
            descriptionRecords = "";
        }
        else{
            descriptionRecords = description.getText().toString();
        }
        if(treatment.getText()==null || treatment.getText().equals("")){
            treatmentRecords = "";
        }
        else{
            treatmentRecords = treatment.getText().toString();
        }

        String patient_name = getIntent().getStringExtra("patient_name");
        String patient_email = getIntent().getStringExtra("patient_email").replace(".",",");

        final Uri[] uriUpload = {null};
        if(isImageSelected && uriImage!=null){
            StorageReference ref = storage.child(System.currentTimeMillis() + "." + getImageType(uriImage));
            progressDialog.show();
            ref.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uriUpload[0] = uri;
                        }
                    });
                    //Toast.makeText(getActivity(), "Upload Successful!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    //progressBar.setVisibility(View.VISIBLE);
                    double progressPercent = (double) (100* snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                    progressDialog.setMessage("Progress: "+ (int)progressPercent + "%");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //progressBar.setVisibility(View.INVISIBLE);
                    progressDialog.dismiss();
                    uriUpload[0] = null;
                }
            });

        }

        Fiche records = new Fiche(diseaseRecord, descriptionRecords, treatmentRecords, typeRecords, FirebaseAuth.getInstance().getCurrentUser().getEmail(), Calendar.getInstance().getTime(), uriUpload[0]);
        reference.child(patient_email).child(System.currentTimeMillis()+"").setValue(records).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(FicheActivity.this, "Records added of "+ patient_name, Toast.LENGTH_LONG).show();
            }
        });
        FicheActivity.super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
