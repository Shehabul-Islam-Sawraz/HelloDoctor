package com.exercise.thesis.hellodoc.repository;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.exercise.thesis.hellodoc.model.Doctor;
import com.exercise.thesis.hellodoc.model.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PatientAuthRepository {
    private Application application;
    private static PatientAuthRepository authRepository;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public PatientAuthRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseUser = new MutableLiveData<>();
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("patient");
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public static PatientAuthRepository getInstance(Application application) {
        if (authRepository == null) {
            authRepository = new PatientAuthRepository(application);
        }
        return authRepository;
    }

    public void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithEmail:success");
                firebaseUser.postValue(firebaseAuth.getCurrentUser());
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                Log.w(TAG, "signInWithEmail:failure", task.getException());
            }
        });
    }
    public void register(String mblNum, String email, String fullName, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseUser.postValue(firebaseAuth.getCurrentUser());
                Patient patient = new Patient(fullName, email, mblNum);
                reference.child(email.replace(".",",")).setValue(patient);
                Toast.makeText(application, "Sign Up successful!", Toast.LENGTH_SHORT).show();
            } else {
                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(application, "User already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(application, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public MutableLiveData<FirebaseUser> getFirebaseUser() {
        return firebaseUser;
    }
}
