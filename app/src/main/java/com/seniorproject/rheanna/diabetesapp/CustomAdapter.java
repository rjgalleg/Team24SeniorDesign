package com.seniorproject.rheanna.diabetesapp;
import android.content.Context;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ArrayAdapter;

import android.widget.TextView;

import java.util.ArrayList;

/**

 * Created by Victor on 2/18/2016.

 */

class CustomAdapter  extends ArrayAdapter<UserPostInfo> {

    public CustomAdapter(Context context, ArrayList listItems) {

        super(context,R.layout.custom_row ,listItems);

    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater myInflater = LayoutInflater.from(getContext());

        View customView = myInflater.inflate(R.layout.custom_row, parent, false);

        UserPostInfo singleItem = getItem(position);

        TextView username_text= (TextView)

                customView.findViewById(R.id.username_text);

        TextView title_text= (TextView) customView.findViewById(R.id.title_text);

        TextView userpost_text= (TextView)

                customView.findViewById(R.id.userpost_text);

        username_text.setText(singleItem.userName);

        title_text.setText(singleItem.title);

        userpost_text.setText(singleItem.userPost);

        return customView;

    }

}