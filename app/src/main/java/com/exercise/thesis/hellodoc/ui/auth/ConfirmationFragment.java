package com.exercise.thesis.hellodoc.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.exercise.thesis.hellodoc.R;

public class ConfirmationFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirmation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Button noButton = view.findViewById(R.id.no_button);
        Button yesButton = view.findViewById(R.id.yes_button);
        noButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_confirmationFragment_to_patientAuthFragment);
        });
        yesButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_signinFragment);
        });
    }

}