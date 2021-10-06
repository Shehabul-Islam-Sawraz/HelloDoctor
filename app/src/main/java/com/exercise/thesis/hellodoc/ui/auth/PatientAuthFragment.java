package com.exercise.thesis.hellodoc.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.exercise.thesis.hellodoc.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PatientAuthFragment extends Fragment {

    private Button sendOTP;
    private EditText mbl_number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mbl_number = view.findViewById(R.id.patient_mobile_number);
        sendOTP = view.findViewById(R.id.send_otp);
        final ProgressBar progressBar = view.findViewById(R.id.send_otp_progressBar);
        sendOTP.setOnClickListener(v -> {
            if(mbl_number.getText().toString().trim().isEmpty()){
                Toast.makeText(getContext(), "Enter your mobile number", Toast.LENGTH_SHORT).show();
                return;
            }
            sendOTP.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            PhoneAuthProvider.verifyPhoneNumber(
                    PhoneAuthOptions.
                            newBuilder(FirebaseAuth.getInstance()).
                            setPhoneNumber("+88"+mbl_number.getText().toString()).
                            setTimeout(60L, TimeUnit.SECONDS).
                            setActivity(getActivity()).
                            setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressBar.setVisibility(View.GONE);
                                    sendOTP.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressBar.setVisibility(View.GONE);
                                    sendOTP.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(verificationId, forceResendingToken);
                                    progressBar.setVisibility(View.GONE);
                                    sendOTP.setVisibility(View.VISIBLE);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("number",mbl_number.getText().toString());
                                    bundle.putString("verificationId",verificationId);
                                    Navigation.findNavController(view).navigate(R.id.action_patientAuthFragment_to_OTPVerificationFragment,bundle);
                                }
                            })
                    .build());
            });
    }
}