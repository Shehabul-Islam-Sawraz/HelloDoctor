package com.exercise.thesis.hellodoc.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.viewmodel.DoctorAuthViewModel;
import com.exercise.thesis.hellodoc.viewmodel.PatientAuthViewModel;


public class PatientSignupFragment extends Fragment {

    private PatientAuthViewModel authViewModel;

    Button registerButton;
    Button returnToLogin;
    EditText fullName;
    EditText password;
    EditText confirmPassword;
    EditText email;
    String mblNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_signup, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerButton = view.findViewById(R.id.patient_register);
        returnToLogin = view.findViewById(R.id.patient_returnToLogin);
        fullName = view.findViewById(R.id.patient_fullName);
        password = view.findViewById(R.id.patient_signup_password);
        confirmPassword = view.findViewById(R.id.patient_signup_confirm_password);
        email = view.findViewById(R.id.patient_signup_email);
        authViewModel = new ViewModelProvider(requireActivity()).get(PatientAuthViewModel.class);

        registerButton.setEnabled(false);

        authViewModel.getFirebaseUserMutableLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                Navigation.findNavController(view).navigate(R.id.action_patientSignupFragment_to_homepageFragment);
            }
        });

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
                String error = authViewModel.checkPassword(password.getText().toString());
                if (!fullName.getText().toString().isEmpty() &&
                        !email.getText().toString().isEmpty() &&
                        error.equals("")) {
                    registerButton.setEnabled(true);
                }
                if (!error.equals("")) {
                    password.setError("Password must contain at least 8 characters");
                }
            }
        };

        password.addTextChangedListener(afterTextChangedListener);

        registerButton.setOnClickListener(v -> {
            boolean match = authViewModel.checkPasswordAndConfirmPassword(password.getText().toString(),confirmPassword.getText().toString());
            if (!match) {
                Toast.makeText(getContext(), "Sign Up Failed! Please fill all the fields properly.", Toast.LENGTH_SHORT).show();
                password.setText("");
                confirmPassword.setText("");
                registerButton.setEnabled(false);
            }
            else{
                getParentFragmentManager().setFragmentResultListener("OTPVerify", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        mblNum = result.getString("mbl_num");
                    }
                });
                authViewModel.register(
                        mblNum,
                        email.getText().toString(),
                        fullName.getText().toString(),
                        password.getText().toString());
            }
        });

        returnToLogin.setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });
    }
}