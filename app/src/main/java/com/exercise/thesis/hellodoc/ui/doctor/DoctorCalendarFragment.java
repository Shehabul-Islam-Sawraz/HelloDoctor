package com.exercise.thesis.hellodoc.ui.doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.interfaces.ITimeSlotLoadListener;
import com.exercise.thesis.hellodoc.viewmodel.TimeSlot;

import java.util.List;


public class DoctorCalendarFragment extends Fragment implements ITimeSlotLoadListener {

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {

    }

    @Override
    public void onTimeSlotLoadFailed(String message) {

    }

    @Override
    public void onTimeSlotLoadEmpty() {

    }
}