package com.example.siangapp.AllAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.siangapp.model.DivisiModel;

import java.util.List;

public class SpinnerDivisiAdapter extends ArrayAdapter<DivisiModel> {

   public SpinnerDivisiAdapter(@NonNull Context context, List<DivisiModel> pilarModel){
            super(context, android.R.layout.simple_spinner_item, pilarModel);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setText(getItem(position).getDivisi());
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setText(getItem(position).getDivisi());
            return view;
        }



    public Integer getDivisId(int position) {
        return getItem(position).getId();
    }





    }
