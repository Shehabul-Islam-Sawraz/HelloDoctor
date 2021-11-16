package com.exercise.thesis.hellodoc.ui.doctor;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.common.Common;
import com.exercise.thesis.hellodoc.model.Doctor;
import com.exercise.thesis.hellodoc.repository.DoctorAuthRepository;
import com.exercise.thesis.hellodoc.ui.DrawerLocker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DoctorProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private View viewThis;
    private static boolean isWelcomed = false;

    static String doc;
    Button signOutBtn2;
    Button BtnRequest;
    Button listPatients;
    Button appointmentBtn;
    Button profileBtn;
    Button myCalendarBtn;

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
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("doctor");
        this.viewThis = view;
        Common.CurrentDoctor = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Common.CurrentUserType = "doctor";
        listPatients = view.findViewById(R.id.listPatients);
        BtnRequest=view.findViewById(R.id.btnRequest);
        signOutBtn2=view.findViewById(R.id.signOutBtn);
        appointmentBtn = view.findViewById(R.id.appointment);
        profileBtn = view.findViewById(R.id.profile);
        myCalendarBtn = view.findViewById(R.id.myCalendarBtn);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((DrawerLocker) getActivity()).setDrawerEnabled(false);

        //Button logOutButton = view.findViewById(R.id.log_out);
        //TextView textView = view.findViewById(R.id.demo_txt);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doctor doctor = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")).getValue(Doctor.class);
                //String text = "Yo";
                if (doctor != null) {
                    System.out.println(doctor.toString());
                    //text = "Name-> " + doctor.getFullName() + "\nEmail-> " + doctor.getEmail() + "\nUsername-> " + doctor.getUserName();
                }
                //textView.setText(text);
                if(isWelcomed==false){
                    Toast.makeText(getActivity(), "Welcome "+doctor.getFullName(), Toast.LENGTH_SHORT).show();
                    isWelcomed=true;
                }
//                System.out.println("WHY");
//                System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        signOutBtn2.setOnClickListener(v -> {

            DoctorAuthRepository.getInstance(getActivity().getApplication()).getFirebaseAuth().signOut();

            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            ((DrawerLocker) getActivity()).setDrawerEnabled(false);

            getActivity().getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            Navigation.findNavController(view).navigate(R.id.action_doctorProfileFragment_to_confirmationFragment);
        });

        profileBtn.setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_doctorProfileFragment_to_aboutDoctorFragment);
        });

        myCalendarBtn.setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_doctorProfileFragment_to_doctorCalendarFragment);
        });

        BtnRequest.setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_doctorProfileFragment_to_confirmedAppointmentFragment);
        });

        listPatients.setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_doctorProfileFragment_to_myPatientsFragment);
        });

        appointmentBtn.setOnClickListener(v-> {
            Navigation.findNavController(view).navigate(R.id.action_doctorProfileFragment_to_doctorAppointmentFragment);
        });

    }

    public void showDatePickerDialog(Context wf){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                wf,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String date = "month_day_year: " + i1 + "_" + i2 + "_" + i;
        openPage(datePicker.getContext(),doc,date);
    }

    private void openPage(Context wf, String d, String day){
        Bundle bundle = new Bundle();
        bundle.putString("key1", d+"");
        bundle.putString("key2", day);
        bundle.putString("key3", "doctor");
        getParentFragmentManager().setFragmentResult("dataFromProfile", bundle);
        Navigation.findNavController(viewThis).navigate(R.id.action_doctorProfileFragment_to_appointmentFragment);
    }



}