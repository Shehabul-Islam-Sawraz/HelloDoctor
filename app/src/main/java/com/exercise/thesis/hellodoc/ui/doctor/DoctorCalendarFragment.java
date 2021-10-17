package com.exercise.thesis.hellodoc.ui.doctor;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.adapter.MyTimeSlotAdapter;
import com.exercise.thesis.hellodoc.common.Common;
import com.exercise.thesis.hellodoc.interfaces.ITimeSlotLoadListener;
import com.exercise.thesis.hellodoc.model.AppointmentInformation;
import com.exercise.thesis.hellodoc.model.Doctor;
import com.exercise.thesis.hellodoc.viewmodel.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class DoctorCalendarFragment extends Fragment implements ITimeSlotLoadListener {

    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog alertDialog;
    RecyclerView recycler_time_slot;
    HorizontalCalendarView calendarView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference bookDateReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_calendar, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("doctor");
        this.bookDateReference = database.getReference("bookdate");
        recycler_time_slot = view.findViewById(R.id.recycle_time_slot2);
        calendarView = view.findViewById(R.id.calendarView2);
        Common.CurrentDoctor = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Common.CurrentUserType = "doctor";
        iTimeSlotLoadListener = this;
        /* alertDialog = new SpotsDialog.Builder().setCancelable(false).setContext(this)
                .build();*/
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE,0);
        loadAvailableTimeSlotOfDoctor(Common.CurrentDoctor,simpleDateFormat.format(date.getTime()));
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recycler_time_slot.setLayoutManager(gridLayoutManager);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE,5);

        /*calendarView.setMinDate(startDate.getTimeInMillis());
        calendarView.setMaxDate(endDate.getTimeInMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(startDate.getTime());
        c.set(Calendar.DAY_OF_MONTH,1);
        Calendar calen = new Calendar.Builder(view, calendarView)*/



        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(getActivity(),R.id.calendarView2)
                .range(startDate,endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if(Common.currentDate.getTimeInMillis() != date.getTimeInMillis()){
                    Common.currentDate = date;
                    loadAvailableTimeSlotOfDoctor(Common.CurrentDoctor,simpleDateFormat.format(date.getTime()));
                }
            }
        });

    }

    private void loadAvailableTimeSlotOfDoctor(String currentDoctor, String bookDate) {
        //alertDialog.show();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doctor doctor = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(Doctor.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*doctorDoc = FirebaseFirestore.getInstance()
                .collection("Doctor")
                .document(Common.CurrentDoctor);
        doctorDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        CollectionReference date =FirebaseFirestore.getInstance()
                                .collection("Doctor")
                                .document(Common.CurrentDoctor)
                                .collection(bookDate);

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot.isEmpty())
                                    {
                                        iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                                    }else {
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for (QueryDocumentSnapshot document:task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                    }

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
        });*/


        bookDateReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(bookDate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        List<TimeSlot> timeSlots = new ArrayList<>();
                        for(DataSnapshot shot:task.getResult().getChildren()){
                            timeSlots.add(shot.getValue(AppointmentInformation.class).getTimeSlot());
                        }
                    }
                    else{
                        iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
            }
        });



    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getActivity(),timeSlotList);
        recycler_time_slot.setAdapter(adapter);
        //alertDialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        //alertDialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getActivity());
        recycler_time_slot.setAdapter(adapter);
        //alertDialog.dismiss();
    }
}