package com.jldev.mysnack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jldev.mysnack.Adapters.EasyAdapter;
import com.jldev.mysnack.Navigation.NavigateActivity;
import com.jldev.mysnack.Reviews.ReviewActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ChooseActivity extends Activity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayAdapter mAdapter;
    private String document = mAuth.getCurrentUser().getEmail();
    private ArrayList<String> MENU = new ArrayList<>(Arrays.asList("Navigation","Reviews"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        mAdapter = new EasyAdapter(this.getApplicationContext(),MENU);

        ListView list = findViewById(R.id.ChooseScreen_OptionsListview);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    Intent intent = new Intent(getApplicationContext(), NavigateActivity.class);
                    startActivity(intent);
                } else if(i==1){
                    Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChooseActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkUser();
    }

    void checkUser(){
        db.collection("users").document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(!task.getResult().exists()){
                   Map<String, Object> user = new HashMap<>();
                    user.put("Email",mAuth.getCurrentUser().getEmail());
                    db.collection("users").document(document).set(user)
                            .addOnSuccessListener(documentReference -> Log.d("SUCCES", " user detailes:" + user.toString()))
                            .addOnFailureListener(e -> Log.w("FAILED", " detailes:" + e));
                } else {
                    System.out.println("BOIY IT ALREADY EXIST");
                }
            }
        });
    }
}
