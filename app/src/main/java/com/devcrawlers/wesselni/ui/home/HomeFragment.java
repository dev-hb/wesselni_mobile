package com.devcrawlers.wesselni.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.devcrawlers.wesselni.R;

import java.util.Date;

public class HomeFragment extends Fragment {

    TextView date_text_view;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        date_text_view = root.findViewById(R.id.date_text_view);
        date_text_view.setText(new Date()+"");
        return root;
    }
}