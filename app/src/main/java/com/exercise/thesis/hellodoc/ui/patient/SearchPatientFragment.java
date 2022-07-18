package com.exercise.thesis.hellodoc.ui.patient;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.adapter.DoctorAdapterFiltred;
import com.exercise.thesis.hellodoc.model.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SearchPatientFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private View viewThis;
    private List<Doctor> doctorsList = new ArrayList<>();
    private DoctorAdapterFiltred adapter;
    public String doctorType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_patient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("doctor");
        this.viewThis = view;
        getParentFragmentManager().setFragmentResultListener("DoctorCategorySearch", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                doctorType = result.getString("doc_type");
                System.out.println("Doctor type is: "+doctorType);
            }
        });
        configureToolbar();
        setUpRecyclerView();
    }

    private void configureToolbar(){
        // Get the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) viewThis.findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("Doctors List");
        // Sets the Toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = viewThis.findViewById(R.id.searchPatRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //Query query = doctorRef.orderBy("name", Query.Direction.DESCENDING);
        Query query = reference.orderByChild("fullName");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("Snapshot is: "+snapshot);
                if(snapshot.exists()){
                    doctorsList.clear();
                    for(DataSnapshot shot: snapshot.getChildren()){
                        Doctor d = shot.getValue(Doctor.class);
                        if(d.getSpecialities().equals(doctorType) && !d.getFees().equals("")) {
                            doctorsList.add(d);
                        }
                    }
                    //Sorting doctors based on ratings
                    if(doctorsList.size()>1){
                        Collections.sort(doctorsList, new Comparator<Doctor>() {
                            @Override
                            public int compare(Doctor d1, Doctor d2) {
                                return d2.getAvgRating().compareTo(d1.getAvgRating());
                            }
                        });
                    }
                    adapter = new DoctorAdapterFiltred(doctorsList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, ((AppCompatActivity)getActivity()).getMenuInflater());
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.navigation_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        Drawable r= getResources().getDrawable(R.drawable.ic_local_hospital_black_24dp);
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" Specialities" );
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //menu.findItem(R.id.empty).setTitle(sb);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint(Html.fromHtml("<font color = #000000>" + "Search patient" + "</font>"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                DoctorAdapterFiltred.specialistSearch = false;
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //3 - Handle actions on menu items
        switch (item.getItemId()) {
            case R.id.option_all:
                DoctorAdapterFiltred.specialistSearch = true;
                adapter.getFilter().filter("");
                return true;
            case R.id.option_general:
                DoctorAdapterFiltred.specialistSearch = true;
                adapter.getFilter().filter("Medicine General");
                return true;
            case R.id.option_Dentist:
                DoctorAdapterFiltred.specialistSearch = true;
                adapter.getFilter().filter("Dentist");
                return true;
            case R.id.option_Ophthalmology:
                DoctorAdapterFiltred.specialistSearch = true;
                adapter.getFilter().filter("Ophthalmologist");
                return true;
            case R.id.option_ORL:
                DoctorAdapterFiltred.specialistSearch = true;
                adapter.getFilter().filter("ORL");
                return true;
            case R.id.option_Pediatric:
                DoctorAdapterFiltred.specialistSearch = true;
                adapter.getFilter().filter("Pediatrician");
                return true;
            case R.id.option_Radiology:
                DoctorAdapterFiltred.specialistSearch = true;
                adapter.getFilter().filter("Radiologist");
                return true;
            case R.id.option_Rheumatology:
                DoctorAdapterFiltred.specialistSearch = true;
                adapter.getFilter().filter("Rheumatologist");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}