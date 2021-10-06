package com.exercise.thesis.hellodoc.ui.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.model.Doctor;
import com.exercise.thesis.hellodoc.repository.DoctorAuthRepository;
import com.exercise.thesis.hellodoc.ui.DrawerLocker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorProfileFragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("doctor");

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((DrawerLocker) getActivity()).setDrawerEnabled(true);

        Button logOutButton = view.findViewById(R.id.log_out);
        TextView textView = view.findViewById(R.id.demo_txt);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doctor doctor = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(Doctor.class);
                String text = "Yo";
                if (doctor != null) {
                    System.out.println(doctor.toString());
                    text = "Name-> " + doctor.getFullName() + "\nEmail-> " + doctor.getEmail() + "\nUsername-> " + doctor.getUserName();
                }
                textView.setText(text);
                Toast.makeText(getActivity(), "Welcome "+doctor.getUserName(), Toast.LENGTH_SHORT).show();
//                System.out.println("WHY");
//                System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logOutButton.setOnClickListener(v -> {

            DoctorAuthRepository.getInstance(getActivity().getApplication()).getFirebaseAuth().signOut();

            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            ((DrawerLocker) getActivity()).setDrawerEnabled(false);

            getActivity().getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            Navigation.findNavController(view).navigate(R.id.action_doctorProfileFragment_to_welcomeFragment);
        });
    }
}