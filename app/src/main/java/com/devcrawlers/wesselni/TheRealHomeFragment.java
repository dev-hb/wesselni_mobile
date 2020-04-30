package com.devcrawlers.wesselni;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;



public class TheRealHomeFragment extends Fragment {

    TextView date_text_view;
    LinearLayout complaint;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_the_real_home, container, false);
        date_text_view = view.findViewById(R.id.date_text_view);
        date_text_view.setText(new Date()+"");
        complaint=view.findViewById(R.id.liner_complaint);
        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent( getContext(),ComplaintsActivity.class);
                startActivity(it);
            }
        });

        return view;


}

}
