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


public class AllCategories extends Fragment {

    private CardView Anesthesiologist,Cardiologist,Dentist,Dermatologist,ENTSpecialist,Epidemiologist,GeneralPractitioner,Rheumatologist,Veterinarian;
    private CardView Gynecologist,Neurologist,Optometrist,Pediatrician,PlasticSurgeon,Psychiatrist,Pulmonologist,Radiologist,Surgeon,Urologist;

    public AllCategories() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Anesthesiologist = view.findViewById(R.id.Anesthesiologist);
        Anesthesiologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Cardiologist = view.findViewById(R.id.Cardiologist);
        Cardiologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Dentist = view.findViewById(R.id.Dentist);
        Dentist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Dermatologist = view.findViewById(R.id.Dermatologist);
        Dermatologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        ENTSpecialist = view.findViewById(R.id.ENTSpecialist);
        ENTSpecialist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Epidemiologist = view.findViewById(R.id.Epidemiologist);
        Epidemiologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        GeneralPractitioner = view.findViewById(R.id.GeneralPractitioner);
        GeneralPractitioner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Rheumatologist = view.findViewById(R.id.Rheumatologist);
        Rheumatologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Veterinarian = view.findViewById(R.id.Veterinarian);
        Veterinarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Gynecologist = view.findViewById(R.id.Gynecologist);
        Gynecologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Neurologist = view.findViewById(R.id.Neurologist);
        Neurologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Optometrist = view.findViewById(R.id.Optometrist);
        Optometrist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Pediatrician = view.findViewById(R.id.Pediatrician);
        Pediatrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        PlasticSurgeon = view.findViewById(R.id.PlasticSurgeon);
        PlasticSurgeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Psychiatrist = view.findViewById(R.id.Psychiatrist);
        Psychiatrist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Pulmonologist = view.findViewById(R.id.Pulmonologist);
        Pulmonologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Radiologist = view.findViewById(R.id.Radiologist);
        Radiologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Surgeon = view.findViewById(R.id.Surgeon);
        Surgeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
        Urologist = view.findViewById(R.id.Urologist);
        Urologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler(view);
            }
        });
    }

    private void clickHandler(View view) {
        Bundle bundle;
        switch (view.getId()){
            case R.id.Anesthesiologist:
                bundle = new Bundle();
                bundle.putString("doc_type", "Anesthesiologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Cardiologist:
                bundle = new Bundle();
                bundle.putString("doc_type", "Cardiologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Dentist:
                bundle = new Bundle();
                bundle.putString("doc_type", "Dentist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Dermatologist:
                bundle = new Bundle();
                bundle.putString("doc_type", "Dermatologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.ENTSpecialist:
                bundle = new Bundle();
                bundle.putString("doc_type", "ENT Specialist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Epidemiologist:
                bundle = new Bundle();
                bundle.putString("doc_type", "Epidemiologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.GeneralPractitioner:
                bundle = new Bundle();
                bundle.putString("doc_type", "General Practitioner");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Rheumatologist:
                bundle = new Bundle();
                bundle.putString("doc_type", "Rheumatologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Veterinarian:
                bundle = new Bundle();
                bundle.putString("doc_type", "Veterinarian");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Gynecologist:
                bundle = new Bundle();
                bundle.putString("doc_type", "Gynecologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Neurologist:
                bundle = new Bundle();
                bundle.putString("doc_type", "Neurologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Optometrist:
                bundle = new Bundle();
                bundle.putString("doc_type", "Optometrist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Pediatrician:
                bundle = new Bundle();
                bundle.putString("doc_type", "Pediatrician");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.PlasticSurgeon:
                bundle = new Bundle();
                bundle.putString("doc_type", "Plastic Surgeon");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Psychiatrist:
                bundle = new Bundle();
                bundle.putString("doc_type", "Psychiatrist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Pulmonologist:
                bundle = new Bundle();
                bundle.putString("doc_type", "Pulmonologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Radiologist:
                bundle = new Bundle();
                bundle.putString("doc_type", "Radiologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Surgeon:
                bundle = new Bundle();
                bundle.putString("doc_type", "Surgeon");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
            case R.id.Urologist:
                bundle = new Bundle();
                bundle.putString("doc_type", "Urologist");
                getParentFragmentManager().setFragmentResult("DoctorCategorySearch", bundle);
                Navigation.findNavController(view).navigate(R.id.action_allCategories_to_searchPatientFragment);
                break;
        }
    }


}