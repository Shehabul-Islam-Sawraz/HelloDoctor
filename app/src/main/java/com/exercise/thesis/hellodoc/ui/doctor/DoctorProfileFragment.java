package com.exercise.thesis.hellodoc.ui.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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

public class DoctorProfileFragment extends Fragment{
    private FirebaseDatabase database;
    private DatabaseReference reference;

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
        Common.CurrentDoctor = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
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
                Doctor doctor = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(Doctor.class);
                //String text = "Yo";
                if (doctor != null) {
                    System.out.println(doctor.toString());
                    //text = "Name-> " + doctor.getFullName() + "\nEmail-> " + doctor.getEmail() + "\nUsername-> " + doctor.getUserName();
                }
                //textView.setText(text);
                Toast.makeText(getActivity(), "Welcome "+doctor.getFullName(), Toast.LENGTH_SHORT).show();
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

            Navigation.findNavController(view).navigate(R.id.action_doctorProfileFragment_to_welcomeFragment);
        });

        profileBtn.setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_doctorProfileFragment_to_aboutDoctorFragment);
        });

        myCalendarBtn.setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_doctorProfileFragment_to_aboutDoctorFragment);
        });

    }







/*  Unbinder unbinder;

    @OnClick(R.id.myCalendarBtn)
    void myCalendarOnclick() {
        Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show();
        Intent k = new Intent(DoctorHomeActivity.this, MyCalendarDoctorActivity.class);
        startActivity(k);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home); //ici layout de page d'acceuil MEDECIN
        unbinder = ButterKnife.bind(this,this);
        Common.CurreentDoctor = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        Common.CurrentUserType = "doctor";
        listPatients = findViewById(R.id.listPatients);
        BtnRequst=findViewById(R.id.btnRequst);
        SignOutBtn2=findViewById(R.id.signOutBtn);
        appointementBtn = findViewById(R.id.appointement);
        SignOutBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        BtnRequst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(DoctorHomeActivity.this, ConfirmedAppointmensActivity.class);
                startActivity(k);
            }
        });
        listPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(DoctorHomeActivity.this, MyPatientsActivity.class);
                startActivity(k);
            }
        });
        appointementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // doc = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                //showDatePickerDialog(v.getContext());
                Intent k = new Intent(DoctorHomeActivity.this, DoctorAppointementActivity.class);
                startActivity(k);
            }
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = "month_day_year: " + month + "_" + dayOfMonth + "_" + year;
        openPage(view.getContext(),doc,date);
    }

    private void openPage(Context wf, String d,String day){
        Intent i = new Intent(wf, AppointementActivity.class);
        i.putExtra("key1",d+"");
        i.putExtra("key2",day);
        i.putExtra("key3","doctor");
        wf.startActivity(i);
    }
*/


}