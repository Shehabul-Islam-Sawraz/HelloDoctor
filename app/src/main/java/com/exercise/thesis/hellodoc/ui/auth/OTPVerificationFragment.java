package com.exercise.thesis.hellodoc.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.exercise.thesis.hellodoc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPVerificationFragment extends Fragment {

    private EditText inputCode1,inputCode2,inputCode3,inputCode4,inputCode5,inputCode6;
    private TextView mobileNumber;
    private Button verifyButton;
    private String verificationId;
    private TextView resendOTP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_o_t_p_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputCode1 = view.findViewById(R.id.inputOTP1);
        inputCode2 = view.findViewById(R.id.inputOTP2);
        inputCode3 = view.findViewById(R.id.inputOTP3);
        inputCode4 = view.findViewById(R.id.inputOTP4);
        inputCode5 = view.findViewById(R.id.inputOTP5);
        inputCode6 = view.findViewById(R.id.inputOTP6);
        mobileNumber = view.findViewById(R.id.textMobile);
        verifyButton = view.findViewById(R.id.buttonVerifyOTP);
        resendOTP = view.findViewById(R.id.resendOTPText);
        mobileNumber.setText(String.format("+88%s",getArguments().getString("number")));
        verificationId = getArguments().getString("verificationId");
        setupOTPInputs();
        final ProgressBar progressBar = view.findViewById(R.id.verify_otp_progressBar);
        verifyButton.setOnClickListener(view1 -> {
            if(inputCode1.getText().toString().trim().isEmpty() ||
                    inputCode2.getText().toString().trim().isEmpty() ||
                    inputCode3.getText().toString().trim().isEmpty() ||
                    inputCode4.getText().toString().trim().isEmpty() ||
                    inputCode5.getText().toString().trim().isEmpty() ||
                    inputCode6.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Please enter valid code!", Toast.LENGTH_SHORT).show();
                return;
            }
            String code = inputCode1.getText().toString()+
                    inputCode2.getText().toString()+
                    inputCode3.getText().toString()+
                    inputCode4.getText().toString()+
                    inputCode5.getText().toString()+
                    inputCode6.getText().toString();
            if(verificationId!=null){
                progressBar.setVisibility(View.VISIBLE);
                verifyButton.setVisibility(View.INVISIBLE);
                PhoneAuthCredential phoneAuthCredential  = PhoneAuthProvider.getCredential(
                        verificationId,code);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).
                        addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            verifyButton.setVisibility(View.VISIBLE);
                            if(task.isSuccessful()){
                                Navigation.findNavController(view).navigate(R.id.action_OTPVerificationFragment_to_homepageFragment);
                            }
                            else{
                                Toast.makeText(getContext(), "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        resendOTP.setOnClickListener(view12 -> {
            PhoneAuthProvider.verifyPhoneNumber(
                    PhoneAuthOptions.
                            newBuilder(FirebaseAuth.getInstance()).
                            setPhoneNumber("+88"+getArguments().getString("number")).
                            setTimeout(60L, TimeUnit.SECONDS).
                            setActivity(getActivity()).
                            setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    verificationId = newVerificationId;
                                    Toast.makeText(getContext(), "OTP Sent", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build());
        });
    }

    private void setupOTPInputs(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}