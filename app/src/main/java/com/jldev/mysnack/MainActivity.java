package com.jldev.mysnack;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SEE ME";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart(){
        super.onStart();
        //Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // Sign in succes, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:succes");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    //If sign in fails, display a message to the user
                    Log.w(TAG,"CreateUserWithEmale:failure", task.getException());
                    Toast.makeText(MainActivity.this,"Authentication failed.",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }
    
    private void updateUI(FirebaseUser currentUser) {

    }
}


/*

access user information

 FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
 if (user != null) {
 // Name, email address, and profile photo Url
 String name = user.getDisplayName();
 String email = user.getEmail();
 Uri photoUrl = user.getPhotoUrl();

 // Check if user's email is verified
 boolean emailVerified = user.isEmailVerified();

 // The user's ID, unique to the Firebase project. Do NOT use this value to
 // authenticate with your backend server, if you have one. Use
 // FirebaseUser.getToken() instead.
 String uid = user.getUid();
 }
 */