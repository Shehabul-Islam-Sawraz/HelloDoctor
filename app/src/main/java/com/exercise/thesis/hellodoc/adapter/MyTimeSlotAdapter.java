package com.exercise.thesis.hellodoc.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.common.Common;
import com.exercise.thesis.hellodoc.interfaces.IRecyclerItemSelectedListener;
import com.exercise.thesis.hellodoc.model.AppointmentInformation;
import com.exercise.thesis.hellodoc.viewmodel.TimeSlot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {


    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;
    SimpleDateFormat simpleDateFormat;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference bookDateReference;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("doctor");
        this.bookDateReference = database.getReference("bookdate");
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("doctor");
        this.bookDateReference = database.getReference("bookdate");
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot,parent,false);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(position)).toString());
        if(timeSlotList.size()==0){
            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
            holder.txt_time_slot_description.setText("Available");
            holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));
        }
        else{
            for (TimeSlot slotValue:timeSlotList){
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if(slot == position){

                    holder.card_time_slot.setTag(Common.DISABLE_TAG);
                    holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));

                    holder.txt_time_slot_description.setText("Full");
                    if(slotValue.getType().equals("Checked"))
                        holder.txt_time_slot_description.setText("Chosen");
                    holder.txt_time_slot_description.setTextColor(context.getResources()
                            .getColor(android.R.color.white));
                    holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white));

                }
            }

        }
        if (!cardViewList.contains(holder.card_time_slot))
            cardViewList.add(holder.card_time_slot);

        holder.setRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                for(CardView cardView:cardViewList) {
                    if (cardView.getTag() == null)
                        cardView.setCardBackgroundColor(context.getResources()
                                .getColor(android.R.color.white));
                }
                holder.card_time_slot.setCardBackgroundColor(context.getResources()
                        .getColor(android.R.color.holo_orange_dark));

                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_TIME_SLOT,position);
                Common.currentTimeSlot = position ;
                intent.putExtra(Common.KEY_STEP,2);
                Log.e("pos ", "onItemSelectedListener: "+position );
                localBroadcastManager.sendBroadcast(intent);
                if(Common.CurrentUserType == "doctor" && holder.txt_time_slot_description.getText().equals("Available")){
                    AlertDialog.Builder alert = new AlertDialog.Builder(holder.card_time_slot.getContext());
                    alert.setTitle("Block");
                    alert.setMessage("Are you sure you want to block?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppointmentInformation appointmentInformation = new AppointmentInformation();
                            appointmentInformation.setAppointmentType(Common.CurrentAppointmentType);
                            appointmentInformation.setDoctorId(Common.CurrentDoctor);
                            appointmentInformation.setDoctorName(Common.CurrentDoctorName);
                            appointmentInformation.setChain("bookdate/"+Common.CurrentDoctor+"/"+Common.simpleFormat.format(Common.currentDate.getTime())+"/"+String.valueOf(Common.currentTimeSlot));
                            appointmentInformation.setType("full");
                            appointmentInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                                    .append("at")
                                    .append(simpleDateFormat.format(Common.currentDate.getTime())).toString());
                            appointmentInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

                            TimeSlot slot = new TimeSlot();
                            slot.setSlot((long)Common.currentTimeSlot);
                            slot.setType("full");
                            slot.setChain("Doctor/"+Common.CurrentDoctor+"/"+Common.simpleFormat.format(Common.currentDate.getTime())+"/"+String.valueOf(Common.currentTimeSlot));
                            appointmentInformation.setTimeSlot(slot);

                            bookDateReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Common.simpleFormat.format(Common.currentDate.getTime())).
                                    child(String.valueOf(Common.currentTimeSlot)).setValue(appointmentInformation);

                            dialog.dismiss();
                        }
                    });

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot,txt_time_slot_description;
        CardView card_time_slot;
        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = (CardView)itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView)itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description = (TextView)itemView.findViewById(R.id.txt_time_slot_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v,getBindingAdapterPosition());
        }
    }
}