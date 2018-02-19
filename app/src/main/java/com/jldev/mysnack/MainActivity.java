package com.jldev.mysnack;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    private FirebaseAuth mAuth;
    private static final String TAG = "SEE ME";
    private Button loginButton, signinButton;
    private EditText user, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        user = findViewById(R.id.MainScreen_UserField);
        password = findViewById(R.id.MainScreen_PasswordField);

        loginButton = findViewById(R.id.MainScreen_LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getText().toString().matches("") || password.getText().toString().matches("")){
                    Toast.makeText(MainActivity.this, "At least one field is empty.", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkIfEmail(user.getText().toString())) {
                        signIn(user.getText().toString(), password.getText().toString());
                    } else {
                        Toast.makeText(MainActivity.this, "Not a real email.\nplease try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signinButton = findViewById(R.id.MainScreen_SigninButton);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getText().toString().matches("") || password.getText().toString().matches("")){
                    Toast.makeText(MainActivity.this, "At least one field is empty.", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkIfEmail(user.getText().toString())) {
                        createAccount(user.getText().toString(), password.getText().toString());
                    } else {
                        Toast.makeText(MainActivity.this, "Not a real email.\nplease try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in succes, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:succes");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    //If sign in fails, display a message to the user
                    Log.w(TAG, "CreateUserWithEmale:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
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

    @Override
    public void onStart() {
        super.onStart();
        //Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    public void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null){
            Intent intent = new Intent(this, ChooseActivity.class);
            startActivity(intent);
        } else{
            System.out.println("still yeahboiy");
        }
    }

    private boolean checkIfEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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