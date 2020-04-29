package com.devcrawlers.wesselni;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;



public class TheRealHomeFragment extends Fragment {

    TextView date_text_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_the_real_home, container, false);
        date_text_view = view.findViewById(R.id.date_text_view);
        date_text_view.setText(new Date()+"");
        return view;
}

}
