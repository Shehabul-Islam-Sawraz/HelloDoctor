package com.exercise.thesis.hellodoc.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.exercise.thesis.hellodoc.model.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class DoctorAuthRepository {
    private Application application;
    private static DoctorAuthRepository authRepository;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public DoctorAuthRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseUser = new MutableLiveData<>();
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("doctor");
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public static DoctorAuthRepository getInstance(Application application) {
        if (authRepository == null) {
            authRepository = new DoctorAuthRepository(application);
        }
        return authRepository;
    }

    public void signIn(String email, String password) {
        ValueEventListener findAcc = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(searchAccount(snapshot,email)){
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        //Toast.makeText(application, firebaseAuth.getCurrentUser().toString(), Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            firebaseUser.postValue(firebaseAuth.getCurrentUser());
                        }else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }
                    });
                }
                else {
                    Toast.makeText(application, "No account exists with this email!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        reference.addListenerForSingleValueEvent(findAcc);

    }

    private boolean searchAccount(DataSnapshot snapshot, String email) {
        if(snapshot.exists()){
            for(DataSnapshot ds:snapshot.getChildren()){
                if(ds.child("email").getValue(String.class).equals(email)){
                    return true;
                }
            }
        }
        return false;
    }

    public void register(String email, String fullName, String password, String address) {
        ValueEventListener findAcc = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(searchAccount(snapshot,email)){
                    Toast.makeText(application, "User already exists!", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            firebaseUser.postValue(firebaseAuth.getCurrentUser());
                            Doctor doctor = new Doctor(fullName, email, address,firebaseAuth.getCurrentUser().getPhotoUrl());
                            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(doctor);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        reference.addListenerForSingleValueEvent(findAcc);

    }

    public MutableLiveData<FirebaseUser> getFirebaseUser() {
        return firebaseUser;
    }
}
