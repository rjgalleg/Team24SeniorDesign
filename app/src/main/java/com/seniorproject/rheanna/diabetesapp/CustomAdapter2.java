package com.seniorproject.rheanna.diabetesapp;
import android.content.Context;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ArrayAdapter;

import android.widget.TextView;

import java.util.ArrayList;

/**

 * Created by Victor on 3/7/2016.

 */

public class CustomAdapter2 extends ArrayAdapter<UserCommentInfo> {

    public CustomAdapter2(Context context, ArrayList listItems) {

        super(context,R.layout.custom_row_2,listItems);

    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater myInflater = LayoutInflater.from(getContext());

        View customView = myInflater.inflate(R.layout.custom_row_2, parent, false);

        UserCommentInfo singleItem = getItem(position);

        TextView comment_username_text= (TextView)

                customView.findViewById(R.id.comment_username_text);

        TextView comment_text= (TextView) customView.findViewById(R.id.comment_text);

        comment_username_text.setText(singleItem.originalusername);

        comment_text.setText(singleItem.comment);

        return customView;

    }

}