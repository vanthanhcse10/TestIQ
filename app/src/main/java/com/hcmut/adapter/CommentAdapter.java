package com.hcmut.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hcmut.testiq.R;
import com.hcmut.testiq.models.Comment;

import java.util.List;

/**
 * Created by ThanhNguyen on 5/13/2017.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {

    Activity context;
    int resource;
    List<Comment> objects;

    public CommentAdapter(Activity context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(this.resource, null);

        TextView txtUser = (TextView) view.findViewById(R.id.txtUser);
        TextView txtContent = (TextView) view.findViewById(R.id.txtContent);
        TextView txtTime = (TextView) view.findViewById(R.id.txtTime);

        txtUser.setText(objects.get(position).getUsername());
        txtContent.setText(objects.get(position).getContent());
        txtTime.setText(objects.get(position).getDate());

        return view;
    }
}
