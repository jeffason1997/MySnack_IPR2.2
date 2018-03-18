package com.jldev.mysnack;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ChooseActivity extends Activity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        mAuth = FirebaseAuth.getInstance();
        System.out.println(mAuth.getCurrentUser().getEmail());
    }


}
