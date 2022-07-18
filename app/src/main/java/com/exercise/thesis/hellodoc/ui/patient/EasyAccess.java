package com.exercise.thesis.hellodoc.ui.patient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exercise.thesis.hellodoc.R;

public class EasyAccess extends Fragment {

    private CardView BoneFracture,ColdFeverFlu,DepressionAnxiety,Diabetes,Diarrhea,EyeItching,FoodPoisoning,Headache;
    private CardView HeartAttack,Migraine,NoseBleed,SkinAllergy,SoreThroat,StomachPain,Sunburn,Toothache;

    public EasyAccess(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_easy_access, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BoneFracture = view.findViewById(R.id.BoneFracture);
        BoneFracture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        ColdFeverFlu = view.findViewById(R.id.ColdFeverFlu);
        ColdFeverFlu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        DepressionAnxiety = view.findViewById(R.id.DepressionAnxiety);
        DepressionAnxiety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Diabetes = view.findViewById(R.id.Diabetes);
        Diabetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Diarrhea = view.findViewById(R.id.Diarrhea);
        Diarrhea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        EyeItching = view.findViewById(R.id.EyeItching);
        EyeItching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        FoodPoisoning = view.findViewById(R.id.FoodPoisoning);
        FoodPoisoning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Headache = view.findViewById(R.id.Headache);
        Headache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        HeartAttack = view.findViewById(R.id.HeartAttack);
        HeartAttack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Migraine = view.findViewById(R.id.Migraine);
        Migraine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        NoseBleed = view.findViewById(R.id.NoseBleed);
        NoseBleed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        SkinAllergy = view.findViewById(R.id.SkinAllergy);
        SkinAllergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        SoreThroat = view.findViewById(R.id.SoreThroat);
        SoreThroat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        StomachPain = view.findViewById(R.id.StomachPain);
        StomachPain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Sunburn = view.findViewById(R.id.Sunburn);
        Sunburn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Toothache = view.findViewById(R.id.Toothache);
        Toothache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
    }

    private void clickHandler(View view) {
        Bundle bundle;
        switch (view.getId()){
            case R.id.BoneFracture:
                bundle = new Bundle();
                bundle.putString("doc_type", "Rheumatologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.ColdFeverFlu:
                bundle = new Bundle();
                bundle.putString("doc_type", "General Practitioner");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.DepressionAnxiety:
                bundle = new Bundle();
                bundle.putString("doc_type", "Psychiatrist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.Diabetes:
                bundle = new Bundle();
                bundle.putString("doc_type", "Endocrinologists");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.Diarrhea:
                bundle = new Bundle();
                bundle.putString("doc_type", "Gastroenterologists");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.EyeItching:
                bundle = new Bundle();
                bundle.putString("doc_type", "Optometrist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.FoodPoisoning:
                bundle = new Bundle();
                bundle.putString("doc_type", "General Practitioner");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.Headache:
                bundle = new Bundle();
                bundle.putString("doc_type", "General Practitioner");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.HeartAttack:
                bundle = new Bundle();
                bundle.putString("doc_type", "Cardiologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.Migraine:
                bundle = new Bundle();
                bundle.putString("doc_type", "Neurologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.NoseBleed:
                bundle = new Bundle();
                bundle.putString("doc_type", "ENT Specialist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.SkinAllergy:
                bundle = new Bundle();
                bundle.putString("doc_type", "Dermatologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.SoreThroat:
                bundle = new Bundle();
                bundle.putString("doc_type", "ENT Specialist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.StomachPain:
                bundle = new Bundle();
                bundle.putString("doc_type", "Gastroenterologists");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.Sunburn:
                bundle = new Bundle();
                bundle.putString("doc_type", "Dermatologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
            case R.id.Toothache:
                bundle = new Bundle();
                bundle.putString("doc_type", "Dentist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_easyAccess2_to_searchPatientFragment);
                break;
        }
    }
}