package com.exercise.thesis.hellodoc.ui.doctor;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.model.Fiche;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FicheActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText disease;
    private EditText description;
    private EditText treatment;
    private Spinner recordsType;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("PatientMedicalFolder");
        disease = findViewById(R.id.disease);
        description = findViewById(R.id.disease_description);
        treatment = findViewById(R.id.disease_treatment);
        recordsType = findViewById(R.id.records_type_spinner);

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String SelectedFicheType = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void addFiche(){
        String diseaseRecord = disease.getText().toString();
        String descriptionRecords =  description.getText().toString();
        String treatmentRecords = treatment.getText().toString();
        String typeRecords = recordsType.getSelectedItem().toString();

        String patient_name = getIntent().getStringExtra("patient_name");
        String patient_email = getIntent().getStringExtra("patient_email");

        Fiche records = new Fiche(diseaseRecord, descriptionRecords, treatmentRecords, typeRecords, FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.child(patient_email).setValue(records).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(FicheActivity.this, "Records added of "+ patient_name, Toast.LENGTH_LONG).show();
            }
        });
        finish();
    }

}
