package com.exercise.thesis.hellodoc.ui.doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.viewmodel.Hour;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;


public class AppointmentFragment extends Fragment {

    private LinearLayout lis;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String patient_email;
    private String day;
    private DatabaseReference appointRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("AppointmentInfo");
        lis = view.findViewById(R.id.listDate);
        getParentFragmentManager().setFragmentResultListener("dataFromProfile", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                patient_email = result.getString("key1");
                day = result.getString("key2");
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(140, 398);
        layoutParams.setMargins(200, 0, 300, 0);
        this.appointRef = reference.child(patient_email).child("calendar").child(day).child("hour");

        for (int i = 8; i<19;i++){
            final int j = i;
            TextView text = new TextView(getContext());
            text.setText(i + ":00");
            LinearLayout l = new LinearLayout(getContext());
            l.setMinimumHeight(250);
            l.addView(text, layoutParams);
            final Button btn = new Button(getContext());

            appointRef.child(i+"").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Hour note = snapshot.getValue(Hour.class);
                    if(note != null){
                        btn.setText("Already chosen");
                    }
                    else{
                        btn.setText("Confirm this hour!!");
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Hour h =new Hour(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                appointRef.child(j+"").setValue(h).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Can't set value successfully!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            l.addView(btn);
            lis.addView(l);
        }
    }
}