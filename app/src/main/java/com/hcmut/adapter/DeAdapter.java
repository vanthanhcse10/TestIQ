package com.hcmut.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hcmut.testiq.R;

import java.util.List;

/**
 * Created by ThanhNguyen on 5/7/2017.
 */

public class DeAdapter extends ArrayAdapter<String>{
    Activity context;
    int resource;
    List<String> objects;
    public DeAdapter(Activity context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(this.resource, null);
        TextView txtTenDe = (TextView) view.findViewById(R.id.txtTenDe);
        txtTenDe.setText(this.objects.get(position));
        return view;
    }
}
