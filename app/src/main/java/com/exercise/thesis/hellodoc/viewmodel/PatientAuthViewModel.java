package com.exercise.thesis.hellodoc.viewmodel;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.exercise.thesis.hellodoc.repository.PatientAuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class PatientAuthViewModel extends AndroidViewModel {

    private PatientAuthRepository authRepository;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;

    public PatientAuthViewModel(@NonNull Application application) {
        super(application);
        this.authRepository = authRepository.getInstance(application);
        firebaseUserMutableLiveData = authRepository.getFirebaseUser();
    }

    public PatientAuthRepository getLoginRepository() {
        return authRepository;
    }


    public void register(String mblNum, String email, String fullName, String password) {
        authRepository.register(mblNum, email, fullName, password);
    }

    public void signIn(String email, String password) {
        authRepository.signIn(email, password);
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public String checkPassword(String password) {
        if (!isPasswordValid(password)) {
            return "invalid_password";
        }
        return "";
    }
    public boolean checkPasswordAndConfirmPassword(String password,String confirmPassword) {
        return password.equals(confirmPassword);
    }

    // A placeholder username validation check
    public boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    public boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= 8;
    }
}
