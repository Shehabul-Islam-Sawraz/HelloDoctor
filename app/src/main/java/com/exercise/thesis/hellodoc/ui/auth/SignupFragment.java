package com.exercise.thesis.hellodoc.ui.auth;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.viewmodel.DoctorAuthViewModel;


public class SignupFragment extends Fragment {

    private DoctorAuthViewModel authViewModel;

    Button registerButton;
    Button returnToLogin;
    EditText fullName;
    EditText username;
    EditText password;
    EditText confirmPassword;
    EditText email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerButton = view.findViewById(R.id.register);
        returnToLogin = view.findViewById(R.id.returnToLogin);
        fullName = view.findViewById(R.id.fullName);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.signup_password);
        confirmPassword = view.findViewById(R.id.signup_confirm_password);
        email = view.findViewById(R.id.signup_email);
        authViewModel = new ViewModelProvider(requireActivity()).get(DoctorAuthViewModel.class);

        registerButton.setEnabled(false);

        authViewModel.getFirebaseUserMutableLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                //Toast.makeText(getActivity(), "YEEEETTT", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_doctorProfileFragment);
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
                        !username.getText().toString().isEmpty() &&
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
                authViewModel.register(
                        email.getText().toString(),
                        fullName.getText().toString(),
                        username.getText().toString(),
                        password.getText().toString());
            }
        });

        returnToLogin.setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });
    }
}