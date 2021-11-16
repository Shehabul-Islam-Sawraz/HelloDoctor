package com.exercise.thesis.hellodoc.bookingSteps;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.adapter.MyTimeSlotAdapter;
import com.exercise.thesis.hellodoc.common.Common;
import com.exercise.thesis.hellodoc.interfaces.ITimeSlotLoadListener;
import com.exercise.thesis.hellodoc.model.AppointmentInformation;
import com.exercise.thesis.hellodoc.viewmodel.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class BookingStep2Fragment extends Fragment implements ITimeSlotLoadListener {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference("bookdate");
    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog dialog;
    LocalBroadcastManager localBroadcastManager;
    RecyclerView recycler_time_slot;
    HorizontalCalendarView calendarView;
    SimpleDateFormat simpleDateFormat;
    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE,0);
            loadAvailableTimeSlotOfDoctor(Common.CurrentDoctor,simpleDateFormat.format(date.getTime()));

        }
    };

    private void loadAvailableTimeSlotOfDoctor(String doctorId, final String bookDate) {
        dialog.show();

        reference.child(Common.CurrentDoctor.replace(".",",")).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    reference.child(Common.CurrentDoctor.replace(".",",")).child(bookDate).get().
                            addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DataSnapshot snapshot = task.getResult();
                                        if(!snapshot.hasChildren()){
                                            iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                                        }
                                        else{
                                            List<TimeSlot> timeSlots = new ArrayList<>();
                                            for(DataSnapshot shot: snapshot.getChildren()){
                                                AppointmentInformation information = shot.getValue(AppointmentInformation.class);
                                                timeSlots.add(information.getTimeSlot());
                                            }
                                            iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                        }
                                    }
                                }
                            }).
                            addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                                }
                            });
                }
            }
        });
    }

    static BookingStep2Fragment instance;
    public  static  BookingStep2Fragment getInstance(){
        if(instance == null )
            instance = new BookingStep2Fragment();
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iTimeSlotLoadListener = this;
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot,new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));
        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         //super.onCreateView(inflater, container, savedInstanceState);
         return inflater.inflate(R.layout.fragment_booking_step_two,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.recycler_time_slot = view.findViewById(R.id.recycle_time_slot);
        this.calendarView = view.findViewById(R.id.calendarView);
        init(view);
        loadAvailableTimeSlotOfDoctor(Common.CurrentDoctor,simpleDateFormat.format(Common.currentDate.getTime()));
    }

    private void init(View itemView) {
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recycler_time_slot.setLayoutManager(gridLayoutManager);
        //recycler_time_slot.addItemDecoration(new SpaceI);
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE,5);
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemView,R.id.calendarView)
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

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext(),timeSlotList);
        recycler_time_slot.setAdapter(adapter);
        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(adapter);
        dialog.dismiss();
    }
}
