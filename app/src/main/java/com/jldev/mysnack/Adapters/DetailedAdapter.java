package com.jldev.mysnack.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jldev.mysnack.R;
import com.jldev.mysnack.Reviews.Review;

import java.util.List;

/**
 * Created by jeffr on 9-4-2018.
 */

public class DetailedAdapter extends ArrayAdapter<Review> {
    public DetailedAdapter(@NonNull Context context, List<Review> objects) {
        super(context, 0, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Review current = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_activity_detail_review,parent,false);
        }

        TextView date = convertView.findViewById(R.id.date_tv_detail);
        date.setText(current.date);

        TextView grade = convertView.findViewById(R.id.grade_tv_detail);
        String temp = current.grade+"";
        grade.setText(temp);

        TextView review = convertView.findViewById(R.id.review_tv_detail);
        review.setText(current.text);


        return convertView;
    }
}
