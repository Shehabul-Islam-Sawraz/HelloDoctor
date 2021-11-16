package com.exercise.thesis.hellodoc.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.viewmodel.DoctorAuthViewModel;
import com.exercise.thesis.hellodoc.viewmodel.PatientAuthViewModel;


public class PatientSigninFragment extends Fragment {

    private PatientAuthViewModel patientAuthViewModel;
    private MutableLiveData<Boolean> isLoginComplete = new MutableLiveData<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_signin, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText emailEditText = view.findViewById(R.id.patient_email);
        EditText passwordEditText = view.findViewById(R.id.patient_password);
        Button loginButton = view.findViewById(R.id.patient_login);
        Button registerButton = view.findViewById(R.id.patientsignInToRegister);

        patientAuthViewModel = new ViewModelProvider(requireActivity()).get(PatientAuthViewModel.class);
        isLoginComplete.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean == true)
                Navigation.findNavController(view).navigate(R.id.action_patientSigninFragment_to_homepageFragment);
        });

        patientAuthViewModel.getFirebaseUserMutableLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                isLoginComplete.postValue(true);
            }
        });

        loginButton.setEnabled(false);

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!emailEditText.getText().toString().isEmpty() &&
                        !passwordEditText.getText().toString().isEmpty()
                ) {
                    loginButton.setEnabled(true);
                }
            }
        };
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(v -> {
            patientAuthViewModel.signIn(emailEditText.getText().toString(), passwordEditText.getText().toString());
        });

        registerButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_patientSigninFragment_to_patientSignupFragment);
        });

    }
}