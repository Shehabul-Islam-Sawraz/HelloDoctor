package com.exercise.thesis.hellodoc.ui.auth;

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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.model.Doctor;
import com.exercise.thesis.hellodoc.viewmodel.DoctorAuthViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SigninFragment extends Fragment {
    private DoctorAuthViewModel doctorAuthViewModel;
    private MutableLiveData<Boolean> isLoginComplete = new MutableLiveData<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        doctorAuthViewModel = new ViewModelProvider(requireActivity()).get(DoctorAuthViewModel.class);
        isLoginComplete.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean == true)
                Navigation.findNavController(view).navigate(R.id.action_signinFragment_to_doctorProfileFragment);
            //Toast.makeText(getActivity(), "ISTRUE OBSVR", Toast.LENGTH_SHORT).show();
        });

        doctorAuthViewModel.getFirebaseUserMutableLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                isLoginComplete.postValue(true);
                //Toast.makeText(getActivity(), "VIEWMODEL OBSVR", Toast.LENGTH_SHORT).show();
            }
        });

        EditText emailEditText = view.findViewById(R.id.email);
        EditText passwordEditText = view.findViewById(R.id.password);
        Button loginButton = view.findViewById(R.id.login);
        Button registerButton = view.findViewById(R.id.signInToRegister);

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
                //String error = doctorAuthViewModel.loginDataChanged(passwordEditText.getText().toString());
                if (!emailEditText.getText().toString().isEmpty() &&
                        !passwordEditText.getText().toString().isEmpty()
                        //&& error.equals("")
                    ) {
                    loginButton.setEnabled(true);
                }
                /*if (!error.equals("")) {
                    passwordEditText.setError(error);
                }*/
            }
        };
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(v -> {
            doctorAuthViewModel.signIn(emailEditText.getText().toString(), passwordEditText.getText().toString());
        });

        registerButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_signinFragment_to_signupFragment);
        });

    }
}