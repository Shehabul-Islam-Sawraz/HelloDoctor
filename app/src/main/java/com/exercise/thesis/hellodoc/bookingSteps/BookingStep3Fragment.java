package com.exercise.thesis.hellodoc.bookingSteps;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.common.Common;
import com.exercise.thesis.hellodoc.model.AppointmentInformation;
import com.exercise.thesis.hellodoc.notification.ReminderBroadcast;
import com.exercise.thesis.hellodoc.viewmodel.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class BookingStep3Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    static BookingStep3Fragment instance;
    TextView txt_booking_berber_text;
    TextView txt_booking_time_text;
    TextView txt_booking_type;
    TextView txt_booking_phone;
    Button confirmBtn;
    private FirebaseDatabase database;
    private DatabaseReference bookDateReference;
    private DatabaseReference appointmentReference;
    private DatabaseReference patientAppointmentReference;
    private Context thisContext;
    private ProgressDialog progressDialog;

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TAG", "onReceive: heave been receiver" );
            setData();
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());

        localBroadcastManager.registerReceiver(confirmBookingReceiver,new IntentFilter(Common.KEY_CONFIRM_BOOKING));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.thisContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.fragment_booking_step3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.txt_booking_berber_text = view.findViewById(R.id.txt_booking_berber_text);
        this.txt_booking_time_text = view.findViewById(R.id.txt_booking_time_text);
        this.txt_booking_type = view.findViewById(R.id.txt_booking_type);
        this.txt_booking_phone = view.findViewById(R.id.txt_booking_phone);
        this.confirmBtn = view.findViewById(R.id.btn_confirm);
        this.database = FirebaseDatabase.getInstance();
        this.bookDateReference = database.getReference("bookdate");
        this.appointmentReference = database.getReference("AppointmentRequest");
        this.patientAppointmentReference = database.getReference("PatientAppointment");
        progressDialog = new ProgressDialog(thisContext);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Requesting your appointment!!");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAppointment();
            }
        });
    }

    void confirmAppointment(){
        AppointmentInformation appointmentInformation = new AppointmentInformation();
        appointmentInformation.setAppointmentType(Common.CurrentAppointmentType);
        appointmentInformation.setDoctorId(Common.CurrentDoctor);
        appointmentInformation.setDoctorName(Common.CurrentDoctorName);
        appointmentInformation.setPatientName(Common.CurrentUserName);
        appointmentInformation.setPatientId(Common.CurrentUserid);
        appointmentInformation.setChain("bookdate/"+Common.CurrentDoctor.replace(".",",")+"/"+Common.simpleFormat.format(Common.currentDate.getTime())+"/"+String.valueOf(Common.currentTimeSlot));
        appointmentInformation.setType("Checked");
        appointmentInformation.setIsChargeApplicable("No");
        appointmentInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(Common.currentDate.getTime())).toString());
        appointmentInformation.setSlot(Long.valueOf(Common.currentTimeSlot));



        TimeSlot slot = new TimeSlot();
        slot.setSlot((long)Common.currentTimeSlot);
        slot.setType("full");
        slot.setChain("bookdate/"+Common.CurrentDoctor.replace(".",",")+"/"+Common.simpleFormat.format(Common.currentDate.getTime())+"/"+String.valueOf(Common.currentTimeSlot));
        appointmentInformation.setTimeSlot(slot);

        bookDateReference.child(Common.CurrentDoctor.replace(".",",")).child(Common.simpleFormat.format(Common.currentDate.getTime())).
                child(String.valueOf(Common.currentTimeSlot)).setValue(appointmentInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                getActivity().finish();
                Toast.makeText(getContext(),"Success!",Toast.LENGTH_SHORT).show();
                Common.currentTimeSlot = -1;
                Common.currentDate = Calendar.getInstance();
                Common.step = 0;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                appointmentReference.child(Common.CurrentDoctor.replace(".",",")).
                        child(appointmentInformation.getTime().replace("/","_")).setValue(appointmentInformation).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                patientAppointmentReference.child(appointmentInformation.getPatientId().replace(".",",")).
                                        child("calendar").child(appointmentInformation.getTime().replace("/","_"))
                                        .setValue(appointmentInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        callAlarm(appointmentInformation);
                                        progressDialog.hide();
                                    }
                                });
                            }
                        });

            }
        });
    }

    private void callAlarm(AppointmentInformation appointmentInformation) {
        Intent intent = new Intent(thisContext, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(thisContext,0 , intent, 0);
        AlarmManager alarmManager = (AlarmManager) thisContext.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(System.currentTimeMillis());
        String[] time = appointmentInformation.getTime().split(" ");
        String[] hour = time[0].split("-");
        String[] hourMin = hour[0].split(":");
        int hourNow = Integer.parseInt(hourMin[0]);
        int minNow = Integer.parseInt(hourMin[1]);
        if(minNow==0){
            calendar.set(Calendar.HOUR_OF_DAY, hourNow-1);
            calendar.set(Calendar.MINUTE,45);
            calendar.set(Calendar.SECOND,1);
        }
        else{
            calendar.set(Calendar.HOUR_OF_DAY, hourNow);
            calendar.set(Calendar.MINUTE,minNow-15);
            calendar.set(Calendar.SECOND,1);
        }
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(1000*10), pendingIntent);// for testing notification after 10 seconds
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "AppointmentNotificationChannel";
            String description = "Next Appointment Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyMenu", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = thisContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public BookingStep3Fragment() {
        // Required empty public constructor
    }

    public static BookingStep3Fragment newInstance(String param1, String param2) {
        BookingStep3Fragment fragment = new BookingStep3Fragment();
        return fragment;
    }

    public  static  BookingStep3Fragment getInstance(){
        if(instance == null )
            instance = new BookingStep3Fragment();
        return instance;
    }

    private void setData() {
        txt_booking_berber_text.setText(Common.CurrentDoctorName);
        txt_booking_time_text.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(Common.currentDate.getTime())));
        txt_booking_phone.setText(Common.CurrentPhone);
        txt_booking_type.setText(Common.CurrentAppointmentType);
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }
}
