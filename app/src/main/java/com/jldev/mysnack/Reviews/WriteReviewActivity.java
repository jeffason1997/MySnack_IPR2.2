package com.jldev.mysnack.Reviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jldev.mysnack.ChooseActivity;
import com.jldev.mysnack.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteReviewActivity extends Activity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String user = mAuth.getCurrentUser().getEmail();
    private Integer[] numbers = new Integer[]{1,2,3,4,5,6,7,8,9,10};
    private double lng,lat;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        name = getIntent().getStringExtra("NAME");
        lat = getIntent().getDoubleExtra("LAT",999);
        lng = getIntent().getDoubleExtra("LNG",999);

        TextView place = findViewById(R.id.Title_TV);
        place.setText(name);

        Spinner grade = findViewById(R.id.Spinner_Grade);
        ArrayAdapter<Integer> mAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,numbers);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(mAdapter);

        EditText rev = findViewById(R.id.review_ET);
        Button submit = findViewById(R.id.SUBMITBUTT);
        submit.setOnClickListener(view -> {
            handleButtonClick();
            addReview(rev.getText().toString(),numbers[grade.getSelectedItemPosition()]);
            Intent i = new Intent(getApplicationContext(), ChooseActivity.class);
            startActivity(i);
        });

    }

    boolean handleButtonClick(){

        final boolean[] finished = new boolean[1];

        Map<String,Object> placeInfo = new HashMap<>();
        placeInfo.put("Name",name);
        placeInfo.put("Latitude",lat);
        placeInfo.put("longitude",lng);

        db.collection("users").document(user).collection("places").document(name).set(placeInfo)
                .addOnSuccessListener(documentReference -> Log.d("SUCCES", " review detailes:" + placeInfo.toString()))
                .addOnFailureListener(e -> Log.w("FAILED", " detailes:" + e));

        return finished[0];
    }

    void addReview(String text, int number){
        Map<String,Object> review = new HashMap<>();
        review.put("Review",text);
        review.put("Grade",number);

        Date time = new Date();
        String goodTime = new SimpleDateFormat("dd-MM-yyyy").format(time);

        db.collection("users").document(user).collection("places").document(name).collection("reviews").document(goodTime).set(review)
                .addOnSuccessListener(documentReference -> Log.d("SUCCES", " review detailes:" + review.toString()))
                .addOnFailureListener(e -> Log.w("FAILED", " detailes:" + e));
    }
}
