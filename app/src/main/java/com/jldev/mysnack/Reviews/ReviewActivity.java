package com.jldev.mysnack.Reviews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jldev.mysnack.Adapters.EasyAdapter;
import com.jldev.mysnack.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class ReviewActivity extends Activity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String user = mAuth.getCurrentUser().getEmail();
    private ArrayList<String> PLACES = new ArrayList<>(Arrays.asList("No Reviews Yet"));
    private ArrayAdapter mAdapter;
    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);


        System.out.println(user);
        db.collection("users").document(user).collection("places").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    System.out.println(task.getResult().getDocuments().toString());
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (first) {
                            PLACES.set(0, document.getId());
                            first = false;
                            mAdapter.notifyDataSetChanged();
                        } else {
                            PLACES.add(document.getId());
                            mAdapter.notifyDataSetChanged();
                        }

                    }

                }
            }
        });


        mAdapter = new EasyAdapter(this.getApplicationContext(), PLACES);

        ListView list = findViewById(R.id.ChooseScreen_OptionsListview);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!PLACES.get(i).matches("No Reviews Yet")) {
                    Intent intent = new Intent(getApplicationContext(),DetailedReviewActivity.class);
                    intent.putExtra("NAME",PLACES.get(i));
                    startActivity(intent);
                }
            }
        });

    }
}
