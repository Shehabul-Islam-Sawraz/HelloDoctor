package com.exercise.thesis.hellodoc.ui.patient;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.common.Common;
import com.exercise.thesis.hellodoc.model.Patient;
import com.exercise.thesis.hellodoc.ui.doctor.DossierMedical;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PatientInfoActivity extends AppCompatActivity {

    EditText heightBtn;
    EditText weightBtn;
    Spinner bloodTypeSpinner;
    Button updateBtn;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    String patient_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("patient");
        updateBtn = findViewById(R.id.updateInfoBtn);
        heightBtn = findViewById(R.id.heightBtn);
        weightBtn = findViewById(R.id.weightBtn);
        Spinner specialistList = (Spinner) findViewById(R.id.bloodType);
        ArrayAdapter<CharSequence> adapterSpecialistList = ArrayAdapter.createFromResource(this,
                R.array.blood_spinner, android.R.layout.simple_spinner_item);
        adapterSpecialistList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialistList.setAdapter(adapterSpecialistList);

        String patient_name = getIntent().getStringExtra("patient_name");
        patient_email = getIntent().getStringExtra("patient_email");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Patient patient = snapshot.child(patient_email.replace(".",",")).getValue(Patient.class);
                weightBtn.setText(patient.getWeight());
                heightBtn.setText(patient.getHeight());
                if(patient.getBloodType()!=null){
                    specialistList.setSelection(Common.convertBloodToInt(patient.getBloodType()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> map = new HashMap<>();
                map.put("height",""+heightBtn.getText());
                map.put("weight",""+weightBtn.getText());
                map.put("bloodType",""+specialistList.getSelectedItem().toString());
                Log.e("tag", "onClick: "+specialistList.getTag() );

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Patient patient = snapshot.child(patient_email.replace(".",",")).getValue(Patient.class);
                        patient.setHeight(heightBtn.getText().toString());
                        patient.setWeight(weightBtn.getText().toString());
                        patient.setBloodType(specialistList.getSelectedItem().toString());

                        reference.child(patient_email.replace(".",",")).setValue(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PatientInfoActivity.this,"Update Info Successfully!",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PatientInfoActivity.this, "Can't update info successfully!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                PatientInfoActivity.super.onBackPressed();
            }
        });
        if(Common.CurrentUserType.equals("patient")){
            updateBtn.setVisibility(View.GONE);
            heightBtn.setEnabled(false);
            weightBtn.setEnabled(false);
            specialistList.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
