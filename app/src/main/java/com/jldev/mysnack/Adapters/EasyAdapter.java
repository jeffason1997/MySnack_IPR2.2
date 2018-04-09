package com.jldev.mysnack.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jldev.mysnack.R;

import java.util.ArrayList;

/**
 * Created by jeffr on 9-4-2018.
 */

public class EasyAdapter extends ArrayAdapter<String> {

    public EasyAdapter(@NonNull Context context, ArrayList<String> options) {
        super(context, 0 , options);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        String current = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_activity_choose_options,parent,false);
        }

        TextView name = convertView.findViewById(R.id.lvChooseActivity_OptionText);
        name.setText(current);

        return convertView;
    }
}
