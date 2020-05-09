package com.devcrawlers.wesselni;

import android.content.Context;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devcrawlers.wesselni.entities.Complaint;


import java.util.ArrayList;

public class ComplaintAdapter extends BaseAdapter {
    ArrayList<Complaint> complaintsArrayList;
    Context context;

    public ComplaintAdapter(ArrayList<Complaint> complaints,Context c) {
        complaintsArrayList=complaints;
        context =c;
    }

    @Override
    public int getCount() {
        return complaintsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return complaintsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Complaint c=complaintsArrayList.get(position);
        view=LayoutInflater.from(context).inflate(R.layout.complaint_row, null);


        ((TextView)view.findViewById(R.id.textViewComplaint_fullName)).setText(c.getFullName());
        ((TextView)view.findViewById(R.id.textViewComplaint_Email)).setText(c.getEmail());
        ((TextView)view.findViewById(R.id.textViewComplaint_subject)).setText(c.getSubject());
        ((TextView) view.findViewById(R.id.textViewComplaint_message)).setText(c.getMessage());
        return view;
    }
}
