package com.jldev.mysnack.Reviews;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jldev.mysnack.Adapters.DetailedAdapter;
import com.jldev.mysnack.Adapters.EasyAdapter;
import com.jldev.mysnack.R;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailedReviewActivity extends Activity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String user = mAuth.getCurrentUser().getEmail();
    private ArrayList<Review> REVIEWS = new ArrayList<>();
    private ArrayAdapter mAdapter;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_review);

        name = getIntent().getStringExtra("NAME");
        TextView nameTv = findViewById(R.id.detailedReview_Name);
        nameTv.setText(name);

        db.collection("users").document(user).collection("places").document(name).collection("reviews").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Review review = new Review();
                        review.date = document.getId();
                        double temp = document.getDouble("Grade");
                        review.grade = (int) temp;
                        review.text = document.getString("Review");
                        REVIEWS.add(review);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


        mAdapter = new DetailedAdapter(this.getApplicationContext(), REVIEWS);
        ListView list = findViewById(R.id.detailedReview_lv);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}
