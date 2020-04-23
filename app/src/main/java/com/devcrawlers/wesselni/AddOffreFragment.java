package com.devcrawlers.wesselni;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import javax.microedition.khronos.egl.EGLDisplay;


public class AddOffreFragment extends Fragment {

    private EditText editTextStratCity,editTextTargetCity,editTextAdress,editTextPrix,editTextDate,editTextTime,editTextPosition;


    public AddOffreFragment() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_add_offre, container, false);
        editTextStratCity=view.findViewById(R.id.editTextStartCity);
        editTextTargetCity=view.findViewById(R.id.editTextTargetCity);
        editTextAdress=view.findViewById(R.id.editTextAdress);
        editTextDate=view.findViewById(R.id.editTextDate);
        editTextTime=view.findViewById(R.id.editTextTime);
        return view;
    }





}
